package com.kakashi.codeboxbank.service.impl;

import com.kakashi.codeboxbank.dto.*;
import com.kakashi.codeboxbank.entity.User;
import com.kakashi.codeboxbank.repository.UserRepository;
import com.kakashi.codeboxbank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

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

        //Send email alert
        EmailDetails emailDetails;
        emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations! Your Account Has Been Successfully Created." +
                        "\nYour Account Details: " +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " "
                        + savedUser.getOtherName() + "\n"
                        + "Account Number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
         boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

         if(!isAccountExist){
             return BankResponse.builder()
                     .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                     .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                     .accountInfo(null)
                     .build();
         }

         User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
         return  BankResponse.builder()
                 .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                 .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                 .accountInfo(AccountInfo.builder()
                         .accountBalance(foundUser.getAccountBalance())
                         .accountNumber(foundUser.getAccountNumber())
                         .accountName(foundUser.getFirstName() + " " + foundUser.getLastName()
                                 + " " + foundUser.getOtherName())
                         .build())
                    .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isNameExist = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isNameExist){
            return AccountUtils.ACCOUNT_NOT_FOUND_CODE + " " + AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;

        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();

    }

    @Override
    public BankResponse creditAccount(CreditDebitReq request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }


        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
     userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        return  BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName()
                                + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitReq request) {
       boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

       if(!isAccountExist){
              return BankResponse.builder()
                     .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                     .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                     .accountInfo(null)
                     .build();
       }

       User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

       BigInteger availableBalance =  userToDebit.getAccountBalance().toBigInteger();
       BigInteger amountToDebit = request.getAmount().toBigInteger();

       if(availableBalance.intValue() < amountToDebit.intValue()){
           return BankResponse.builder()
                   .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                   .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                   .accountInfo(null)
                   .build();
       }

       else {
           userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
           userRepository.save(userToDebit);

           return  BankResponse.builder()
                     .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                     .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                     .accountInfo(AccountInfo.builder()
                             .accountNumber(request.getAccountNumber())
                             .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName()
                                      + " " + userToDebit.getOtherName())
                             .accountBalance(userToDebit.getAccountBalance())

                             .build())
                     .build();
       }

    }

    @Override
    public BankResponse transferAmount(TransferReq request) {

        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        User userToCredit = userRepository.findByAccountNumber(request.getDestinationAccountNumber());

        BigInteger availableBalance =  userToDebit.getAccountBalance().toBigInteger();

        BigInteger amountToDebit = request.getAmount().toBigInteger();

        if(availableBalance.intValue() < amountToDebit.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
            userRepository.save(userToDebit);
            userRepository.save(userToCredit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("Debit Alert")
                    .messageBody("Your account has been debited with " + request.getAmount()  +
                            " to " + userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " +
                            " your current balance is "  + userToDebit.getAccountBalance())
                    .build();

            EmailDetails creditAlert = EmailDetails.builder()
                    .recipient(userToCredit.getEmail())
                    .subject("Credit Alert")
                    .messageBody("Your account has been credited with " + request.getAmount()  +
                            " from " + userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " +
                            " your current balance is "  + userToCredit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);
            emailService.sendEmailAlert(creditAlert);

            return  BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName()
                                    + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }


    }


}
