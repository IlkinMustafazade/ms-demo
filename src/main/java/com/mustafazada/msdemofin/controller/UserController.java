package com.mustafazada.msdemofin.controller;

import com.mustafazada.msdemofin.dto.request.AuthenticationRequestDTO;
import com.mustafazada.msdemofin.dto.request.UserRequestDTO;
import com.mustafazada.msdemofin.service.UserLoginService;
import com.mustafazada.msdemofin.service.UserRegisterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserRegisterService userRegisterService;
    UserLoginService userLoginService;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userRegisterService.saveUser(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return new ResponseEntity<>(userLoginService.loginUser(authenticationRequestDTO), HttpStatus.OK);
    }
}
