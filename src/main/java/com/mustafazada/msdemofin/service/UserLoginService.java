package com.mustafazada.msdemofin.service;

import com.mustafazada.msdemofin.dto.request.AuthenticationRequestDTO;
import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import com.mustafazada.msdemofin.dto.response.Status;
import com.mustafazada.msdemofin.dto.response.StatusCode;
import com.mustafazada.msdemofin.dto.response.UserResponseDTO;
import com.mustafazada.msdemofin.exception.BadInputCredentialsException;
import com.mustafazada.msdemofin.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLoginService {
    DTOCheckUtil dtoCheckUtil;
    AuthenticationManager authenticationManager;

    public CommonResponseDTO<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        dtoCheckUtil.isValid(authenticationRequestDTO);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getPin(),
                    authenticationRequestDTO.getPassword()
            ));
        } catch (Exception e) {
            throw BadInputCredentialsException.builder().responseDTO(CommonResponseDTO.builder().status(Status.builder()
                    .statusCode(StatusCode.BAD_CREDENTIALS)
                    .message("The Credentials are incorrect. Pin: "
                            + authenticationRequestDTO.getPin() + " or Password: "
                            + authenticationRequestDTO.getPassword() + " is wrong")
                    .build()).build()).build();
        }

        return CommonResponseDTO.builder().status(Status.builder()
                        .statusCode(StatusCode.SUCCESS)
                        .message("Welcome our FIN_TECH application")
                        .build()).data(authenticationRequestDTO).build();
    }
}
