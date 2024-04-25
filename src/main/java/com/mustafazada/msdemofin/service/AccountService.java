package com.mustafazada.msdemofin.service;

import com.mustafazada.msdemofin.dto.response.AccountResponseListDTO;
import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.entity.TechUser;
import com.mustafazada.msdemofin.repository.UserRepository;
import com.mustafazada.msdemofin.util.CurrentUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    CurrentUser currentUser;
    UserRepository userRepository;

    public CommonResponseDTO<?> getAccountsForUser() {
        Optional<TechUser> userRepositoryByPin = userRepository
                .findByPin(currentUser.getCurrentUser().getUsername());

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Account(s) for user: "
                        + currentUser.getCurrentUser().getUsername()
                        + " was successfully fetched")
                .build()).data(AccountResponseListDTO.entityToDTO(userRepositoryByPin.get().getAccountList())).build();
    }

}
