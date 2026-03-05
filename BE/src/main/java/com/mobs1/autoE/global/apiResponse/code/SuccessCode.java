package com.mobs1.autoE.global.apiResponse.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 조회 (S001~S099)
    SUCCESS_READ(HttpStatus.OK, "S001", "조회에 성공했습니다."),

    // 생성 (S100~S199)
    SUCCESS_CREATE(HttpStatus.CREATED, "S100", "생성에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
