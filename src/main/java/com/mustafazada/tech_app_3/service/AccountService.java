package com.mustafazada.tech_app_3.service;

import com.mustafazada.tech_app_3.dto.request.AccountToAccountDTO;
import com.mustafazada.tech_app_3.dto.response.*;
import com.mustafazada.tech_app_3.entity.Account;
import com.mustafazada.tech_app_3.entity.TechUser;
import com.mustafazada.tech_app_3.exception.InvalidDTO;
import com.mustafazada.tech_app_3.repository.AccountRepository;
import com.mustafazada.tech_app_3.repository.UserRepository;
import com.mustafazada.tech_app_3.util.CurrentUser;
import com.mustafazada.tech_app_3.util.DTOUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {

    @Autowired
    CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DTOUtil dtoUtil;

    @Autowired
    AccountRepository accountRepository;

    public CommonResponseDTO<?> getAccount() {
        Optional<TechUser> user = userRepository.findByPin(currentUser.getCurrentUser().getUsername());

        return CommonResponseDTO.builder()
                .status(Status.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .message("Accounts successfully fetched")
                        .build())
                .data(AccountResponseDTOList.entityToDTO(user.get().getAccountList())).build();

    }

    @Transactional
    public CommonResponseDTO<?> account2account(AccountToAccountDTO accountToAccountDTO) {
        dtoUtil.isValid(accountToAccountDTO);
        if (accountToAccountDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw InvalidDTO.builder()
                    .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                            .statusCode(StatusCode.INVALID_DTO)
                            .message("Amount is not correct")
                            .build()).build()).build();
        } else if (accountToAccountDTO.getDebitAccount().equals(accountToAccountDTO.getCreditAccount())) {
            throw InvalidDTO.builder()
                    .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                            .statusCode(StatusCode.INVALID_DTO)
                            .message("debit account: " + accountToAccountDTO.getDebitAccount() +
                                    " and credit account: " + accountToAccountDTO.getCreditAccount() +
                                    " are same")
                            .build()).build()).build();
        }

        Optional<Account> byDebitAccount = accountRepository.findByAccountNo(accountToAccountDTO.getDebitAccount());

        Account debitAccount;
        Account creditAccount;

        if (byDebitAccount.isPresent()) {
            debitAccount = byDebitAccount.get();
            if (!debitAccount.getIsActive()) {
                throw InvalidDTO.builder()
                        .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                                .statusCode(StatusCode.INVALID_DTO)
                                .message("Debit account is not Active")
                                .build()).build()).build();
            }
            if (debitAccount.getBalance().compareTo(accountToAccountDTO.getAmount()) <= 0) {
                throw InvalidDTO.builder()
                        .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                                .statusCode(StatusCode.INVALID_DTO)
                                .message("Balance is not enough")
                                .build()).build()).build();
            }
            Optional<Account> byCreditAccountNo = accountRepository.findByAccountNo(accountToAccountDTO.getCreditAccount());
            if (byCreditAccountNo.isPresent()) {
                creditAccount = byCreditAccountNo.get();
                if (!creditAccount.getIsActive()) {
                    throw InvalidDTO.builder()
                            .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                                    .statusCode(StatusCode.INVALID_DTO)
                                    .message("Credit account is not Active")
                                    .build()).build()).build();
                }
            } else {
                throw InvalidDTO.builder()
                        .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                                .statusCode(StatusCode.INVALID_DTO)
                                .message("Credit account is not present")
                                .build()).build()).build();
            }
        } else {
            throw InvalidDTO.builder()
                    .responseDTO(CommonResponseDTO.builder().status(Status.builder()
                            .statusCode(StatusCode.INVALID_DTO)
                            .message("Debit account is not present")
                            .build()).build()).build();
        }

        debitAccount.setBalance(debitAccount.getBalance().subtract(accountToAccountDTO.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance().add(accountToAccountDTO.getAmount()));
        return CommonResponseDTO.builder()
                .status(Status.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .message("Transfer completed successfully").build())
                .data(AccountResponseDTO.builder()
                        .balance(debitAccount.getBalance())
                        .currency(debitAccount.getCurrency())
                        .isActive(debitAccount.getIsActive())
                        .accountNo(debitAccount.getAccountNo()).build()).build();
    }
}
