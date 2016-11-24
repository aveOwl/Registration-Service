package com.registration.util.impl;

import com.registration.model.User;
import com.registration.util.EmailConfigurer;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:application-mail.properties")
public class EmailConfigurerImpl implements EmailConfigurer {
    private static Logger LOG = LoggerFactory.getLogger(EmailBuilderImpl.class);
    private static String CONFIRM_URI = "http://localhost:8080/registration/confirm/"; // TODO deal with this

    @Value("${spring.freemarker.view-names}")
    public String templateName;
    @Value("${spring.freemarker.resources}")
    public String resources;

    private FreeMarkerConfigurer configurer;

    @Autowired
    public EmailConfigurerImpl(FreeMarkerConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailBody(User user) {
        try {
            String emailBody = this.constructEmailBody(user);

            LOG.debug("Constructing email body for User: {}", user);

            return emailBody;
        } catch (Exception e) {
            throw new MailPreparationException("Failed to construct email body.", e);
        }
    }

    @Override
    public Resource getEmailResource() {
        return new ClassPathResource(this.resources);
    }

    /**
     * Wires up template and model and converts it into String.
     *
     * @return String representation of the email body.
     * @throws Exception on error.
     */
    private String constructEmailBody(User user) throws Exception {
        Template template = this.configurer.createConfiguration().getTemplate(this.templateName);

        Map<String, String> model = this.getEmailModel(user);

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    /**
     * Constructs confirmation email body for the given user,
     * providing email, password and confirmation URL.
     *
     * @return map containing key-value pairs for email, password, and confirmation URL.
     */
    private Map<String, String> getEmailModel(User user) {
        Map<String, String> model = new HashMap<>();

        model.put("email", user.getEmail());
        model.put("password", this.getStarsPassword(user));
        model.put("confirmUrl", this.getConfirmUrl(user));

        LOG.debug("Constructing model: {email={}, password={}, confirmUrl={}}", user.getEmail(), this.getStarsPassword(user), this.getConfirmUrl(user));

        return model;
    }

    /**
     * Constructs partly hidden user password, revealing two last characters
     * of the actual password and replacing other characters with '*' symbol.
     *
     * @return a string representing partly hidden user password.
     */
    private String getStarsPassword(User user) {
        char[] array = user.getEmail().toCharArray();
        for (int i = 0; i < array.length - 2; i++) {
            array[i] = '*';
        }
        return new String(array);
    }

    /**
     * Creates unique string with encrypted user email and password
     * associated with this specific user.
     *
     * @return conformation link which contains user specific data.
     */
    private String getConfirmUrl(User user) {
        return CONFIRM_URI + Base64Utils.encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
    }
}
