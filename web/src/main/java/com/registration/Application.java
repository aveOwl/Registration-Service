package com.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@SpringBootApplication
@EnableAsync
public class Application extends WebMvcConfigurerAdapter {

    @Bean
    public ResourceUrlEncodingFilter filter() {
        return new ResourceUrlEncodingFilter();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") // handle all paths
                .addResourceLocations("classpath:/static/", "/webjars/*")
                .resourceChain(true)
                .addResolver(
                        new VersionResourceResolver()
                                .addFixedVersionStrategy("1.0.SNAPSHOT", "/**/*.js") // for .js files prepend with version
                                .addContentVersionStrategy("/**")) // for css files - hash strategy
                .addTransformer(new AppCacheManifestTransformer()); // code can be used offline
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
