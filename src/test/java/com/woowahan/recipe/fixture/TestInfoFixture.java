package com.woowahan.recipe.fixture;

import lombok.Getter;
import lombok.Setter;

public class TestInfoFixture {
    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setRecipeId(1L);
        info.setUserName("testUser");
        info.setPassword("testPassword");
        return info;
    }

    @Getter
    @Setter
    public static class TestInfo {
        private Long recipeId;
        private String userName;
        private String password;
    }

}
