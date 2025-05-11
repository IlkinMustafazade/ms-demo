package com.mustafazada.fintech.exception;

import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotActiveAccountException extends RuntimeException {
    CommonResponseDTO<?> responseDTO;
}
