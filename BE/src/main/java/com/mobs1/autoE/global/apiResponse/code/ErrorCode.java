package com.mobs1.autoE.global.apiResponse.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 (E000~E009)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E000", "잘못된 요청입니다."),

    // 존/주차 (E100~E199)
    ZONE_NOT_FOUND(HttpStatus.NOT_FOUND, "E100", "존 정보를 찾을 수 없습니다."),
    SLOT_CATEGORY_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "E101", "지원하지 않는 슬롯 타입입니다."),
    CURRENT_PARKING_NOT_FOUND(HttpStatus.NOT_FOUND, "E102", "현재 주차 중인 차량 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
