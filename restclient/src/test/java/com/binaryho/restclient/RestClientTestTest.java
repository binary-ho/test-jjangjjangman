package com.binaryho.restclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.binaryho.restclient.service.OrderDto;
import com.binaryho.restclient.service.UserOrderService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(UserOrderService.class)
@ExtendWith(SpringExtension.class)
public class RestClientTestTest {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private MockRestServiceServer mockServer;

    private String ORDER_API_URL = "http://localhost:8080/order?orderNumber=";

    @Test
    void 메서드로_Mock객체를_만들어_활용할_수_있다() {
        // given
        String expectOrderNo = "1";
        Long expectAmount = 1000L;
        LocalDateTime expectOrderDateTime = LocalDateTime.of(2018, 9, 29, 0, 0);

        String expectResult = "{\"orderNo\":\"1\",\"amount\":1000,\"orderDateTime\":\"2018-09-29 00:00:00\"}";
        mockServer.expect(requestTo(ORDER_API_URL + expectOrderNo))
            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));

        // when
        OrderDto response = userOrderService.getMemberOrder(expectOrderNo);

        // then
        assertThat(response.getOrderNo()).isEqualTo(expectOrderNo);
        assertThat(response.getAmount()).isEqualTo(expectAmount);
        assertThat(response.getOrderDateTime()).isEqualTo(expectOrderDateTime);
    }
}
