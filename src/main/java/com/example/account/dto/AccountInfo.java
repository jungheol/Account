package com.example.account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
    // Client 와 Controller 간 필요 정보
    private String accountNumber;
    private Long balance;
}
