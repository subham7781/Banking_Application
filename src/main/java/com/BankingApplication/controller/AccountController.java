package com.BankingApplication.controller;

import com.BankingApplication.entity.Account;
import com.BankingApplication.entity.Transaction;
import com.BankingApplication.payload.AccountDto;
import com.BankingApplication.payload.DepositRequest;
import com.BankingApplication.payload.WithdrawalRequest;
import com.BankingApplication.service.AccountService;
import com.BankingApplication.util.EmailSender;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class AccountController {
    private Map<String, String> otpStore = new HashMap<>(); // Store OTPs
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private AccountService accountService;

    //http://localhost:8080/api/account
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PostMapping("/newaccount")
    public ResponseEntity<?> CreateAccount(@Valid @RequestBody  AccountDto accountDto){
        AccountDto dto = accountService.CreateAccount(accountDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable long id){
        accountService.delectAccount(id);
        return new ResponseEntity<>("Account is Successfully deleted ::"+id,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/updateAccount")
    public ResponseEntity<?> UpdateAccount(@RequestBody AccountDto accountDto,@RequestParam String accountNumber){
        AccountDto dto = accountService.UpdateAccount(accountDto, accountNumber);
        return new ResponseEntity<>("Account Successfully Updated.."+dto,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    @GetMapping("/SearchAccount")
    public ResponseEntity<?> FindByAccountNumber(@RequestParam String accountNumber){
        Account account = accountService.FindByAccountNumber(accountNumber);
        return new ResponseEntity<>(account,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public List<?> getAllAccount(){
        List<Account> dto = accountService.getAllAc();
        return dto;
    }
    //http://localhost:8080/api/121223/deposits
    @PostMapping("/{accountNumber}/deposits")
    @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @Valid @RequestBody DepositRequest depositRequest) {

        Account account = accountService.deposit(accountNumber, depositRequest.getAmount());

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/withdrawals")
    public ResponseEntity<?> withdraw(
            @PathVariable String accountNumber) {

        // Generate and send OTP
        String otp = generateOtp();
        Account account = accountService.FindByAccountNumber(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Send OTP to the user
        emailSender.EmailSenderToCustomer(account.getEmail(), "State Bank of India. ", "Your OTP for withdrawal is: " + otp);

        // Store OTP for verification later
        otpStore.put(accountNumber, otp);

        return new ResponseEntity<>("Otp sent Successfully to your Email "+account.getEmail(), HttpStatus.OK);
    }
    public String generateOtp(){
        return String.format("%06d",new java.util.Random().nextInt(1000000));
    }
    // Verify OTP
    @PostMapping("/{accountNumber}/withdrawals/verify")
    public ResponseEntity<?> verifyOTP(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawalRequest withdrawalRequest,
            @RequestParam String otp) {

        // Retrieve stored OTP
        String storedOTP = otpStore.get(accountNumber);

        if (storedOTP == null || !storedOTP.equals(otp)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Unauthorized if OTP is incorrect
        }
        // If OTP is correct, proceed with withdrawal
        Account account = accountService.withdraw(accountNumber, withdrawalRequest.getAmount());
        return new ResponseEntity<>("Withdrawal Successful "+account, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/SearchTransaction")
    public ResponseEntity<?> getTransactionByAccountNumber(@RequestParam String accountNumber){
        List<Transaction> dto = accountService.getTransactionByAccountNumber(accountNumber);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

}
