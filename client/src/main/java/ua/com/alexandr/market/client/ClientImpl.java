package ua.com.alexandr.market.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ua.com.alexandr.market.client.connector.ClientToServerWsConnector;

import java.math.BigDecimal;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class ClientImpl implements Client, ApplicationRunner {

    private final ClientToServerWsConnector connector;
    private final RestTemplate restTemplate;
    private final String serverRestEndpoint;

    @Autowired
    public ClientImpl(ClientToServerWsConnector connector, RestTemplate restTemplate,
                      @Value("${market.server.rest-endpoint}") String serverRestEndpoint) {
        this.connector = connector;
        this.restTemplate = restTemplate;
        this.serverRestEndpoint = serverRestEndpoint;
    }

    @Override
    public void run(ApplicationArguments args) {
        subscribe();
    }

    @Override
    public void subscribe() {
        connector.connect();
    }

    @Override
    public void setAlert(String pair, BigDecimal limit) {
        restTemplate.put(buildUri(pair, limit), null);
    }

    @Override
    public void removeAlert(String pair, BigDecimal limit) {
        restTemplate.delete(buildUri(pair, limit));
    }

    private String buildUri(String pair, BigDecimal limit) {
        return UriComponentsBuilder
                .fromUriString(serverRestEndpoint)
                .queryParam("pair", pair)
                .queryParam("limit", limit)
                .build()
                .toUriString();
    }
}
