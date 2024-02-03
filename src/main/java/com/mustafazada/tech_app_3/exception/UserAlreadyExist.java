package com.mustafazada.tech_app_3.exception;

import com.mustafazada.tech_app_3.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAlreadyExist extends RuntimeException {
    CommonResponseDTO<?> responseDTO;
}
