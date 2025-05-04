package com.mustafazada.fintech.dto.response;

import com.mustafazada.fintech.dto.request.AccountRequestDTO;
import com.mustafazada.fintech.entity.TechUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.LifecycleState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    long id;
    String name;
    String surname;
    String password;
    String pin;
    String role;
    List<AccountRequestDTO> accountRequestDTOList;

    public static UserResponseDTO entityResponse(TechUser user) {
        List<AccountRequestDTO> accountList = new ArrayList<>();
        user.getAccountList().forEach(account -> accountList.add(AccountRequestDTO.builder()
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .accountNo(account.getAccountNo()).build()));

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .password(user.getPassword())
                .pin(user.getPin())
                .role(user.getRole())
                .accountRequestDTOList(accountList).build();
    }

}
