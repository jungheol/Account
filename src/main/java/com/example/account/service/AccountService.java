package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static com.example.account.type.AccountStatus.IN_USE;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 사용자가 있는지 확인 조회
     * 계좌 번호 생성
     * 계좌 저장, 정보 토스
     */
    @Transactional
    public Account createAccount(Long userId, Long initialBalance) {
        // 사용자 정보 찾아오기. 없다면 에러 코드 표시
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

        // db에 계좌가 있다면 계좌번호에 +1 해서 새로운 계좌 생성, 없다면 10000000000 으로 계좌 생성
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                  .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                  .orElse("1000000000");

        return accountRepository.save(
                Account.builder()
                      .accountUser(accountUser)
                      .accountStatus(IN_USE)
                      .accountNumber(newAccountNumber)
                      .balance(initialBalance)
                      .registeredAt(LocalDateTime.now())
                      .build()
        );
    }

    @Transactional
    public Account getAccount(Long id) {
        if(id < 0){
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }
}
