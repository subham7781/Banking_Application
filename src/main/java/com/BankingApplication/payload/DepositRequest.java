package com.BankingApplication.payload;


import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull
    private BigDecimal amount;

}

