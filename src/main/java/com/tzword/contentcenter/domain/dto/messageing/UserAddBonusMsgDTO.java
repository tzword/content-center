package com.tzword.contentcenter.domain.dto.messageing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/12 20:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddBonusMsgDTO {
    //为谁加积分
    private Integer userId;
    //加多少积分
    private Integer bonus;
}
