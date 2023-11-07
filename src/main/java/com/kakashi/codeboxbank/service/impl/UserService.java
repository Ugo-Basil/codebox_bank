package com.kakashi.codeboxbank.service.impl;

import com.kakashi.codeboxbank.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitReq request);

    BankResponse debitAccount(CreditDebitReq request);

    BankResponse transferAmount(TransferReq request);
}
