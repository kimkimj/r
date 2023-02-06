package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

    Optional<SellerEntity> findBySellerName(String sellerName);
    Optional<SellerEntity> findByEmail(String email);

}
