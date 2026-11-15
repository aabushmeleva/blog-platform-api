package com.github.aabushmeleva.blog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
}
