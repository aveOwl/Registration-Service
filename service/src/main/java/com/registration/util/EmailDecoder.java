package com.registration.util;

/**
 * The EmailDecoder interface defines how a confirmation
 * code should be translated into user email.
 *
 * @author Bohdan Bachkala
 */
public interface EmailDecoder {

    /**
     * Translates a single confirmation code into user email address.
     *
     * @param code confirmation code.
     * @return user email address.
     */
    String decode(String code);
}
