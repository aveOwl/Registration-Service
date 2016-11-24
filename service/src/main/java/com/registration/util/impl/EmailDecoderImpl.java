package com.registration.util.impl;

import com.registration.util.EmailDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.persistence.NoResultException;

@Service
public class EmailDecoderImpl implements EmailDecoder {

    /**
     * Decodes given confirmation code into array containing user email and password.
     * Retrieves user's email from the array and returns it.
     *
     * @throws NoResultException if confirmation code is invalid.
     * @param code confirmation code with encoded user data.
     * @return user email.
     */
    @Override
    public String decode(String code) {
        try {
            byte[] decodedData = Base64Utils.decodeFromString(code);

            String[] data = new String(decodedData).split(":"); // {'email', 'password'}

            return data[0];
        } catch (IllegalArgumentException e) {
            throw new NoResultException("Invalid confirmation link.");
        }
    }
}
