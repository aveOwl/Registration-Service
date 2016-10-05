package com.registration.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MimeMessageHelperProviderImpl.class)
public class MimeMessageHelperProviderImplTest {
    private static final String ENCODING = "UTF-8";

    @Autowired
    private MimeMessageHelperProvider helperProvider;

    @MockBean
    private MimeMessage message;

    @Test
    public void shouldReturnConfiguredHelper() throws Exception {
        final String encoding = helperProvider.getMimeMessageHelper(message).getEncoding();
        final MimeMessage mimeMessage = helperProvider.getMimeMessageHelper(message).getMimeMessage();
        final boolean multipart = helperProvider.getMimeMessageHelper(message).isMultipart();

        assertThat("utf-8 encoding", encoding, is(ENCODING));
        assertThat("mimeMessage present", mimeMessage, is(message));
        assertThat("support multipart", multipart, is(true));
    }
}