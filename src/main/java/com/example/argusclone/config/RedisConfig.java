package com.example.argusclone.config;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 1. Create a shared ObjectMapper with JavaTime support
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RedisSerializer<String> keySerializer = new StringRedisSerializer();

        GenericJackson2JsonRedisSerializer defaultSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(defaultSerializer))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(10));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withCacheConfiguration("COURSE_CACHE", createCacheConfig(mapper, CourseResponse.class))
                .withCacheConfiguration("COURSE_CACHE_LIST", createCacheConfig(mapper, List.class))
                .withCacheConfiguration("GROUP_CACHE", createCacheConfig(mapper, GroupResponse.class))
                .withCacheConfiguration("GROUP_CACHE_LIST", createCacheConfig(mapper, List.class))
                .withCacheConfiguration("INSTRUCTOR_CACHE", createCacheConfig(mapper, InstructorResponse.class))
                .withCacheConfiguration("STUDENT_CACHE", createCacheConfig(mapper, StudentResponse.class))
                .withCacheConfiguration("STUDENT_CACHE_LIST", createCacheConfig(mapper, List.class))
                .withCacheConfiguration("STUDENT_COURSE_RESULTS_CACHE", createCacheConfig(mapper, List.class))
                .withCacheConfiguration("SYLLABUS_CACHE", createCacheConfig(mapper, SyllabusResponse.class))
                .withCacheConfiguration("SCORE_CACHE_LIST", createCacheConfig(mapper, List.class))
                .withCacheConfiguration("LECTURE_CACHE", createCacheConfig(mapper, List.class))
                .build();
    }

    private RedisCacheConfiguration createCacheConfig(ObjectMapper mapper, Class<?> clazz) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()
                ))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new Jackson2JsonRedisSerializer<>(mapper, clazz)
                ))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(10));
    }
}
