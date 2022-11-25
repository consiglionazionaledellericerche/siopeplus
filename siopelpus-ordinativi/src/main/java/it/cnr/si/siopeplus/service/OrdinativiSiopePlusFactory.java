package it.cnr.si.siopeplus.service;

import it.cnr.si.siopeplus.exception.SIOPEPlusServiceNotInstantiated;
import it.cnr.si.siopeplus.exception.SIOPEPlusServiceUnavailable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdinativiSiopePlusFactory {
    @Autowired
    private ApplicationContext appCtx;

    @Value("#{${siopeplus.codiceuniuo}}")
    private Map<String, String> uniuo;

    @Value("${siopeplus.codice.a2a}")
    public String a2a;

    private Map<String, OrdinativiSiopePlusService> ordinantiviSiopeServices=new HashMap<String, OrdinativiSiopePlusService>();;

    @PostConstruct
    private void createService() {
        if (Optional.ofNullable(ordinantiviSiopeServices).isPresent()) {
            for (Map.Entry<String, String> entry : uniuo.entrySet()) {
                ordinantiviSiopeServices.put(entry.getKey(), (OrdinativiSiopePlusService) appCtx.getBean("ordinativiSiopePlusService", a2a, entry.getValue()));
            }
        }
    }

    public OrdinativiSiopePlusService getOrdinativiSiopePlusService(String key) throws SIOPEPlusServiceNotInstantiated {
        return ordinantiviSiopeServices.entrySet().stream()
                .filter(e -> key.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new SIOPEPlusServiceNotInstantiated("cannot find OrdinantiviSiopePlusService for ".concat( key )));
    }
    public List<OrdinativiSiopePlusService> getListOrdinativiSiopeService(){
        if ( Optional.ofNullable(ordinantiviSiopeServices).isPresent()) {
            return ordinantiviSiopeServices.values().stream().collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public Map<String,OrdinativiSiopePlusService> getOrdinativiSiope(){
        return ordinantiviSiopeServices;
    }
}