package com.tzword.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/4 9:39
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        // 1.找到指定服务的所有实例 A
        // 2.过滤出相同集群下的所有实例 B
        // 3.如果B是空，就用A
        // 4.基于权重的负载均衡算法，返回1个实例

        try {
            // 拿到配置文件中的集群名称
            String clusterName = nacosDiscoveryProperties.getClusterName();
            // 拿到服务名称
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            // 拿到服务发现的API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            // 1.找到指定服务的所有的实例A
            List<Instance> instances = namingService.selectInstances(name, true);
            // 2.过滤出相同集群下的所有实例B
            List<Instance> sameClusterInstances = instances.stream().filter(instance -> Objects.equals(instance.getClusterName(), clusterName)).collect(Collectors.toList());
            // 3.如果B是空，就用A
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                instancesToBeChosen = instances;
                log.info("发生跨集群调用，name ={},clusterName ={},instances={}",name,clusterName,instances);
            }else{
                instancesToBeChosen = sameClusterInstances;
            }
            // 4.基于权重的负载均衡算法，返回1个实例
            Instance hostByRandomWeight2 = ExtendBalancer.getHostByRandomWeight2(instancesToBeChosen);
            log.info("选择的实例是 port={},instance={}",hostByRandomWeight2.getPort(),hostByRandomWeight2);

            return new NacosServer(hostByRandomWeight2);
        } catch (NacosException e) {
            e.printStackTrace();
        }


        return null;
    }
}


class ExtendBalancer extends Balancer{
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}