package com.woowahan.recipe.exception;

public class NotEnoughStockException extends BaseException{
    public NotEnoughStockException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
