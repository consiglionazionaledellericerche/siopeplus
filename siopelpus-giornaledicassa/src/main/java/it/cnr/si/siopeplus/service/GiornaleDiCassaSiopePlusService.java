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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.si.siopeplus.model.Lista;
import it.cnr.si.siopeplus.model.MessaggioXML;
import it.cnr.si.siopeplus.model.Parameter;
import it.cnr.si.siopeplus.giornaledicassa.custom.ObjectFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GiornaleDiCassaSiopePlusService extends CommonsSiopePlusService{
    private transient static final Logger logger = LoggerFactory.getLogger(GiornaleDiCassaSiopePlusService.class);
    private final String a2a;

    private final String uniuo;
    private final String urlGiornaleDiCassa;

    public GiornaleDiCassaSiopePlusService(String a2a, String uniuo, String urlGiornaleDiCassa) {
        this.a2a = a2a;
        this.uniuo = uniuo;
        this.urlGiornaleDiCassa = urlGiornaleDiCassa;
    }
    public Lista getListaMessaggi(LocalDateTime dataDa, LocalDateTime dataA, Boolean download, Integer pagina) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(urlGiornaleDiCassa);
            Optional.ofNullable(dataDa)
                    .ifPresent(date -> builder.setParameter(Parameter.dataUploadDa.name(), dataDa.format(formatter)));
            Optional.ofNullable(dataA)
                    .ifPresent(date -> builder.setParameter(Parameter.dataUploadA.name(), dataA.format(formatter)));
            Optional.ofNullable(download)
                    .ifPresent(aBoolean -> builder.setParameter(Parameter.download.name(), aBoolean.toString()));
            Optional.ofNullable(pagina)
                    .ifPresent(integer -> builder.setParameter(Parameter.pagina.name(), integer.toString()));
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", APPLICATION_JSON_UTF8);

            final HttpResponse response = client.execute(httpGet);
            if (!Optional.ofNullable(response).filter(httpResponse -> httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK).isPresent()) {
                logger.error(response.getStatusLine().getReasonPhrase());
                return new Lista();
            }
            Gson gson = new GsonBuilder().setDateFormat(pattern).create();
            return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Lista.class);
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(client)
                    .ifPresent(httpClient -> {
                        try {
                            httpClient.close();
                        } catch (IOException e) {
                            logger.error("CANNOT CLOSE HTTPCLIENT", e);
                        }
                    });
        }
    }
    @Override
    public <T> MessaggioXML<T> getLocation(String location, Class<T> clazz) {
        try {
            return getLocation(location, clazz, JAXBContext.newInstance(
                    it.siopeplus.giornaledicassa.ObjectFactory.class,
                    it.cnr.si.siopeplus.giornaledicassa.custom.ObjectFactory.class
            ), 0);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
