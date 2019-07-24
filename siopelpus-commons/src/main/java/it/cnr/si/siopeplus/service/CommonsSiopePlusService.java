/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.si.siopeplus.service;

import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.MessaggioXML;
import it.cnr.si.siopeplus.model.Parameter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class CommonsSiopePlusService {
    protected static final String APPLICATION_ZIP = "application/zip";
    protected static final String  APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    public static final int TOO_MANY_REQUEST = 429;

    private transient static final Logger logger = LoggerFactory.getLogger(CommonsSiopePlusService.class);
    protected static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    public static final int TIMEOUT = 30;

    @Value("${siopeplus.certificate.password}")
    public String password;

    @Value("${siopeplus.certificate.path}")
    public String certificatePath;

    @Autowired
    private ApplicationContext appContext;

    public String getDataDa(Esito esito) {
        switch (esito) {
            case ACK:
                return Parameter.dataProduzioneDa.name();
            default:
                return Parameter.dataUploadDa.name();
        }
    }

    public String getDataA(Esito esito) {
        switch (esito) {
            case ACK:
                return Parameter.dataProduzioneA.name();
            default:
                return Parameter.dataUploadA.name();
        }
    }

    public abstract <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz);

    protected <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz, JAXBContext jc, Integer iterate) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(location);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", APPLICATION_ZIP);

            final HttpResponse response = client.execute(httpGet);
            if (!Optional.ofNullable(response).filter(httpResponse -> httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK).isPresent()) {
                logger.error("ERROR SIOPE+ for location {} iterate", response.getStatusLine(), iterate);
                if (response.getStatusLine().getStatusCode() == TOO_MANY_REQUEST) {
                    try {
                        TimeUnit.SECONDS.sleep(TIMEOUT);
                        if (iterate < 10) {
                            return getLocation(location, clazz, jc, ++iterate);
                        } else {
                            logger.error("ERROR SIOPE+ CANNOT ITERATE THAN 10");
                            throw new RuntimeException("ERROR SIOPE+ CANNOT ITERATE THAN 10");
                        }
                    } catch (InterruptedException e) {
                        logger.error("ERROR SIOPE+ for location", e);
                        throw new RuntimeException("ERROR SIOPE+ for location " + response.getStatusLine());
                    }
                } else {
                    throw new RuntimeException("ERROR SIOPE+ for location " + response.getStatusLine());
                }
            } else {
                final InputStream content = response.getEntity().getContent();
                ZipInputStream zipInputStream = new ZipInputStream(content);
                final ZipEntry nextEntry = Optional.ofNullable(zipInputStream.getNextEntry())
                        .orElseThrow(() -> new RuntimeException("Cannot download file from location: " + location));
                final String name = nextEntry.getName();
                final byte[] bytes = IOUtils.toByteArray(extractFileFromArchive(zipInputStream));

                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                final T object = Optional.ofNullable(
                        unmarshaller.unmarshal(new ByteArrayInputStream(bytes)))
                        .map(jaxbElement -> {
                            try {
                                return (T) jaxbElement;
                            } catch (ClassCastException _ex) {
                                return null;
                            }
                        })
                        .orElse(null);
                return new MessaggioXML<T>(name, bytes, object);
            }
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException | JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(client)
                    .ifPresent(httpClient -> {
                        try {
                            httpClient.close();
                        } catch (IOException e) {
                            logger.error("CANNOT CLOSE HTTPCLIENT");
                        }
                    });
        }
    }

    public InputStream extractFileFromArchive(ZipInputStream stream) {
        // build the path to the output file and then create the file
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            // create a buffer to copy through
            byte[] buffer = new byte[2048];

            // now copy out of the zip archive until all bytes are copied
            int len;
            while ((len = stream.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CloseableHttpClient getHttpClient() throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore identityKeyStore = KeyStore.getInstance("jks");
        identityKeyStore.load(appContext.getResource(certificatePath).getInputStream(), password.toCharArray());
        SSLContext sslContext = SSLContexts
                .custom()
                // load identity keystore
                .loadKeyMaterial(identityKeyStore, password.toCharArray())
                // load trust keystore
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2", "TLSv1.1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
    }
}
