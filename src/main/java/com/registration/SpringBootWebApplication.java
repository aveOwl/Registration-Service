package com.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Launches spring application.
 */
@SpringBootApplication
public class SpringBootWebApplication {

    /**
     * Logging system.
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(SpringBootWebApplication.class);

    /**
     * Delegates to Spring Boot’s SpringApplication class by calling run.
     * SpringApplication will bootstrap application, starting Spring which
     * will in turn start the auto-configured Tomcat web server.
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        LOG.info("starting spring application ...");
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
