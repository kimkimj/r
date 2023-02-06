package com.woowahan.recipe.domain.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayCancelVo {

    private String imp_uid;
    private String userName;
    private Long orderId;

}
