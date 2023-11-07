package com.kakashi.codeboxbank.dto;

import com.kakashi.codeboxbank.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDebitReq {
    private  String accountNumber;
    private BigDecimal amount;

}
