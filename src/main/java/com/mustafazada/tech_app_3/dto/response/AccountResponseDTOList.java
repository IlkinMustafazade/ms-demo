package com.mustafazada.tech_app_3.dto.response;

import com.mustafazada.tech_app_3.entity.Account;
import com.mustafazada.tech_app_3.exception.NoActiveAccount;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDTOList implements Serializable {
    private static final long serialVersionUID = 1L;

    List<AccountResponseDTO> accountResponseDTOList;

    public static AccountResponseDTOList entityToDTO(List<Account> accountList) {
        accountList = accountList.stream().filter(Account::getIsActive).collect(Collectors.toList());

        if (!accountList.isEmpty()) {
            List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
            accountList.forEach(account -> accountResponseDTOList.add(AccountResponseDTO.entityDTO(account)));
            return AccountResponseDTOList.builder().accountResponseDTOList(accountResponseDTOList).build();
        } else {
            throw NoActiveAccount.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.NOT_ACTIVE_ACCOUNT)
                    .message("there is no active accounts")
                    .build()).build()).build();
        }
    }
}
