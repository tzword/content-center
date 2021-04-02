package com.tzword.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * @author jianghy
 * @Description: ribbon配置
 * @date 2021/4/2 14:03
 */
@Configuration
//单个配置：
//@RibbonClient(name = "user-center",configuration = RibbonConfiguration.class)
//全局配置：
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
}
