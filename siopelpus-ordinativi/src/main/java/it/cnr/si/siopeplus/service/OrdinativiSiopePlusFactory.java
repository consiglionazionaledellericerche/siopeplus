package it.cnr.si.siopeplus.service;

import it.cnr.si.siopeplus.exception.SIOPEPlusServiceNotInstantiated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class OrdinativiSiopePlusFactory {
    @Value("#{'${siopeplus.endpoints}'.split(',')}")
    public List<String> endpoints;
    @Autowired
    private ApplicationContext appCtx;
    private final Map<String, OrdinativiSiopePlusService> ordinantiviSiopeServices = new HashMap<String, OrdinativiSiopePlusService>();

    @PostConstruct
    private void createService() {
        endpoints.stream().forEach(s -> {
            final String endpint = CommonsSiopePlusService.PREFIX.concat(".").concat(s).concat(".");
            ordinantiviSiopeServices.put(s, (OrdinativiSiopePlusService) appCtx.getBean(
                    "ordinativiSiopePlusService",
                    appCtx.getEnvironment().getProperty(endpint.concat("a2a")),
                    appCtx.getEnvironment().getProperty(endpint.concat("uniuo")),
                    appCtx.getEnvironment().getProperty(endpint.concat("flusso")),
                    appCtx.getEnvironment().getProperty(endpint.concat("ack")),
                    appCtx.getEnvironment().getProperty(endpint.concat("esitoflusso")),
                    appCtx.getEnvironment().getProperty(endpint.concat("esitoapplicativo"))
            ));
        });
    }

    public OrdinativiSiopePlusService getOrdinativiSiopePlusService(String key) throws SIOPEPlusServiceNotInstantiated {
        return ordinantiviSiopeServices.entrySet().stream()
                .filter(e -> key.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new SIOPEPlusServiceNotInstantiated("cannot find OrdinantiviSiopePlusService for ".concat(key)));
    }

    public List<OrdinativiSiopePlusService> getListOrdinativiSiopeService() {
        if (Optional.ofNullable(ordinantiviSiopeServices).isPresent()) {
            return ordinantiviSiopeServices.values().stream().collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public Map<String, OrdinativiSiopePlusService> getOrdinativiSiope() {
        return ordinantiviSiopeServices;
    }
}