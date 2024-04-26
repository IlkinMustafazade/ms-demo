package com.mustafazada.msdemofin.exception;

import com.mustafazada.msdemofin.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PACKAGE)
public class NotAccountFoundException extends RuntimeException {
    CommonResponseDTO<?> responseDTO;
}
