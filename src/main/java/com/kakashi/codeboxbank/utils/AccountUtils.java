package com.kakashi.codeboxbank.utils;

import java.time.Year;

public class AccountUtils {

    public  static  final  String ACCOUNT_EXISTS_CODE = "001";
    public  static  final  String ACCOUNT_EXISTS_MESSAGE = "This user has an account already";

    public  static  final  String ACCOUNT_CREATED_CODE = "002";
    public  static  final  String ACCOUNT_CREATED_MESSAGE = "Account created successfully";

    public  static  final  String ACCOUNT_NOT_FOUND_CODE = "003";

    public  static  final  String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";


    public  static  final  String ACCOUNT_FOUND_CODE = "004";

    public  static  final  String ACCOUNT_FOUND_MESSAGE = "Account found";

    public  static  final  String ACCOUNT_CREDITED_SUCCESS = "005";

    public  static  final  String ACCOUNT_CREDITED_MESSAGE = "Account credited successfully";

    public  static  final  String INSUFFICIENT_BALANCE_CODE = "006";

    public  static  final  String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public  static  final  String ACCOUNT_DEBITED_SUCCESS = "007";

    public  static  final  String ACCOUNT_DEBITED_MESSAGE = "Account debited successfully";

    public  static  String generateAccountNumber(){
//       Generate an account number, begin with the year plus randomSixDigits

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // Generate a random number between 100000 and 999999

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert the year and randomNumber to string and concatenate with the random number

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return  accountNumber.append(year).append(randomNumber).toString();
    }
}
