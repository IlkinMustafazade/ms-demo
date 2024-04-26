package com.mustafazada.msdemofin.service;

import com.mustafazada.msdemofin.dto.request.AccountToAccountRequestDTO;
import com.mustafazada.msdemofin.dto.response.AccountResponseDTO;
import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.entity.Account;
import com.mustafazada.msdemofin.exception.*;
import com.mustafazada.msdemofin.repository.AccountRepository;
import com.mustafazada.msdemofin.util.AccountCheckUtil;
import com.mustafazada.msdemofin.util.CurrentUser;
import com.mustafazada.msdemofin.util.DTOCheckUtil;
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
    DTOCheckUtil dtoCheckUtil;
    CurrentUser currentUser;

    @Transactional
    public CommonResponseDTO<?> account2account(AccountToAccountRequestDTO requestDTO) {
        currentUser.validateCurrentUserAndAccount(requestDTO.getDebitAccountNumber());
        dtoCheckUtil.isValid(requestDTO);
        accountCheckUtil.checkAmount(requestDTO.getAmount());
        accountCheckUtil.checkEqualityOfAccounts(requestDTO.getDebitAccountNumber(), requestDTO.getCreditAccountNumber());
        Account debitAccount = getAccountByAccountNo(requestDTO.getDebitAccountNumber(), "Debit");
        validateAccountStatus(debitAccount, "Debit");
        validateDebitAccountBalance(debitAccount.getBalance(), requestDTO.getAmount());
        Account creditAccount = getAccountByAccountNo(requestDTO.getCreditAccountNumber(), "Credit");
        validateAccountStatus(creditAccount, "Credit");
        debitAccount.setBalance(debitAccount.getBalance().subtract(requestDTO.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance().add(requestDTO.getAmount()));

        return CommonResponseDTO.builder().status(Status.builder().statusCode(StatusCode.SUCCESS)
                .message("Transfer completed successfully")
                .build()).data(AccountResponseDTO.builder()
                .accountNo(debitAccount.getAccountNo())
                .currency(debitAccount.getCurrency())
                .isActive(debitAccount.getIsActive())
                .balance(debitAccount.getBalance())
                .build()).build();
    }


    private Account getAccountByAccountNo(Integer accountNo, String typeAccount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNo(accountNo);
        if (optionalAccount.isEmpty()) {
            throw NotAccountFoundException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.ACCOUNT_NOT_FOUND)
                    .message(typeAccount + " account: " + accountNo + " is not found").build()).build()).build();
        }
        return optionalAccount.get();
    }

    private void validateAccountStatus(Account account, String typeAccount) {
        if (!account.getIsActive()) {
            throw NotActiveAccountException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.NOT_ACTIVE_ACCOUNT)
                    .message(typeAccount + " account: " + account.getAccountNo() + " is not active").build()).build()).build();
        }
    }

    private void validateDebitAccountBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) <= 0) {
            throw InsufficientBalanceException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.INSUFFICIENT_BALANCE)
                    .message("Balance is not enough").build()).build()).build();
        }
    }
}
