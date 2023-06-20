package com.bachelors.speecher.service.asterisk;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;

public class AsteriskManagerEventListenerImpl extends AbstractManagerEventListener implements ManagerEventListener {

    @Override
    public void onManagerEvent(final ManagerEvent event) {}
}
