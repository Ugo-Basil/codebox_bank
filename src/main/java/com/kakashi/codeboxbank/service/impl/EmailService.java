package com.kakashi.codeboxbank.service.impl;

import com.kakashi.codeboxbank.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
