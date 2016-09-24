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

    private static final Logger LOG = LoggerFactory.getLogger(EmailConfig.class);

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;

    @Value("${spring.mail.email}")
    private String senderEmail;

    @Value("${spring.mail.password}")
    private String senderPassword;

    @Value("${spring.freemarker.template-loader-path}")
    public String templatePath;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(this.mailHost);
        javaMailSender.setPort(Integer.valueOf(this.mailPort));
        javaMailSender.setUsername(this.senderEmail);
        javaMailSender.setPassword(this.senderPassword);

        LOG.debug("Configuring senderEmail properties: {mailHost={}, mailPort={}, senderEmail={}, senderPassword={}}",
                this.mailHost, this.mailPort, this.senderEmail, this.senderPassword);

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
