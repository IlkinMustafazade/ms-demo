package com.mustafazada.msdemofin.util;


import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.entity.Account;
import com.mustafazada.msdemofin.entity.TechUser;
import com.mustafazada.msdemofin.exception.InvalidTokenException;
import com.mustafazada.msdemofin.exception.NoTiedTokenException;
import com.mustafazada.msdemofin.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentUser {
    UserRepository userRepository;

    public UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public void validateCurrentUserAndAccount(Integer accountNo) {
        TechUser techUser = validateCurrentToken();
        List<Account> accountList = techUser.getAccountList();
        if (accountList.stream().noneMatch(account -> account.getAccountNo().equals(accountNo))) {
            throw NoTiedTokenException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.INVALID_TOKEN)
                    .message("The Token is not tied to this user")
                    .build()).build()).build();
        }
    }

    private TechUser validateCurrentToken() {
        Optional<TechUser> userRepositoryByPin = userRepository.findByPin(getCurrentUser().getUsername());
        if (userRepositoryByPin.isEmpty()) {
            throw InvalidTokenException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.INVALID_TOKEN)
                    .message("Token is not valid")
                    .build()).build()).build();
        }
        return userRepositoryByPin.get();
    }
}
