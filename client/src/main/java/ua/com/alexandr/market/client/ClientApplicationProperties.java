package ua.com.alexandr.market.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Data
@Validated
@Configuration
@Component
@ConfigurationProperties(prefix = "market.server")
public class ClientApplicationProperties {
    @NotBlank
    private String wsUrl;
    @NotBlank
    private String wsSubscriptionUri;
}
