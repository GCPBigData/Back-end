package br.com.clearinvest.clivserver.quickfixj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.Password;
import quickfix.field.Username;

public class ClientApplicationAdapter implements Application {

    private static final Logger log = LoggerFactory.getLogger(ClientApplicationAdapter.class);

    @Override
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: SessionId={}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogon: SessionId={}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("onLogout: SessionId={}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        log.info("toAdmin: Message={}, SessionId={}", message, sessionId);

        if (message instanceof quickfix.fix44.Logon) {
            quickfix.fix44.Logon logonMessage = (quickfix.fix44.Logon) message;
            logonMessage.set(new Username("160119"));
            logonMessage.set(new Password("Lf@2019"));
        }
    }

    /** Messages sent by this app to the OMS. */
    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }

    /** Messages received by this app from the OMS. */
    @Override
    public void fromApp(Message message, SessionID sessionId)
        throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("fromApp: Message={}, SessionId={}", message, sessionId);
    }

}
