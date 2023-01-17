package com.woowahan.recipe.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RecipeRestController.class)
class RecipeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;

    @Autowired
    ObjectMapper objectMapper;


    void 레시피_ID_단건_조회() {
        //given

        //when

        //then
    }
}