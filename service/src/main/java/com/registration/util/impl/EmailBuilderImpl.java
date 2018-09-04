package com.registration.util.impl;

import com.registration.model.User;
import com.registration.util.EmailBuilder;
import com.registration.util.EmailConfigurer;
import com.registration.util.MimeMessageHelperProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:application-mail.properties")
@Slf4j
@RequiredArgsConstructor
public class EmailBuilderImpl implements EmailBuilder {

    private final EmailConfigurer configurer;
    private final MimeMessageHelperProvider helperProvider;
    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.email}")
    private String senderEmail;

    /**
     * Creates complete email message and handles exceptions that may occur
     * during the email assembling process.
     *
     * @return completed email message.
     */
    @Override
    @SneakyThrows
    public MimeMessage createEmail(User user) {
        val message = this.getEmailMessage(user);
        log.debug("Creating message for User: {}", user);
        return message;
    }

    /**
     * Completely constructs email. Specifies sender ard recipient
     * information, email body.
     *
     * @return complete email message.
     * @throws Exception on error.
     */
    private MimeMessage getEmailMessage(User user) throws Exception {
        val helper = this.helperProvider.getMimeMessageHelper();

        if (helper == null) { // TODO should use Optional to avoid this
            throw new MailPreparationException("Failed to get message helper, message helper was null.");
        }

        val logo = this.configurer.getEmailResource();
        val emailBody = this.configurer.getEmailBody(user);

        helper.setFrom(this.senderEmail);
        helper.setSubject(this.subject);
        helper.setTo(user.getEmail());
        helper.setText(emailBody, true);
        helper.addInline("mail-logo", logo); // image

        log.debug("Constructing email: {Sender email address={}, Message subject={}, Recipient email address={}}",
                this.senderEmail,
                this.subject,
                user.getEmail());

        return helper.getMimeMessage();
    }
}
