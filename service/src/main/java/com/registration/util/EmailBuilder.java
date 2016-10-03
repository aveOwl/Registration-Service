package com.registration.util;

import com.registration.model.User;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:application-mail.properties")
public class EmailBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(EmailBuilder.class);
    private static final String CONFIRM_URI = "http://localhost:8080/registration/confirm/"; // TODO deal with this

    @Value("${spring.mail.email}")
    private String senderEmail;

    @Value("${spring.mail.subject}")
    public String subject;

    @Value("${spring.freemarker.view-names}")
    public String templateName;

    @Value("${spring.freemarker.resources}")
    public String resources;

    private JavaMailSender mailSender;
    private FreeMarkerConfigurer configurer;
    private MimeMessageHelperProvider helperProvider;

    @Autowired
    public EmailBuilder(final FreeMarkerConfigurer configurer,
                        final JavaMailSender mailSender,
                        final MimeMessageHelperProvider helperProvider) {
        this.configurer = configurer;
        this.mailSender = mailSender;
        this.helperProvider = helperProvider;
    }

    /**
     * Creates complete email message and handles exceptions that may occur
     * during the email assembling process.
     *
     * @return completed email message.
     */
    public MimeMessage createEmail(final User user) {
        try {
            MimeMessage message = this.getEmailMessage(user);

            LOG.debug("Creating message with subject: {} for User: {}",
                    message.getSubject(), user);

            return message;
        } catch (Exception e) {
            LOG.error("Error while creating email: {}", e.getMessage());
            throw new MailPreparationException("Failed to construct email.", e);
        }
    }

    /**
     * Completely constructs email. Specifies sender ard recipient
     * information, email body.
     *
     * @return complete email message.
     * @throws Exception on error.
     */
    private MimeMessage getEmailMessage(final User user) throws Exception {
        final MimeMessage message = this.mailSender.createMimeMessage();
        final MimeMessageHelper helper = this.helperProvider.getMimeMessageHelper(message);

        if (helper == null) {
            LOG.error("Failed to get message helper, message helper was null.");
            throw new MailPreparationException("Failed to get message helper, message helper was null.");
        }

        final Resource logo = new ClassPathResource(this.resources);
        final String emailBody = this.getEmailBody(user);

        helper.setFrom(this.senderEmail);
        helper.setSubject(this.subject);
        helper.setTo(user.getEmail());
        helper.setText(emailBody, true);
        helper.addInline("mail-logo", logo); // image

        LOG.debug("Constructing email: {Sender email address={}, Message subject={}, Recipient email address={}}",
                this.senderEmail, this.subject, user.getEmail());

        return message;
    }

    /**
     * Wires up template and model and converts it into String.
     *
     * @return String representation of the email body.
     * @throws Exception on error.
     */
    private String getEmailBody(final User user) throws Exception {
        final Template template = this.configurer
                .createConfiguration()
                .getTemplate(this.templateName);

        final Map<String, String> model = this.getEmailModel(user);

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    /**
     * Constructs confirmation email body for the given user,
     * providing email, password and confirmation URL.
     *
     * @return map containing key-value pairs for
     * email, password, and confirmation URL.
     */
    private Map<String, String> getEmailModel(final User user) {
        final Map<String, String> model = new HashMap<>();

        model.put("email", user.getEmail());
        model.put("password", this.getStarsPassword(user));
        model.put("confirmUrl", this.getConfirmUrl(user));

        LOG.debug("Constructing model: {email={}, password={}, confirmUrl={}}",
                user.getEmail(), this.getStarsPassword(user), this.getConfirmUrl(user));

        return model;
    }

    /**
     * Constructs partly hidden user password, revealing two last characters
     * of the actual password and replacing other characters with '*' symbol.
     *
     * @return a string representing partly hidden user password.
     */
    private String getStarsPassword(final User user) {
        final char[] array = user.getEmail().toCharArray();
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
    private String getConfirmUrl(final User user) {
        return CONFIRM_URI +
                Base64Utils.encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
    }
}
