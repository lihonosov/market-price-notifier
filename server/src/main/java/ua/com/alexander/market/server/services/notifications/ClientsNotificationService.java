package ua.com.alexander.market.server.services.notifications;

import ua.com.alexander.market.server.model.AlertMessage;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public interface ClientsNotificationService {
    void notify(AlertMessage message);
}
