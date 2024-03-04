package com.BankingApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TransactionId;

    private LocalDateTime localDateTime;
    private BigDecimal amount;
    private String Description;
    private String accountNumber;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


}

