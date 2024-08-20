package com.footballgg.server.base.baseresponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true,1000,"요청에 성공하였습니다"),
    SUCCESS_EMAIL_SIGNUP(true, 1001, "이메일 회원가입이 성공하였습니다."),
    SUCCESS_EMAIL_LOGIN(true, 1002, "이메일 로그인이 성공하였습니다."),
    SUCCESS_LOGOUT(true,1003,"로그아웃에 성공하였습니다."),
    SUCCESS_REFRESH_TOKEN(true,1004,"액세스 토큰을 재발급했습니다."),
    SUCCESS_VERIFY_EMAIL(true, 1005, "이메일 인증에 성공하였습니다."),

    // 2000 : Request 에러
    FAILED_INVALID_INPUT(false, 2000, "입력하지 않은 정보가 존재합니다."),
    FAILED_NOT_CORRECT_AUTHCODE(false, 2001, "인증번호가 일치하지 않습니다."),
    // 3000 : Response 에러
    FAILED_NOT_FOUND_USER(false, 3000, "존재하지 않는 회원입니다."),
    FAILED_DUPLICATED_EMAIL(false, 3001, "중복된 이메일입니다."),
    FAILED_VALIDATE_TOKEN(false,3002,"액세스 토큰이 유효하지 않습니다."),
    FAILED_VERIFY_EMAIL(false,3003,"이메일 인증에 실패했습니다."),
    // 4000 : Server 에러
    FAILED_EMAIL_LOGIN(false, 4000, "이메일 로그인에 실패하였습니다."),
    FAILED_LOGOUT(false,4001,"로그아웃에 실패하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
