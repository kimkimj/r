package com.woowahan.recipe.service;

import com.woowahan.recipe.domain.UserRole;
import com.woowahan.recipe.domain.dto.sellerDto.*;
import com.woowahan.recipe.domain.entity.SellerEntity;
import com.woowahan.recipe.exception.AppException;
import com.woowahan.recipe.exception.ErrorCode;
import com.woowahan.recipe.repository.SellerRepository;
import com.woowahan.recipe.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

    private long expiredTimeMs = 60 * 60 * 1000; //토큰 유효시간: 1시간

    public SellerEntity validateSeller(String sellerName) {
        SellerEntity seller = sellerRepository.findBySellerName(sellerName)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getMessage()));
        return seller;
    }


    public SellerJoinResponse join(SellerJoinRequest sellerJoinRequest) {
        // sellerName (ID) 중복확인
        sellerRepository.findBySellerName(sellerJoinRequest.getSellerName())
                .ifPresent(seller -> {
                            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
                        });

        // email 중복확인
        sellerRepository.findByEmail(sellerJoinRequest.getEmail())
                .ifPresent(seller -> {
                        throw new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage());
                });

        SellerEntity seller = sellerRepository.save(sellerJoinRequest.toEntity(
                encoder.encode(sellerJoinRequest.getPassword())
        ));

        return new SellerJoinResponse(seller.getSellerName(),
                String.format("%s님의 회원가입이 완료되었습니다.", seller.getSellerName()));
    }

    public String login(String sellerName, String password) {
        // seller가 존재하는지 확인
        SellerEntity seller = validateSeller(sellerName);

        // password가 틀린 경우
        if (!encoder.matches(password, seller.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtTokenUtils.createToken(sellerName, secretKey, expiredTimeMs);
    }

    public SellerResponse findById(Long id) {
        SellerEntity seller = sellerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getMessage()));

        return SellerResponse.toSellerResponse(seller);
    }

    //TODO: 미완성
    /*
    public SellerResponse update(String sellerName, SellerUpdateRequest sellerUpdateRequest) {
        // seller가 존재하는지 확인
        SellerEntity seller = validateSeller(sellerName);

        // 본인이거나 ADMIN이 아니면 에러
        if (!seller.getSellerName().equals(sellerName) && seller.getUserRole() != UserRole.ADMIN) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        return SellerResponse.toSellerResponse();
    }*/

    public SellerDeleteResponse deleteSeller(String sellerName) {
        SellerEntity seller = validateSeller(sellerName);

        // 본인이거나 ADMIN이 아니면 에러
        if (!seller.getSellerName().equals(sellerName) && seller.getUserRole() != UserRole.ADMIN) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        sellerRepository.delete(seller);
        return new SellerDeleteResponse(sellerName, "회원 삭제가 완료되었습니다");
    }
}
