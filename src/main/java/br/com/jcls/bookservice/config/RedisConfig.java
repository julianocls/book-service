package br.com.jcls.bookservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private CacheConfigProperties cacheConfigProperties;

    private RedisCacheConfiguration createCacheConfiguration(long timeoutInMinutes) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(timeoutInMinutes));
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        if (Objects.nonNull(cacheConfigProperties.getCachesTTL())) {
            for (Map.Entry<String, String> cacheNameAndTimeout : cacheConfigProperties.getCachesTTL().entrySet()) {
                cacheConfigurations.put(cacheNameAndTimeout.getKey(), createCacheConfiguration(Long.parseLong(cacheNameAndTimeout.getValue())));
            }
        }
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(createCacheConfiguration(Long.parseLong(cacheConfigProperties.getDefaultTTL())))
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(cacheConfigProperties.getHost());
        redisStandaloneConfiguration.setPort(Integer.parseInt(cacheConfigProperties.getPort()));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
