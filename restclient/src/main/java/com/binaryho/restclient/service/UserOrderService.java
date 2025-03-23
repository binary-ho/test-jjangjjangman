package com.binaryho.restclient.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserOrderService {
    private final RestTemplate restTemplate;
    private static final String ORDER_API_URL = "http://localhost:8080/order";

    public UserOrderService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public OrderDto getMemberOrder(String orderNumber) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ORDER_API_URL)
            .queryParam("orderNumber", orderNumber);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(
            builder.toUriString(), HttpMethod.GET, httpEntity, OrderDto.class).getBody();
    }
}
