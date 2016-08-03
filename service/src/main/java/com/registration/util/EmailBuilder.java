package com.registration.util;

import com.registration.model.User;
import freemarker.template.utility.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:application-mail.properties")
public class EmailBuilder {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailBuilder.class);

    /**
     * Request URL.
     */
    @Value("${request.url}")
    public String requestUrl;

    /**
     * Confirmation URL.
     */
    private static final String CONFIRM = "/confirm/";

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

    private JavaMailSender mailSender;
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    public void setJavaMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    /**
     * Constructs partly hidden user password,
     * revealing two last characters of the actual password
     * and replacing other characters with '*' symbol.
     * @param user user who's password is manipulated.
     * @return sting same length as a password, revealing
     * only two last characters of it.
     */
    private String getStarsPassword(final User user) {
        char[] array = user.getEmail().toCharArray();
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
     * @param user user for whom unique conformation link is being created.
     * @param request user's registration request.
     * @return conformation link which contains user specific data.
     */
    private String getConfirmUrl(final User user) {
        return requestUrl + CONFIRM +
                Base64Utils.encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());
    }

    /**
     * Constructs conformation email text.
     * Populates email body model and converts
     * template with model into String.
     * @param user user who's information is being processed.
     * @return String containing email template with populated model, or
     * <code>null</code> if exception occurred.
     */
    private String getEmailText(final User user) {
        try {
            Map<String, Object> model = new HashMap<>();

            model.put("email", user.getEmail());
            model.put("password", getStarsPassword(user));
            model.put("confirmUrl", getConfirmUrl(user));

            LOG.debug("Constructing model: {email={}, password={}, confirmUrl={}}",
                    user.getEmail(), getStarsPassword(user), getConfirmUrl(user));

            final String text = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freeMarkerConfigurer
                            .createConfiguration()
                            .getTemplate(templateName), model);
            return text;
        } catch (Exception e) {
            LOG.error("Error while composing email text: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Constructs email message by setting sender email address,
     * message subject, recipient email address, using <code>getEmailText</code>
     * method fetches email text body and adjusts needed resources.
     * @param user user who'm email is being created.
     * @return fully composed conformation email message.
     */
    public MimeMessage createEmail(final User user) {
        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            final String emailText = getEmailText(user);

            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setTo(user.getEmail());
            if (emailText != null) {
                helper.setText(emailText, true);
            } else {
                throw new NullArgumentException("EmailText cannot be null.");
            }

            LOG.debug("Constructing email: " +
                            "{Sender email address={}, Message subject={}, Recipient email address={}}",
                    sender, subject, user.getEmail());

            // needs to be configured after {setText} method.
            ClassPathResource res = new ClassPathResource(resources);
            // can be referenced in html using {src="cid:mail-logo"}.
            helper.addInline("mail-logo", res);

            return message;
        } catch (Exception e) {
            LOG.error("Error while creating email: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends the email created by <code>createEmail</code> method.
     * @param user who'm email is sent.
     */
    public void sendEmail(final User user) {
        try {
            this.mailSender.send(createEmail(user));
        } catch (MailAuthenticationException e) {
            LOG.error("Authentication failure: {}", e.getMessage());
        } catch (MailSendException e) {
            LOG.error("Error while sending the message: {}", e.getMessage());
        }
    }
}
