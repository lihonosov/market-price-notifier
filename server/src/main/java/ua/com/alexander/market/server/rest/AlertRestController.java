package ua.com.alexander.market.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.alexander.market.server.facade.AlertFacade;
import ua.com.alexander.market.server.model.AlertInput;

import javax.validation.Valid;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@RestController
@RequestMapping("/alert")
public class AlertRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertRestController.class);

    private final AlertFacade alertFacade;

    @Autowired
    public AlertRestController(AlertFacade alertFacade) {
        this.alertFacade = alertFacade;
    }

    @PutMapping
    public void setAlert(@Valid AlertInput input) {
        LOGGER.debug("New Limit: {} ", input);
        alertFacade.subscribe(input);
    }

    @DeleteMapping
    public void removeAlert(@Valid AlertInput input) {
        LOGGER.debug("Delete Limit: {}", input);
        alertFacade.unsubscribe(input);
    }
}
