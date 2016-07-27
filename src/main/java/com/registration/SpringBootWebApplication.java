package com.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Launches spring application.
 */
@SpringBootApplication
public class SpringBootWebApplication {

    /**
     * Delegates to Spring Boot’s SpringApplication class by calling run.
     * SpringApplication will bootstrap application, starting Spring which
     * will in turn start the auto-configured Tomcat web server.
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
