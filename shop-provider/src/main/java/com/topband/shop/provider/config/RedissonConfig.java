package com.topband.shop.provider.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RedissonConfig
 * @packageName: com.topband.shop.provider.config
 * @description: RedissonConfig
 * @date 2022/9/13 13:47
 */
@Configuration
public class RedissonConfig {
    private static final String REDIS_PROTOCOL = "redis://";

    @Resource
    private RedissonProperties redissonProperties;

    @Bean
    public RedissonClient redissonClient() {
        String redisPassword = redissonProperties.getPassword();
        List<String> redisClusterNodes = redissonProperties.getCluster().getNodes();

        Config config = new Config();
        List<String> clusterNodes = new ArrayList<>();
        for (String redisClusterNode : redisClusterNodes) {
            redisClusterNode = REDIS_PROTOCOL + redisClusterNode;
            clusterNodes.add(redisClusterNode);
        }
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(clusterNodes.toArray(new String[0]));
        clusterServersConfig.setPassword(redisPassword);
        return Redisson.create(config);
    }
}
