package com.tzword.contentcenter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/8 13:58
 */
@Slf4j
public class SentinelBlockHandlerClass {

    /**
     * @Description: 处理限流或者降级
     * @param a 1
     * @param b 2
     * @return java.lang.String
     * @throws
     * @author jianghy
     * @date 2021/4/8 14:02 
     */
    public static String block(String a, BlockException b){
        log.info("接口被限流了",b);
        return "接口被限流了";
    }

}
