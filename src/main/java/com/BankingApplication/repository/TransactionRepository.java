package com.BankingApplication.repository;

import com.BankingApplication.entity.Account;
import com.BankingApplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

   List<Transaction> getTransactionByAccountNumber(String accountNumber);
}
