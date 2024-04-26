package com.mustafazada.msdemofin.util;

import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.exception.EqualAccountException;
import com.mustafazada.msdemofin.exception.InvalidAmountException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountCheckUtil {

    public void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw InvalidAmountException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.INVALID_AMOUNT)
                    .message("Amount must be greater than 0")
                    .build()).build()).build();
        }
    }

    public void checkEqualityOfAccounts(Integer debitAccountNumber, Integer creditAccountNumber) {
        if (debitAccountNumber.equals(creditAccountNumber)) {
            throw EqualAccountException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.EQUAL_ACCOUNT)
                    .message("Debit account: " + debitAccountNumber + " and " +
                            "Credit Account: " + creditAccountNumber + " are equal")
                    .build()).build()).build();
        }
    }
}


