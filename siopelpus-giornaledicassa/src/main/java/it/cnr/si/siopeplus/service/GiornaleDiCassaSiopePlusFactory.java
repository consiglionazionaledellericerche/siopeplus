package it.cnr.si.siopeplus.service;

import it.cnr.si.siopeplus.exception.SIOPEPlusServiceNotInstantiated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiornaleDiCassaSiopePlusFactory {

    @Value("#{'${siopeplus.endpoints}'.split(',')}")
    public List<String> endpoints;
    @Autowired
    private ApplicationContext appCtx;
    private final Map<String, GiornaleDiCassaSiopePlusService> giornaleDiCassaSiopePlusServices = new HashMap<String, GiornaleDiCassaSiopePlusService>();

    @PostConstruct
    private void createService() {
        endpoints.stream().forEach(s -> {
            final String endpint = CommonsSiopePlusService.PREFIX.concat(".").concat(s).concat(".");
            giornaleDiCassaSiopePlusServices.put(s, (GiornaleDiCassaSiopePlusService) appCtx.getBean(
                    "giornaleDiCassaSiopePlusService",
                    appCtx.getEnvironment().getProperty(endpint.concat("a2a")),
                    appCtx.getEnvironment().getProperty(endpint.concat("uniuo")),
                    appCtx.getEnvironment().getProperty(endpint.concat("giornaledicassa"))
            ));
        });
    }

    public GiornaleDiCassaSiopePlusService getOrdinativiSiopePlusService(String key) throws SIOPEPlusServiceNotInstantiated {
        return giornaleDiCassaSiopePlusServices.entrySet().stream()
                .filter(e -> key.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new SIOPEPlusServiceNotInstantiated("cannot find GiornaleDiCassaSiopePlusService for ".concat(key)));
    }

    public List<GiornaleDiCassaSiopePlusService> getListGiornaleCassaSiopeService() {
        if (Optional.ofNullable(giornaleDiCassaSiopePlusServices).isPresent()) {
            return giornaleDiCassaSiopePlusServices.values().stream().collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public Map<String, GiornaleDiCassaSiopePlusService> getMapGiornaleCassaSiopeService() {
        return giornaleDiCassaSiopePlusServices;
    }
}
