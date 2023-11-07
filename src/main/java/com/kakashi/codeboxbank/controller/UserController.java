package com.kakashi.codeboxbank.controller;

import com.kakashi.codeboxbank.dto.*;
import com.kakashi.codeboxbank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;



    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){

        return userService.createAccount(userRequest);
    }

    @GetMapping("/balance-enquiry")
    public  BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }


    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitReq request){
        return userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitReq request){
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse transferAmount(@RequestBody TransferReq request){
        return userService.transferAmount(request);
    }
}
