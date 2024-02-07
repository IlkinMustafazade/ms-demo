package com.mustafazada.tech_app_3.controller;

import com.mustafazada.tech_app_3.dto.request.AccountToAccountDTO;
import com.mustafazada.tech_app_3.service.AccountService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/account")
    public ResponseEntity<?> getAccount() {
        return new ResponseEntity<>(accountService.getAccount(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> amountTransfer(@RequestBody AccountToAccountDTO accountToAccountDTO) {
        return new ResponseEntity<>(accountService.account2account(accountToAccountDTO), HttpStatus.OK);
    }
}
