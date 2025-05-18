package com.mustafazada.fintech.util;

import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import com.mustafazada.fintech.dto.response.Status;
import com.mustafazada.fintech.dto.response.StatusCode;
import com.mustafazada.fintech.exception.EqualsAccountException;
import com.mustafazada.fintech.exception.InvalidAmountException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountCheckUtil {
    public void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw InvalidAmountException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_AMOUNT)
                            .message("Amount must be greater than 0")
                            .build()).build()).build();
        }
    }

    public void checkEqualityAccounts(Integer debitAccount, Integer creditAccount) {
        if (debitAccount.equals(creditAccount)) {
            throw EqualsAccountException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.EQUALS_ACCOUNT)
                            .message("Debit account: " + debitAccount
                                    + " and credit account: "
                                    + creditAccount + " are the same")
                            .build()).build()).build();
        }
    }
}
