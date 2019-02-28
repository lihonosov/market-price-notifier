package ua.com.alexander.market.server.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.alexander.market.server.facade.AlertFacade;
import ua.com.alexander.market.server.model.AlertInput;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AlertRestController.class)
public class AlertRestControllerTest {

    private static final String ALERT_URI = "/alert";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AlertFacade alertFacade;

    private AlertInput input;

    @Before
    public void setUp() {
        input = new AlertInput("BTC-USD", new BigDecimal(500));
    }

    @Test
    public void successfullySetAlert() throws Exception {
        mvc.perform(put(getUrlWithParams(input.getPair(), input.getLimit().toString()))).andExpect(status().isOk());
        verify(alertFacade, times(1)).subscribe(input);
    }

    @Test
    public void givenNoParameters_setAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(put(ALERT_URI)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    @Test
    public void givenOnlyCurrencyPairParameter_setAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(put(getUrlWithParams("BTC-EUR", ""))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    @Test
    public void givenOnlyLimitParameter_setAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(put(getUrlWithParams("", "123.45"))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    @Test
    public void successfullyRemoveAlert() throws Exception {
        mvc.perform(delete(getUrlWithParams(input.getPair(), input.getLimit().toString()))).andExpect(status().isOk());
        verify(alertFacade, times(1)).unsubscribe(input);
    }

    @Test
    public void givenNoParameters_removeAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(delete(ALERT_URI)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    @Test
    public void givenOnlyCurrencyPairParameter_removeAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(delete(getUrlWithParams("BTC-EUR", ""))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    @Test
    public void givenOnlyLimitParameter_removeAlertShouldReturnBadRequest() throws Exception {
        mvc.perform(delete(getUrlWithParams("", "123.45"))).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        verifyZeroInteractions(alertFacade);
    }

    private String getUrlWithParams(String pair, String limit) {
        return ALERT_URI + "?pair=" + pair + "&limit=" + limit;
    }
}
