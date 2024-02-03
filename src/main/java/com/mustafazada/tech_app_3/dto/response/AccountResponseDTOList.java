package com.mustafazada.tech_app_3.dto.response;

import com.mustafazada.tech_app_3.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDTOList implements Serializable {
    private static final long serialVersionUID = 1L;

    List<AccountResponseDTO> accountResponseDTOList;

    public static AccountResponseDTOList entityToDTO(List<Account> accountList) {
        List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
        accountList.forEach(account -> accountResponseDTOList.add(AccountResponseDTO.entityDTO(account)));
        return AccountResponseDTOList.builder().accountResponseDTOList(accountResponseDTOList).build();
    }
}
