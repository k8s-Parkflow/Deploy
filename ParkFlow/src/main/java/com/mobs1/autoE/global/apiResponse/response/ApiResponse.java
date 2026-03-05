package com.mobs1.autoE.global.apiResponse.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobs1.autoE.global.apiResponse.code.SuccessCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    @Builder
    private ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, String customMessage, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .message(customMessage)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, String customMessage) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .message(customMessage)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> ok() {
        return success(SuccessCode.SUCCESS_READ);
    }

    public static <T> ApiResponse<T> created(T data) {
        return success(SuccessCode.SUCCESS_CREATE, data);
    }

    public static <T> ApiResponse<T> created() {
        return success(SuccessCode.SUCCESS_CREATE);
    }
}
