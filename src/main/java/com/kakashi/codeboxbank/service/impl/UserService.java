package com.kakashi.codeboxbank.service.impl;

import com.kakashi.codeboxbank.dto.BankResponse;
import com.kakashi.codeboxbank.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
