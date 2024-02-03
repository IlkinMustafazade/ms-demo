package com.mustafazada.tech_app_3.service;

import com.mustafazada.tech_app_3.dto.response.AccountResponseDTOList;
import com.mustafazada.tech_app_3.dto.response.CommonResponseDTO;
import com.mustafazada.tech_app_3.dto.response.Status;
import com.mustafazada.tech_app_3.dto.response.StatusCode;
import com.mustafazada.tech_app_3.entity.TechUser;
import com.mustafazada.tech_app_3.repository.UserRepository;
import com.mustafazada.tech_app_3.util.CurrentUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {

    @Autowired
    CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    public CommonResponseDTO<?> getAccount() {
        Optional<TechUser> user = userRepository.findByPin(currentUser.getCurrentUser().getUsername());

        return CommonResponseDTO.builder()
                .status(Status.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .message("Accounts successfully fetched")
                        .build())
                .data(AccountResponseDTOList.entityToDTO(user.get().getAccountList())).build();

    }
}
