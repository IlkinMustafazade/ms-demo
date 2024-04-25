package com.mustafazada.msdemofin.service;

import com.mustafazada.msdemofin.config.security.JWTUtil;
import com.mustafazada.msdemofin.dto.request.AuthenticationRequestDTO;
import com.mustafazada.msdemofin.dto.response.*;
import com.mustafazada.msdemofin.exception.BadInputCredentialsException;
import com.mustafazada.msdemofin.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLoginService {
    JWTUtil jwtUtil;
    DTOCheckUtil dtoCheckUtil;
    UserDetailsService userDetailsService;
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDTO.getPin());
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Token was created SUCCESSFULLY")
                .build()).data(AuthenticationResponseDTO.builder()
                .token(jwtUtil.createToken(userDetails))
                .build()).build();
    }
}
