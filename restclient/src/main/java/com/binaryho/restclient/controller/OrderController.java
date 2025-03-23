package com.binaryho.restclient.controller;

import com.binaryho.restclient.service.OrderDto;
import com.binaryho.restclient.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final UserOrderService userOrderService;

    @GetMapping
    public ResponseEntity<OrderDto> getMemberOrder(@RequestParam("orderNumber") String orderNo) {
        OrderDto response = userOrderService.getMemberOrder(orderNo);
        return ResponseEntity.ok(response);
    }
}
