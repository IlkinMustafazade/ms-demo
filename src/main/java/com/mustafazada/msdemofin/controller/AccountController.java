package com.mustafazada.msdemofin.controller;

import com.mustafazada.msdemofin.dto.request.AccountToAccountRequestDTO;
import com.mustafazada.msdemofin.service.AccountService;
import com.mustafazada.msdemofin.service.TransferMoneyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;
    TransferMoneyService transferMoneyService;

    @GetMapping("/account")
    public ResponseEntity<?> getAccounts() {
        return new ResponseEntity<>(accountService.getAccountsForUser(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> amountTransfer(@RequestBody AccountToAccountRequestDTO accountToAccountRequestDTO) {
        return new ResponseEntity<>(transferMoneyService.account2account(accountToAccountRequestDTO), HttpStatus.OK);
    }
}
