package com.bachelors.speecher.service.asterisk;

import com.bachelors.speecher.util.Logger;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.internal.ManagerConnectionImpl;

import java.util.HashSet;

public class AsteriskManagerConnection extends ManagerConnectionImpl {

    private final HashSet<String> supportedManagerEvents;
    private boolean supportAllManagerEvents = true;

    public AsteriskManagerConnection(
            String hostname,
            int port,
            String username,
            String password,
            long defaultResponseTimeout
    ) {
        super();
        setHostname(hostname);
        setPort(port);
        setUsername(username);
        setPassword(password);
        setDefaultResponseTimeout(defaultResponseTimeout);
        supportedManagerEvents = onInitializeSupportedManagerEvents(new HashSet<>());
    }

    @Override
    public void dispatchEvent(ManagerEvent event, Integer requiredHandlingTime) {
        if (supportAllManagerEvents || supportedManagerEvents.contains(event.getClass().getName())) {
//            Logger.log(event.toString());
            super.dispatchEvent(event, requiredHandlingTime);
        }
    }

    protected HashSet<String> onInitializeSupportedManagerEvents(HashSet<String> events) {
        {
            events.add(ProtocolIdentifierReceivedEvent.class.getName());
        }
        events.add(BridgeEvent.class.getName());
//        events.add(LinkEvent.class.getName());
//        events.add(UnlinkEvent.class.getName());
//        events.add(DialEvent.class.getName());
//        events.add(PeerStatusEvent.class.getName());
//        events.add(HangupEvent.class.getName());
//        events.add(PeerEntryEvent.class.getName());
//        events.add(ConnectEvent.class.getName());
//        events.add(StatusCompleteEvent.class.getName());
//        events.add(DisconnectEvent.class.getName());
//        events.add(CdrEvent.class.getName());
//        events.add(OriginateResponseEvent.class.getName());
//        events.add(OriginateFailureEvent.class.getName());
//        events.add(OriginateSuccessEvent.class.getName());
//        events.add(AgiExecEvent.class.getName());
//        events.add(AgiExecEndEvent.class.getName());
//        events.add(AgiExecStartEvent.class.getName());
//        events.add(AsyncAgiEvent.class.getName());
        return events;
    }
}