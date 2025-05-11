package com.mustafazada.fintech.dto.response;

import com.mustafazada.fintech.entity.Account;
import com.mustafazada.fintech.entity.Currency;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    BigDecimal balance;
    Currency currency;
    Boolean isActive;
    Integer accountNo;

    public static AccountResponseDTO entityToDTO(Account account) {
        return AccountResponseDTO.builder()
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .accountNo(account.getAccountNo())
                .build();
    }

}
