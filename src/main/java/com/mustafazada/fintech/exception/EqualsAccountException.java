package com.mustafazada.fintech.exception;

import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EqualsAccountException extends RuntimeException {
    CommonResponseDTO<?> responseDTO;
}
