package com.ztuo.bc.wallet.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: YangZhi
 * @Date: 2018/11/6 2:29 PM
 */
@Configuration
@Component
public class RedisConfig {
    @Value("${spring.redis.host:}")
    private String hostName;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    @Value("${spring.redis.database:0}")
    private Integer database;

    @Value("${spring.redis.timeout:1000}")
    private Integer redisTimeout;

    @Value("${spring.redis.maxIdle:300}")
    private Integer maxIdle;

    @Value("${spring.redis.maxTotal:1000}")
    private Integer maxTotal;

    @Value("${spring.redis.maxWaitMillis:1000}")
    private Integer maxWaitMillis;

    @Value("${spring.redis.minEvictableIdleTimeMillis:300000}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${spring.redis.numTestsPerEvictionRun:1024}")
    private Integer numTestsPerEvictionRun;

    @Value("${spring.redis.timeBetweenEvictionRunsMillis:30000}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.redis.sentinel.master:mymaster}")
    private String master;

    @Value("${spring.redis.testOnBorrow:true}")
    private boolean testOnBorrow;

    @Value("${spring.redis.testWhileIdle:true}")
    private boolean testWhileIdle;

    /**
     * JedisPoolConfig ?????????
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // ???????????????
        jedisPoolConfig.setMaxIdle(maxIdle);
        // ????????????????????????????????????
        jedisPoolConfig.setMaxTotal(maxTotal);
        // ??????????????????????????????
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // ????????????????????????????????? ??????1800000??????(30??????)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // ????????????????????? ????????????????????? ????????????????????? : 1/abs(n), ??????3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // ???????????????????????????(??????) ???????????????,????????????????????????, ??????-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // ?????????????????????????????????????????????,??????????????????,????????????????????????????????????????????????
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // ???????????????????????????, ??????false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        return jedisPoolConfig;
    }

    /**
     * ?????????
     *
     * @param @param  jedisPoolConfig
     * @param @return
     * @return JedisConnectionFactory
     * @throws
     * @Title: JedisConnectionFactory
     * @date 2019???11???6???
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisClientConfiguration.DefaultJedisClientConfigurationBuilder jedisClientConfiguration = (JedisClientConfiguration.DefaultJedisClientConfigurationBuilder)JedisClientConfiguration.builder();
        jedisClientConfiguration.poolConfig(jedisPoolConfig);
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(redisTimeout));
        if (hostName.split(",").length == 1) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(hostName.split(":")[0]);
            redisStandaloneConfiguration.setPort(Integer.valueOf(hostName.split(":")[1]));
            redisStandaloneConfiguration.setDatabase(database);
            if(StringUtils.isNotEmpty(redisPassword)) {
                redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
            }
            else {
                redisStandaloneConfiguration.setPassword(RedisPassword.of((String) null));
            }
            return new JedisConnectionFactory(redisStandaloneConfiguration,
                    jedisClientConfiguration.build());
        } else {
            Map<String, Object> source = new HashMap<String, Object>();
            source.put("spring.redis.sentinel.master", master);
            source.put("spring.redis.sentinel.nodes", hostName);
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
            if(StringUtils.isNotEmpty(redisPassword)) {
                redisSentinelConfiguration.setPassword(RedisPassword.of(redisPassword));
            }
            else {
                redisSentinelConfiguration.setPassword(RedisPassword.of((String) null));
            }
            redisSentinelConfiguration.setDatabase(database);
            return new JedisConnectionFactory(redisSentinelConfiguration,
                    jedisClientConfiguration.build());
        }
    }

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key??????String??????????????????
        template.setKeySerializer(stringRedisSerializer);
        // hash???key?????????String??????????????????
        template.setHashKeySerializer(stringRedisSerializer);
        // value?????????????????????jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash???value?????????????????????jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}

