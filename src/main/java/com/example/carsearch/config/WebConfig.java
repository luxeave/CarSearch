package com.example.carsearch.config;

import com.example.carsearch.interceptor.RateLimitInterceptor;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final Bucket rateLimitBucket;

    public WebConfig(Bucket rateLimitBucket) {
        this.rateLimitBucket = rateLimitBucket;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitBucket))
                .addPathPatterns("/api/**");
    }
}