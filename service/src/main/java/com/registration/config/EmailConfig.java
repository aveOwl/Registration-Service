package com.registration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Properties;

@Component
@PropertySource("classpath:application-mail.properties")
public class EmailConfig {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailConfig.class);

    /**
     * Mail server host.
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * Mail server port.
     */
    @Value("${spring.mail.port}")
    private String port;

    /**
     * Username for the account at the mail host.
     */
    @Value("${spring.mail.username}")
    private String userName;

    /**
     * Password for the account at the mail host
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Template loader path for the FreeMarker configuration.
     */
    @Value("${spring.freemarker.template-loader-path}")
    public String templatePath;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(this.host);
        javaMailSender.setPort(Integer.valueOf(this.port));
        javaMailSender.setUsername(this.userName);
        javaMailSender.setPassword(this.password);

        LOG.debug("Configuring sender properties: {host={}, port={}, username={}, password={}}",
                this.host, this.port, this.userName, this.password);

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", true);

        javaMailSender.setJavaMailProperties(properties);
        LOG.debug("Configuring mail properties: {}", properties.toString());

        return javaMailSender;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();

        freeMarkerConfigurer.setTemplateLoaderPath(this.templatePath);

        LOG.debug("Configuring FreeMaker: {template-loader-path={}}", this.templatePath);

        return freeMarkerConfigurer;
    }
}
