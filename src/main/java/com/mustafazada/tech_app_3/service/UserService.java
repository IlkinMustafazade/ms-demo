package com.mustafazada.tech_app_3.service;

import com.mustafazada.tech_app_3.dto.request.AuthenticationRequestDTO;
import com.mustafazada.tech_app_3.dto.request.UserRequestDTO;
import com.mustafazada.tech_app_3.dto.response.CommonResponseDTO;
import com.mustafazada.tech_app_3.dto.response.Status;
import com.mustafazada.tech_app_3.dto.response.StatusCode;
import com.mustafazada.tech_app_3.dto.response.UserResponseDTO;
import com.mustafazada.tech_app_3.entity.TechUser;
import com.mustafazada.tech_app_3.exception.NoSuchUserExist;
import com.mustafazada.tech_app_3.exception.UserAlreadyExist;
import com.mustafazada.tech_app_3.repository.UserRepository;
import com.mustafazada.tech_app_3.util.DTOUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    @Autowired
    DTOUtil dtoUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public CommonResponseDTO<?> saveUser(UserRequestDTO userRequestDTO) {
        dtoUtil.isValid(userRequestDTO);
        if (userRepository.findByPin(userRequestDTO.getPin()).isPresent()) {
            throw UserAlreadyExist.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.USER_EXIST)
                    .message("User with this pin: " + userRequestDTO.getPin() +
                            " is exist. Please Enter a pin that has not been registered before")
                    .build()).build()).build();
        }
        TechUser user = TechUser.builder()
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .pin(userRequestDTO.getPin())
                .role("ROLE_USER")
                .build();
        user.addAccountToUser(userRequestDTO.getAccountRequestDTOList());
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("You have successfully registered")
                .build()).data(UserResponseDTO.entityResponse(userRepository.save(user))).build();
    }

    public CommonResponseDTO<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        dtoUtil.isValid(authenticationRequestDTO);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getPin(),
                    authenticationRequestDTO.getPassword()
            ));
        } catch (Exception e) {
            throw NoSuchUserExist.builder().commonResponseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.USER_NOT_EXIST)
                    .message("pin: " + authenticationRequestDTO.getPin() + " or password: "
                            + authenticationRequestDTO.getPassword() + " is wrong")
                    .build()).build()).build();
        }

        return CommonResponseDTO.builder().data(authenticationRequestDTO)
                .status(Status.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .message("Welcome to our FIN-TECH Application")
                        .build()).build();
    }
}
