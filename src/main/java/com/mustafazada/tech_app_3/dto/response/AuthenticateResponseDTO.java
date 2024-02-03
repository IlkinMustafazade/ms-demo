package com.mustafazada.tech_app_3.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticateResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String tokenForUser;
}
