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

    @Autowired
    private ApplicationContext appCtx;

    @Value("#{${siopeplus.codiceuniuo}}")
    private Map<String, String> uniuo;

    @Value("${siopeplus.codice.a2a}")
    public String a2a;

    private Map<String, GiornaleDiCassaSiopePlusService> giornaleDiCassaSioepServices;

    @PostConstruct
    private void createService() {
        if (Optional.ofNullable(giornaleDiCassaSioepServices).isPresent()) {
            for (Map.Entry<String, String> entry : uniuo.entrySet()) {
                giornaleDiCassaSioepServices.put(entry.getKey(), (GiornaleDiCassaSiopePlusService) appCtx.getBean("giornaleDiCassaSiopePlusService", a2a, entry.getValue()));
            }
        }
    }

    public GiornaleDiCassaSiopePlusService getOrdinativiSiopePlusService(String key) throws SIOPEPlusServiceNotInstantiated{
        return giornaleDiCassaSioepServices.entrySet().stream()
                .filter(e -> key.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new SIOPEPlusServiceNotInstantiated("cannot find GiornaleDiCassaSiopePlusService for ".concat( key )));
    }

    public List<GiornaleDiCassaSiopePlusService> getListGiornaleCassaSiopeService(){
        if ( Optional.ofNullable(giornaleDiCassaSioepServices).isPresent()) {
            return giornaleDiCassaSioepServices.values().stream().collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public Map<String,GiornaleDiCassaSiopePlusService> getMapGiornaleCassaSiopeService(){
        return giornaleDiCassaSioepServices;
    }
}
