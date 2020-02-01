package br.com.clearinvest.clivserver.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quickfix.field.*;
import quickfix.fix44.Message;
import quickfix.fix44.NewOrderSingle;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import static br.com.clearinvest.clivserver.util.LangUtil.isNullOrEmpty;

@Service
public class FixMessageFactory {

    private final Logger log = LoggerFactory.getLogger(FixMessageFactory.class);

    @Value("${cliv.serverIpAndName}")
    String serverIpAndName;

    public NewOrderSingle createNewOrderSingle(String clOrderId, String side, String ordType) {
        NewOrderSingle message = new NewOrderSingle(
            new ClOrdID(clOrderId),
            new Side(side.charAt(0)),
            new TransactTime(LocalDateTime.now()),
            new OrdType(ordType.charAt(0)));
        setAuditFields(message);
        return message;
    }

    public void setAuditFields(Message message) {
        if (!isNullOrEmpty(serverIpAndName)) {
            message.setString(10719, getCurrentServerIpAndName());
            message.setString(9933, "Mult3");
        }
    }

    private String getCurrentServerIpAndName() {
        return isNullOrEmpty(serverIpAndName) ? getLocalhostExternalIp() + "#" + getLocalhostName() :serverIpAndName;
    }

    private String getLocalhostExternalIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            log.debug("unexpected error", e);
            return "127.0.0.1";
        }
    }

    private String getLocalhostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.debug("unexpected error", e);
            return "unknown";
        }
    }

}
