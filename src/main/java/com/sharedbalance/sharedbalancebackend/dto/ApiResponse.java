package com.sharedbalance.sharedbalancebackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiResponse {

    private String status;
    private Object payload;
    private Object errorInfo;
    private String serverTime;

    public static ApiResponse success(Object payload) {
        return ApiResponse.builder()
                .status("success")
                .payload(payload)
                .errorInfo(null)
                .serverTime(Instant.now().toString())
                .build();
    }

    public static ApiResponse error(Object errorInfo) {
        return ApiResponse.builder()
                .status("error")
                .payload(null)
                .errorInfo(errorInfo)
                .serverTime(Instant.now().toString())
                .build();
    }
}
