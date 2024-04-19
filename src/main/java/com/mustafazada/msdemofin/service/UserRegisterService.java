package com.mustafazada.msdemofin.service;

import com.mustafazada.msdemofin.dto.request.AccountRequestDTO;
import com.mustafazada.msdemofin.dto.request.UserRequestDTO;
import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.dto.response.UserResponseDTO;
import com.mustafazada.msdemofin.entity.Account;
import com.mustafazada.msdemofin.entity.TechUser;
import com.mustafazada.msdemofin.exception.AccountAlreadyExistException;
import com.mustafazada.msdemofin.exception.UserAlreadyExistException;
import com.mustafazada.msdemofin.repository.AccountRepository;
import com.mustafazada.msdemofin.repository.UserRepository;
import com.mustafazada.msdemofin.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisterService {
    UserRepository userRepository;
    AccountRepository accountRepository;
    DTOCheckUtil dtoCheckUtil;

    public CommonResponseDTO<?> saveUser(UserRequestDTO userRequestDTO) {
        dtoCheckUtil.isValid(userRequestDTO);
        checkUserExist(userRequestDTO);
        checkAccountExist(userRequestDTO);
        TechUser user = createUserEntity(userRequestDTO);
        TechUser savedUser = userRepository.save(user);

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Registration has been successfully completed.")
                .build()).data(UserResponseDTO.entityResponse(savedUser)).build();
    }

    private TechUser createUserEntity(UserRequestDTO userRequestDTO) {
        TechUser user = TechUser.builder()
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .password(userRequestDTO.getPassword())
                .pin(userRequestDTO.getPin())
                .role("USER_ROLE")
                .build();
        user.addAccountToUser(userRequestDTO.getAccountRequestDTOList());
        return user;
    }

    private void checkUserExist(UserRequestDTO userRequestDTO) {
        Optional<TechUser> userRepositoryByPin = userRepository.findByPin(userRequestDTO.getPin());
        if (userRepositoryByPin.isPresent()) {
            throw UserAlreadyExistException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.ALREADY_EXIST)
                    .message("User with pin: " + userRequestDTO.getPin()
                            + " is exist. Please enter a pin that has not been register before")
                    .build()).build()).build();
        }
    }

    private void checkAccountExist(UserRequestDTO userRequestDTO) {
        List<Integer> accountNoList = accountRepository.findAll()
                .stream().map(Account::getAccountNo)
                .collect(Collectors.toList());

        userRequestDTO.getAccountRequestDTOList()
                .stream()
                .map(AccountRequestDTO::getAccountNo)
                .filter(accountNoList::contains)
                .findFirst()
                .ifPresent(duplicateAccountNo -> {
                    throw AccountAlreadyExistException.builder()
                            .responseDTO(CommonResponseDTO.builder()
                                    .status(Status.builder()
                                            .statusCode(StatusCode.ALREADY_EXIST)
                                            .message("Duplicate account number found: " + duplicateAccountNo)
                                            .build()).build()).build();
                });
    }
}
