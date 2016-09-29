package com.registration.util;

import com.registration.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.Map;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmailBuilder.class)
public class EmailBuilderTest {
    @Autowired
    private EmailBuilder emailBuilder;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private FreeMarkerConfigurer configurer;

    @MockBean
    private MimeMessage message;

    @MockBean
    private MimeMessageHelper helper;

    @MockBean
    private Configuration configuration;

    @MockBean
    private Template template;

    @MockBean
    private MimeMessageHelperProvider helperProvider;

    @Mock
    private Map<String, String> map;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;

    @Before
    public void setUp() throws Exception {
        given(mailSender.createMimeMessage())
                .willReturn(message);

        given(helperProvider.getMimeMessageHelper(message))
                .willReturn(helper);

        given(configurer.createConfiguration())
                .willReturn(configuration);

        given(configuration.getTemplate(anyString()))
                .willReturn(template);

        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void shouldBuildEmailMessage() throws Exception {
        // given

        // when
        emailBuilder.createEmail(user);

        // then
        verify(mailSender, only()).createMimeMessage();
        verify(helperProvider, only()).getMimeMessageHelper(message);

        verify(helper, atLeastOnce()).setFrom(anyString());
        verify(helper, atLeastOnce()).setTo(user.getEmail());
        verify(helper, atLeastOnce()).setSubject(anyString());
        verify(helper, atLeastOnce()).setText(anyString(), Matchers.eq(true));
        verify(helper, atLeastOnce()).addInline(anyString(), any(Resource.class));
    }

    @Test
    public void shouldThrowExceptionOnNullMessage() throws Exception {
        // given
        given(mailSender.createMimeMessage())
                .willReturn(null);

        thrown.expect(MailPreparationException.class);
        thrown.expectMessage("Failed to get message helper, message helper was null.");

        // when
        emailBuilder.createEmail(user);

        // then
        verify(mailSender, only()).createMimeMessage();
        verify(helperProvider, only()).getMimeMessageHelper(message);

        verify(helper, never()).setFrom(anyString());
    }
}