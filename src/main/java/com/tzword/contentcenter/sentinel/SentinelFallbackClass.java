package com.tzword.contentcenter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/8 13:58
 */
@Slf4j
public class SentinelFallbackClass {

    /**
     * @Description: 处理限流或者降级(这里有点bug，不太好使)
     * @param a 1
     * @return java.lang.String
     * @throws
     * @author jianghy
     * @date 2021/4/8 14:02 
     */
    public static String fallback(String a){
        log.info("接口被降级了");
        return "接口被降级了";
    }

}
