package com.bachelors.speecher.service.asterisk;

import org.asteriskjava.fastagi.AsyncAgiServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Async AGI server initializing bean
 *
 * @author V.Khomyn
 */
public class AsteriskAsyncAgiBean implements InitializingBean {

    @Autowired
    private AsyncAgiMappingStrategy mappingStrategy;

    @Autowired
    private AsteriskAmiBean acdManager;

    @Override
    public void afterPropertiesSet() {
        AsyncAgiServer asyncAgiServer = new AsyncAgiServer(mappingStrategy);
        acdManager.getManagerConnection().addEventListener(asyncAgiServer);
    }

    public void setMappingStrategy(AsyncAgiMappingStrategy mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
    }

    public void setAcdManager(AsteriskAmiBean acdManager) {
        this.acdManager = acdManager;
    }
}