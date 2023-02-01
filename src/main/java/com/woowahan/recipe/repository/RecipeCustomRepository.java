package com.woowahan.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowahan.recipe.domain.entity.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.woowahan.recipe.domain.entity.QLikeEntity.likeEntity;
import static com.woowahan.recipe.domain.entity.QRecipeEntity.recipeEntity;
import static com.woowahan.recipe.domain.entity.QUserEntity.userEntity;


@RequiredArgsConstructor
@Repository
public class RecipeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Page<RecipeEntity> findByUserAndLove(String userNameCond, Pageable pageable) {
        List<RecipeEntity> contents = queryFactory.select(recipeEntity)
                .from(recipeEntity)
                .join(recipeEntity.likes, likeEntity)
                .join(likeEntity.user, userEntity)
                .where(likeEntity.user.userName.eq(userNameCond))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = contents.size();
        return new PageImpl<>(contents, pageable, total);
    }

}
