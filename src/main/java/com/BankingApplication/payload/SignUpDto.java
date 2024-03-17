package com.BankingApplication.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String roleType;
}