package com.woowahan.recipe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "사용자의 아이디가 중복됩니다"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "사용자의 이메일이 중복됩니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 다릅니다"),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "판매자를 찾을 수 없습니다"),
    SELLER_ALREADY(HttpStatus.CONFLICT, "이미 등록된 판매자 입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다"),
    ROLE_FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러입니다"),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다"),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "재료가 존재하지 않습니다"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다"),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "내용이 없습니다"),
    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "재고 수량이 없습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "장바구니에 담긴 재료를 찾을 수 없습니다"),
    RECIPE_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이템에 담긴 재료를 찾을 수 없습니다"),
    ALREADY_ARRIVED(HttpStatus.BAD_REQUEST, "이미 배송된 상품입니다"),
    SELECT_ORDER_ITEM(HttpStatus.FORBIDDEN, "주문할 상품을 선택해주세요"),
    MISMATCH_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 불일치 합니다"),
    INVALID_ORDER(HttpStatus.BAD_REQUEST, "주문 결제를 취소하셨습니다");


    private HttpStatus httpStatus;
    private String message;
}
