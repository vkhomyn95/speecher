package com.bachelors.speecher.service.asterisk;

import com.bachelors.speecher.util.Logger;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class AsteriskAmiBean implements InitializingBean {
    private ManagerConnection managerConnection;
    private ManagerEventListener managerEventListener;

    public AsteriskAmiBean(ManagerConnection managerConnection, ManagerEventListener managerEventListener) {
        this.managerConnection = managerConnection;
        this.managerEventListener = managerEventListener;
    }

    @Autowired
    public void setManagerEventListener(ManagerEventListener managerEventListener) {
        this.managerEventListener = managerEventListener;
    }

    public void start() {
        try {
            managerConnection.login();
        } catch (Exception e) {
            Logger.log("Manager Connection failed. Reason: ".concat(e.getMessage()));
        }
    }

    @Override
    public void afterPropertiesSet() {
        initializeManagerConnectionListener();
    }

    /**
     * initialize manager event listener to handling asterisk events
     */
    protected void initializeManagerConnectionListener() {
        try {
            getManagerConnection().addEventListener(managerEventListener);
        } catch (ManagerCommunicationException e) {
            Logger.log("Manager Connection failed. Reason: ".concat(e.getMessage()));
        }
    }

    /**
     * Stop {@link ManagerConnection}
     */
    public void stop() {
        managerConnection.logoff();
    }

    public ManagerConnection getManagerConnection() {
        return managerConnection;
    }

    @Autowired
    public void setManagerConnection(ManagerConnection managerConnection) {
        this.managerConnection = managerConnection;
    }
}