package com.topband.shop.provider.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RedissonProperties
 * @packageName: com.topband.shop.provider.config
 * @description: RedissonProperties
 * @date 2022/9/13 14:22
 */
@Component
@RefreshScope
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonProperties {
    private String password;

    private Cluster cluster;

    @Getter
    @Setter
    public static class Cluster {
        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }
}
