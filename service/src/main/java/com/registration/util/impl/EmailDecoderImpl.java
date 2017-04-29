package com.registration.util.impl;

import com.registration.util.EmailDecoder;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.persistence.NoResultException;

@Service
public class EmailDecoderImpl implements EmailDecoder {

    /**
     * Decodes given confirmation code into array containing user email and password.
     * Retrieves user's email from the array and returns it.
     *
     * @param code confirmation code with encoded user data.
     * @return user email.
     * @throws NoResultException if confirmation code is invalid.
     */
    @Override
    @SneakyThrows
    public String decode(String code) {
        val decodedData = Base64Utils.decodeFromString(code);
        val data = new String(decodedData).split(":"); // {'email', 'password'}
        return data[0];
    }
}
