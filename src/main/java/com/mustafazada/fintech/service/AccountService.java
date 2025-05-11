package com.mustafazada.fintech.service;

import com.mustafazada.fintech.dto.response.AccountResponseListDTO;
import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import com.mustafazada.fintech.dto.response.Status;
import com.mustafazada.fintech.dto.response.StatusCode;
import com.mustafazada.fintech.entity.TechUser;
import com.mustafazada.fintech.repositories.UserRepository;
import com.mustafazada.fintech.util.CurrentUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    UserRepository userRepository;
    CurrentUser currentUser;

    public CommonResponseDTO<?> getAccountsForUser() {
        Optional<TechUser> user = userRepository
                .findByPin(currentUser.getCurrentUser().getUsername());
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Account(s) for user: "
                        + currentUser.getCurrentUser().getUsername()
                        + " was successfully fetched")
                .build()).data(AccountResponseListDTO.entityToDTO(user.get().getAccountList())).build();
    }
}
