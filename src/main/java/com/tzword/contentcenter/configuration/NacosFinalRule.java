package com.tzword.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/4 22:23
 */
@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        //负载均衡规则:优先选择同集群下，符合metadata的实例
        //如果没有，就选择所有集群下，符合metadata的实例
        try {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            String targetVersion = nacosDiscoveryProperties.getMetadata().get("target-version");
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer)this.getLoadBalancer();
            String name = loadBalancer.getName();
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //所有实例
            List<Instance> instances = namingService.selectInstances(name, true);
            List<Instance> metadataMathInstances = instances;
            if (StringUtils.isNotBlank(targetVersion)){
                metadataMathInstances = instances.stream().filter(instance -> Objects.equals(targetVersion,instance.getMetadata().get("version"))).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(metadataMathInstances)){
                    log.error("为找到元数据的目标实例");
                    return null;
                }
            }

            List<Instance> clusterMetaMathInstanse = metadataMathInstances;
            //如果配置了集群名称，需要筛选集群下元数据匹配的实例
            if(StringUtils.isNotBlank(clusterName)){
                clusterMetaMathInstanse = metadataMathInstances.stream().filter(instance -> Objects.equals(clusterName,instance.getClusterName())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(clusterMetaMathInstanse)){
                    clusterMetaMathInstanse = metadataMathInstances;
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(clusterMetaMathInstanse);
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        return null;
    }
}
