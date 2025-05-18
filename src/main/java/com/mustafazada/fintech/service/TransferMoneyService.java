package com.mustafazada.fintech.service;

import com.mustafazada.fintech.dto.request.AccountToAccountRequestDTO;
import com.mustafazada.fintech.dto.response.AccountResponseDTO;
import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import com.mustafazada.fintech.dto.response.Status;
import com.mustafazada.fintech.dto.response.StatusCode;
import com.mustafazada.fintech.entity.Account;
import com.mustafazada.fintech.exception.*;
import com.mustafazada.fintech.repositories.AccountRepository;
import com.mustafazada.fintech.util.AccountCheckUtil;
import com.mustafazada.fintech.util.CurrentUser;
import com.mustafazada.fintech.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferMoneyService {
    AccountRepository accountRepository;
    AccountCheckUtil accountCheckUtil;
    CurrentUser currentUser;
    DTOCheckUtil dtoCheckUtil;

    @Transactional
    public CommonResponseDTO<?> account2account(AccountToAccountRequestDTO requestDTO) {
        dtoCheckUtil.isValid(requestDTO);
        currentUser.validateCurrentUserAndAccount(requestDTO.getDebitAccountNo());
        accountCheckUtil.checkAmount(requestDTO.getAmount());
        accountCheckUtil.checkEqualityAccounts(requestDTO.getDebitAccountNo(), requestDTO.getCreditAccountNo());
        Account debitAccount = getAccountByAccountNo(requestDTO.getDebitAccountNo(), "debit");
        checkAccountStatus(debitAccount, "debit");
        checkDebitAccountBalance(debitAccount.getBalance(), requestDTO.getAmount());

        Account creditAccount = getAccountByAccountNo(requestDTO.getCreditAccountNo(), "credit");
        checkAccountStatus(creditAccount, "credit");

        debitAccount.setBalance(debitAccount.getBalance().subtract(requestDTO.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance().add(requestDTO.getAmount()));

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Transfer completed successfully")
                .build()).data(AccountResponseDTO.builder()
                .accountNo(debitAccount.getAccountNo())
                .currency(debitAccount.getCurrency())
                .isActive(debitAccount.getIsActive())
                .balance(debitAccount.getBalance())
                .build()).build();
    }

    private Account getAccountByAccountNo(Integer accountNo, String accountType) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNo(accountNo);
        if (optionalAccount.isEmpty()) {
            throw NotAccountExistException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ACCOUNT_NOT_EXIST)
                            .message("The " + accountType + " account:" + accountNo
                                    + " is not exist")
                            .build()).build()).build();
        }
        return optionalAccount.get();
    }

    private void checkAccountStatus(Account account, String accountType) {
        if (!account.getIsActive()) {
            throw NotActiveAccountException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.NOT_ACTIVE_ACCOUNT)
                            .message("The " + accountType + " account:" + account.getAccountNo()
                                    + " is not active")
                            .build()).build()).build();
        }
    }

    private void checkDebitAccountBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) <= 0) {
            throw InsufficientBalanceException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INSUFFICIENT_BALANCE)
                            .message("Balance: " + balance + " is less than " + amount)
                            .build()).build()).build();
        }
    }
}
