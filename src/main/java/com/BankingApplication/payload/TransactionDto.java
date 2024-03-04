package com.BankingApplication.payload;

import com.BankingApplication.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private LocalDateTime localDateTime;
    private BigDecimal amount;
    private String Description;
    private String accountNumber;
    private Account account;
}
