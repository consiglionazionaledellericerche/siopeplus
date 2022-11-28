<h1 align="center">
  <a href="https://github.com/consiglionazionaledellericerche/siopeplus">
    SIOPE+ Spring client
  </a>
</h1>
<p align="center">
  Artifact that allows integration with SIOPE+, with orders and cash journal.
</p>
<p align="center">
  <a href="https://github.com/consiglionazionaledellericerche/siopeplus/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/License-AGPL%20v3-blue.svg" alt="siopeplus is released under the GNU AGPL v3 license." />
  </a>
  <a href="https://mvnrepository.com/artifact/it.cnr.si/siopeplus">
    <img alt="Maven Central" src="https://img.shields.io/maven-central/v/it.cnr.si/siopeplus.svg?style=flat" alt="Current version on maven central.">
  </a>
</p>

## Environment variable
| Nome                             | Variabile d'ambiente             |Descrizione|
|----------------------------------|----------------------------------|---|
| siopeplus.base_url               | SIOPEPLUS_BASE_URL               |La URL di base per il servizio i.e. https://certa2a.siopeplus.it 
| siopeplus.vAPI                   | SIOPEPLUS_VAPI                   |La versione delle API i.e. v1 
| siopeplus.endpoints              | SIOPEPLUS_ENDPOINTS              |Lista degli endpoint separati da "," i.e. BT,BI
| siopeplus.endpoint.BT.a2a        | SIOPEPLUS_ENDPOINT_BT_A2A        |La credenziale A2A è un codice alfanumerico nella forma A2A-<123456789>
| siopeplus.endpoint.BT.uniuo      | SIOPEPLUS_ENDPOINT_BT_UNIUO      |Codice univoco ufficio presente in [Indice PA](https://www.indicepa.gov.it/public-services/opendata-read-service.php??dstype=FS&filename=amministrazioni.txt)
| siopeplus.codice.ente.bt         | SIOPEPLUS_CODICE_ENTE_BT         |Codice ente concordato con l'istituto cassiere
| siopeplus.codice.tramite.ente    | SIOPEPLUS_CODICE_TRAMITE_ENTE    |codice utenza applicativa A2A del Tramite PA mittente (se l’Ente si avvale di Tramite) ovvero della PA mittente
| siopeplus.codice.tramite.ente.bt | SIOPEPLUS_CODICE_TRAMITE_ENTE_BT |codice utenza applicativa A2A del Tramite BT destinatario (se la BT si avvale di Tramite) ovvero della BT destinataria 
| siopeplus.certificate.password   | SIOPEPLUS_CERTIFICATE_PASSWORD   |Password del certificato caricato in ambiente di [Collaudo SIOPE+](https://certregistration.siopeplus.it)
| siopeplus.certificate.path       | SIOPEPLUS_CERTIFICATE_PATH       |Path del certificato PKS può assumere i valori `classpath:/cert.p12` `file:/etc/cert.p12`
| sign.username                    | SIGN_USERNAME                    |Nome utente per la firma remota del flusso da inviare alla piattaforma SIOPE+
| sign.password                    | SIGN_PASSWORD                    |Password per la firma remota del flusso da inviare alla piattaforma SIOPE+
| sign.otp                         | SIGN_OTP                         |OTP per la firma remota del flusso da inviare alla piattaforma SIOPE+



## 👏 How to Contribute

The main purpose of this repository is to continue evolving SIOPE+. We want to make contributing to this project as easy and transparent as possible, and we are grateful to the community for contributing bugfixes and improvements.

## 📄 License

siopeplus is GNU AFFERO GENERAL PUBLIC LICENSE licensed, as found in the [LICENSE][l] file.

[l]: https://github.com/consiglionazionaledellericerche/siopeplus/blob/master/LICENSE