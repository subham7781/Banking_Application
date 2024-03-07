package com.BankingApplication.payload;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    @NotBlank(message = "Account holder name cannot be null or empty.")
    private String accountHolderName;
    @NotBlank(message = "Account number cannot be null, empty, or contain whitespace.")
    @Column(unique = true)
    @Pattern(regexp = "^\\d{10}$", message = "Account number must be a 10-digit number.")
    private String accountNumber;
    @Email
    @Column(name = "email",length = 255)
    private String email;
    @NotNull(message = "Balance cannot be null.")
    @DecimalMin(value = "0.01", message = "Balance must be positive.")
    private BigDecimal balance;

}
