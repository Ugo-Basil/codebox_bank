package com.kakashi.codeboxbank.service.impl;

import com.kakashi.codeboxbank.dto.AccountInfo;
import com.kakashi.codeboxbank.dto.BankResponse;
import com.kakashi.codeboxbank.dto.UserRequest;
import com.kakashi.codeboxbank.entity.User;
import com.kakashi.codeboxbank.repository.UserRepository;
import com.kakashi.codeboxbank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UserServiceImpl implements UserService {
@Autowired
UserRepository userRepository;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        /*
         * create a new user
         * check if user exist
         * *
         */


        if(userRepository.existsByEmail(userRequest.getEmail())){
             return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null)
                    .build();
        }


        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();


        User savedUser = userRepository.save(newUser);

        return  BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName()
                        + " " + savedUser.getOtherName())
                        .build())
                .build();
    }
}
