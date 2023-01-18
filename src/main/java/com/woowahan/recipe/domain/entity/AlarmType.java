package com.woowahan.recipe.domain.entity;

public enum AlarmType {

    NEW_REVIEW_ON_RECIPE("NEW REVIEW");

    private final String message;

    AlarmType(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return name() + " " + message;
    }
}
