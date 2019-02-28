package ua.com.alexandr.market.server.services.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ua.com.alexandr.market.server.model.AlertMessage;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Service
public class ClientsNotificationServiceImpl implements ClientsNotificationService {

    private final String wsEndpoint;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ClientsNotificationServiceImpl(SimpMessagingTemplate messagingTemplate, @Value("${ws.endpoint}") String wsEndpoint) {
        this.messagingTemplate = messagingTemplate;
        this.wsEndpoint = wsEndpoint;
    }

    @Override
    public void notify(AlertMessage message) {
        messagingTemplate.convertAndSend(wsEndpoint, message);
    }
}
