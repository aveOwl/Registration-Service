package com.registration.service;

import com.registration.model.User;

import javax.servlet.http.HttpServletRequest;

public interface ConfirmService {

    /**
     * Provides user conformation.
     * @param user user to be confirmed.
     * @param request conformation request.
     */
    void confirm(User user, HttpServletRequest request);
}
