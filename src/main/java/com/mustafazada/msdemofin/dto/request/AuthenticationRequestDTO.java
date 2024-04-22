package com.mustafazada.msdemofin.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String password;
    String pin;
}