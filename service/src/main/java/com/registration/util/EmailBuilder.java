package com.registration.util;

import com.registration.model.User;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@PropertySource("classpath:application-mail.properties")
public class EmailBuilder {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailBuilder.class);

    /**
     * Confirmation URL.
     */
    private static final String CONFIRM = "http://localhost:8080/registration/confirm/";

    /**
     * Username for the account at the mail host.
     */
    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Subject of the message.
     */
    @Value("${spring.mail.subject}")
    public String subject;

    /**
     * Name of the specific template.
     */
    @Value("${spring.freemarker.view-names}")
    public String templateName;

    /**
     * Path to the resource.
     */
    @Value("${spring.freemarker.resources}")
    public String resources;

    private User user;

    private JavaMailSender mailSender;
    private FreeMarkerConfigurer configurer;

    @Autowired
    public EmailBuilder(final FreeMarkerConfigurer configurer,
                        final JavaMailSender mailSender) {
        this.configurer = configurer;
        this.mailSender = mailSender;
    }

    /**
     * Recipient of the email.
     * @param user user for whom email is being created.
     */
    public void setRecipient(final User user) {
        this.user = user;
    }

    /**
     * Sends fully constructed email.
     */
    public void sendEmail() {
        try {
            MimeMessage email = this.getEmailMessage();

            this.mailSender.send(email);
        } catch (Exception e) {
            LOG.error("Error while creating email: {}", e.getMessage());
            throw new MailPreparationException("Failed to construct email.");
        }
    }

    /**
     * Completely constructs email. Specifying sender, subject,
     * recipient, email body, resources.
     * @return complete email message.
     * @throws Exception on error.
     */
    private MimeMessage getEmailMessage() throws Exception {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Resource logo = new ClassPathResource(this.resources);
        String emailBody = this.getEmailBody();

        helper.setFrom(this.sender);
        helper.setSubject(this.subject);
        helper.setTo(this.user.getEmail());
        helper.setText(emailBody, true);
        helper.addInline("mail-logo", logo); // image

        LOG.debug("Constructing email: {Sender email address={}, Message subject={}, Recipient email address={}}",
                this.sender, this.subject, this.user.getEmail());

        return message;
    }

    /**
     * Wires up template and model and converts it into String.
     * @return String representation of the email body.
     * @throws Exception on error.
     */
    private String getEmailBody() throws Exception {
        Template template = this.configurer
                .createConfiguration()
                .getTemplate(this.templateName);

        Map<String, String> model = this.getEmailModel();

        String stringTemplate =
                FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        return stringTemplate;
    }

    /**
     * Constructs confirmation email body for the given user,
     * providing email, password and confirmation URL.
     * @return map containing key-value pairs for
     * email, password, and confirmation URL.
     */
    private Map<String, String> getEmailModel() {
        Map<String, String> model = new HashMap<>();

        model.put("email", this.user.getEmail());
        model.put("password", this.getStarsPassword());
        model.put("confirmUrl", this.getConfirmUrl());

        LOG.debug("Constructing model: {email={}, password={}, confirmUrl={}}",
                this.user.getEmail(), this.getStarsPassword(), this.getConfirmUrl());

        return model;
    }

    /**
     * Constructs partly hidden user password,
     * revealing two last characters of the actual password
     * and replacing other characters with '*' symbol.
     * @return sting same length as a password, revealing
     * only two last characters of it.
     */
    private String getStarsPassword() {
        char[] array = this.user.getEmail().toCharArray();
        for (int i = 0; i < array.length - 2; i++) {
            array[i] = '*';
        }
        return new String(array);
    }

    /**
     * Constructs unique conformation url.
     * Using combination of user email and passwords creates
     * unique string with encrypted user data associated with
     * this specific user.
     * @return conformation link which contains user specific data.
     */
    private String getConfirmUrl() {
        return CONFIRM +
                Base64Utils.encodeToString((this.user.getEmail() + ":" + this.user.getPassword()).getBytes());
    }
}
