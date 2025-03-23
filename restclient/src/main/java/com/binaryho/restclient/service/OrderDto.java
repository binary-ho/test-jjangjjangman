package com.binaryho.restclient.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private String orderNo;
    private Long amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime orderDateTime; //문제 요소

    @Builder
    public OrderDto(String orderNo, Long amount, LocalDateTime orderDateTime) {
        this.orderNo = orderNo;
        this.amount = amount;
        this.orderDateTime = orderDateTime;
    }
}
