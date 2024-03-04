package com.BankingApplication.controller;

import com.BankingApplication.entity.Account;
import com.BankingApplication.entity.Transaction;
import com.BankingApplication.payload.AccountDto;
import com.BankingApplication.payload.DepositRequest;
import com.BankingApplication.payload.TransactionDto;
import com.BankingApplication.payload.WithdrawalRequest;
import com.BankingApplication.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    //http://localhost:8080/api/account
    @PostMapping("/newaccount")
    public ResponseEntity<?> CreateAccount(@Valid @RequestBody  AccountDto accountDto){
        AccountDto dto = accountService.CreateAccount(accountDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable long id){
        accountService.delectAccount(id);
        return new ResponseEntity<>("Account is Successfully deleted ::"+id,HttpStatus.OK);
    }
    @PutMapping("/updateAccount")
    public ResponseEntity<?> UpdateAccount(@RequestBody AccountDto accountDto,@RequestParam String accountNumber){
        AccountDto dto = accountService.UpdateAccount(accountDto, accountNumber);
        return new ResponseEntity<>("Account Successfully Updated.."+dto,HttpStatus.OK);

    }
    @GetMapping("/SearchAccount")
    public ResponseEntity<?> FindByAccountNumber(@RequestParam String accountNumber){
        AccountDto dto = accountService.FindByAccountNumber(accountNumber);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @GetMapping
    public List<?> getAllAccount(){
        List<Account> dto = accountService.getAllAc();
        return dto;
    }
    //http://localhost:8080/api/121223/deposits
    @PostMapping("/{accountNumber}/deposits")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @Valid @RequestBody DepositRequest depositRequest) {

        Account account = accountService.deposit(accountNumber, depositRequest.getAmount());

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @PostMapping("/{accountNumber}/withdrawals")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawalRequest withdrawalRequest) {

        Account account = accountService.withdraw(accountNumber, withdrawalRequest.getAmount());

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @GetMapping("/SearchTransaction")
    public ResponseEntity<?> getTransactionByAccountNumber(@RequestParam String accountNumber){
        List<Transaction> dto = accountService.getTransactionByAccountNumber(accountNumber);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

}
