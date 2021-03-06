package ribbonconfiguration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.tzword.contentcenter.configuration.NacosFinalRule;
import com.tzword.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.tzword.contentcenter.configuration.NacosWeightedRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jianghy
 * @Description: ribbon配置文件
 * @date 2021/4/2 14:04
 */
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule(){
        return new NacosFinalRule();
    }
}
