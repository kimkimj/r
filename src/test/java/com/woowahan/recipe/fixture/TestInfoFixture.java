package com.woowahan.recipe.fixture;

import lombok.Getter;
import lombok.Setter;

public class TestInfoFixture {
    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setUserName("testUser");
        info.setPassword("testPassword");
        info.setRecipeId(1L);
        info.setItemId(1L);
        return info;
    }

    @Getter
    @Setter
    public static class TestInfo {
        private String userName;
        private String password;
        private Long recipeId;
        private Long itemId;
    }

}
