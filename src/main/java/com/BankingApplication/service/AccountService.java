package com.BankingApplication.service;

import com.BankingApplication.entity.Account;
import com.BankingApplication.entity.Transaction;
import com.BankingApplication.payload.AccountDto;
import com.BankingApplication.payload.TransactionDto;
import com.BankingApplication.repository.AccountRepository;
import com.BankingApplication.repository.TransactionRepository;
import com.BankingApplication.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TransactionRepository transactionRepository;

    public AccountDto CreateAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountHolderName(accountDto.getAccountHolderName());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setEmail(accountDto.getEmail());
        account.setBalance(accountDto.getBalance());
        emailSender.EmailSenderToCustomer(accountDto.getEmail(), "Account Successfully Created..", "your Account No. is::" + accountDto.getAccountNumber());
        Account save = accountRepository.save(account);

        AccountDto dto = new AccountDto();
        dto.setId(save.getId());
        dto.setAccountHolderName(save.getAccountHolderName());
        dto.setAccountNumber(save.getAccountNumber());
        dto.setEmail(save.getEmail());
        dto.setBalance(save.getBalance());
        return dto;
    }
    public void delectAccount(long id) {
        accountRepository.deleteById(id);
    }
    public AccountDto UpdateAccount(AccountDto accountDto, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        account.setAccountHolderName(accountDto.getAccountHolderName());
        account.setEmail(accountDto.getEmail());
        emailSender.EmailSenderToCustomer(accountDto.getEmail(), "Update Account details", "Account Successfully Updated  " + accountDto.getAccountHolderName()+" email "+accountDto.getEmail());
        Account save = accountRepository.save(account);

        AccountDto dto = new AccountDto();
        dto.setId(save.getId());
        dto.setAccountHolderName(save.getAccountHolderName());
        dto.setAccountNumber(save.getAccountNumber());
        dto.setEmail(save.getEmail());
        dto.setBalance(save.getBalance());
        return dto;
    }
    public Account FindByAccountNumber(String accountNumber) {
        Account byAccountNumber = accountRepository.findByAccountNumber(accountNumber);

        AccountDto dto = new AccountDto();
        dto.setId(byAccountNumber.getId());
        dto.setAccountHolderName(byAccountNumber.getAccountHolderName());
        dto.setAccountNumber(byAccountNumber.getAccountNumber());
        dto.setBalance(byAccountNumber.getBalance());
        dto.setEmail(byAccountNumber.getEmail());
        return byAccountNumber;
    }

    public List<Account> getAllAc() {
        List<Account> all = accountRepository.findAll();
        return all;
    }
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            return null;
        }

        // Update account balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        emailSender.EmailSenderToCustomer(account.getEmail(), "State Bank of India", "Your Account XXXX455 has been CREDITED with an amount of ₹" + amount);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDescription("Deposit");
        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setAccountNumber(accountNumber);
        transactionRepository.save(transaction);
        return account;
    }
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            return null;
        }
        // Check if sufficient balance is available
        if (account.getBalance().compareTo(amount) < 0) {
            // You may handle insufficient balance scenario here
            emailSender.EmailSenderToCustomer(account.getEmail(), "State Bank of India. ", "insufficient balance in your Account Number. " + account.getAccountNumber() + "  Balance ₹" + account.getBalance());
            return null;
        }
        // Update account balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        emailSender.EmailSenderToCustomer(account.getEmail(), "State Bank Of India", "Your Account XXXX455 has been DEBITED with an amount of ₹" + amount);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDescription("Withdraw");
        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setAccountNumber(accountNumber);
        transactionRepository.save(transaction);
        return account;
    }
    public List<Transaction> getTransactionByAccountNumber(String accountNumber) {
        List<Transaction> transaction = transactionRepository.getTransactionByAccountNumber(accountNumber);
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionDto.getId());
        transactionDto.setAccount(transactionDto.getAccount());
        transactionDto.setAccountNumber(transactionDto.getAccountNumber());
        transactionDto.setAmount(transactionDto.getAmount());
        transactionDto.setLocalDateTime(transactionDto.getLocalDateTime());
        transactionDto.setDescription(transactionDto.getDescription());
        return transaction;
    }
}
