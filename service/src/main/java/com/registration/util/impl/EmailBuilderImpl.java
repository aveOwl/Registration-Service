package com.registration.util.impl;

import com.registration.model.User;
import com.registration.util.EmailBuilder;
import com.registration.util.EmailConfigurer;
import com.registration.util.MimeMessageHelperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:application-mail.properties")
public class EmailBuilderImpl implements EmailBuilder {
    private static Logger LOG = LoggerFactory.getLogger(EmailBuilderImpl.class);

    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.email}")
    private String senderEmail;

    private EmailConfigurer configurer;
    private MimeMessageHelperProvider helperProvider;

    @Autowired
    public EmailBuilderImpl(EmailConfigurer configurer, MimeMessageHelperProvider helperProvider) {
        this.configurer = configurer;
        this.helperProvider = helperProvider;
    }

    /**
     * Creates complete email message and handles exceptions that may occur
     * during the email assembling process.
     *
     * @return completed email message.
     */
    @Override
    public MimeMessage createEmail(User user) {
        try {
            MimeMessage message = this.getEmailMessage(user);

            LOG.debug("Creating message for User: {}", user);

            return message;
        } catch (Exception e) {
            LOG.error("Error while creating email: {}", e.getMessage());
            throw new MailPreparationException("Failed to create email.", e);
        }
    }

    /**
     * Completely constructs email. Specifies sender ard recipient
     * information, email body.
     *
     * @return complete email message.
     * @throws Exception on error.
     */
    private MimeMessage getEmailMessage(User user) throws Exception {
        MimeMessageHelper helper = this.helperProvider.getMimeMessageHelper();

        if (helper == null) { // TODO should use Optional to avoid this
            LOG.error("Failed to get message helper, message helper was null.");
            throw new MailPreparationException("Failed to get message helper, message helper was null.");
        }

        Resource logo = this.configurer.getEmailResource();
        String emailBody = this.configurer.getEmailBody(user);

        helper.setFrom(this.senderEmail);
        helper.setSubject(this.subject);
        helper.setTo(user.getEmail());
        helper.setText(emailBody, true);
        helper.addInline("mail-logo", logo); // image

        LOG.debug("Constructing email: {Sender email address={}, Message subject={}, Recipient email address={}}",
                  this.senderEmail,
                  this.subject,
                  user.getEmail());

        return helper.getMimeMessage();
    }
}
