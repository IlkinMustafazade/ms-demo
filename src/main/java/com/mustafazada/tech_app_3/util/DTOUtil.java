package com.mustafazada.tech_app_3.util;


import com.mustafazada.tech_app_3.dto.request.AccountToAccountDTO;
import com.mustafazada.tech_app_3.dto.request.AuthenticationRequestDTO;
import com.mustafazada.tech_app_3.dto.request.UserRequestDTO;
import com.mustafazada.tech_app_3.dto.response.CommonResponseDTO;
import com.mustafazada.tech_app_3.dto.response.Status;
import com.mustafazada.tech_app_3.dto.response.StatusCode;
import com.mustafazada.tech_app_3.exception.InvalidDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTOUtil {
    @Autowired
    Logger logger;

    public void isValid(UserRequestDTO userRequestDTO) {
        logger.warn(userRequestDTO.toString());
        checkDTOInput(userRequestDTO.getName());
        checkDTOInput(userRequestDTO.getSurname());
        checkDTOInput(userRequestDTO.getPassword());
        checkDTOInput(userRequestDTO.getPin());
        checkDTOInput(userRequestDTO.getAccountRequestDTOList());
    }

    public void isValid(AuthenticationRequestDTO authenticationRequestDTO) {
        logger.warn(authenticationRequestDTO.toString());
        checkDTOInput(authenticationRequestDTO.getPin());
        checkDTOInput(authenticationRequestDTO.getPassword());
    }

    public void isValid(AccountToAccountDTO accountToAccountDTO) {
        logger.warn(accountToAccountDTO.toString());
        checkDTOInput(accountToAccountDTO.getDebitAccount());
        checkDTOInput(accountToAccountDTO.getCreditAccount());
        checkDTOInput(accountToAccountDTO.getAmount());
    }

    private <T> void checkDTOInput(T t) {
        if (Objects.isNull(t) || t.toString().isBlank()) {
            logger.error("Invalid Input");
            throw InvalidDTO.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.INVALID_DTO)
                    .message("Invalid DATA")
                    .build()).build()).build();
        }
    }
}
