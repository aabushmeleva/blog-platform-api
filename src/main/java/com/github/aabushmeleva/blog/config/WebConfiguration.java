package com.github.aabushmeleva.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.github.aabushmeleva.blog.controller",
        "com.github.aabushmeleva.blog.service",
        "com.github.aabushmeleva.blog.dao",
        "com.github.aabushmeleva.blog.storage"
})
public class WebConfiguration {

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
