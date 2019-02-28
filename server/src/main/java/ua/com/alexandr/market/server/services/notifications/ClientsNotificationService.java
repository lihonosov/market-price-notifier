package ua.com.alexandr.market.server.services.notifications;

import ua.com.alexandr.market.server.model.AlertMessage;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public interface ClientsNotificationService {
    void notify(AlertMessage message);
}
