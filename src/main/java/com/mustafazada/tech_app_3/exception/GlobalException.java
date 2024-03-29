package com.mustafazada.tech_app_3.exception;


import com.mustafazada.tech_app_3.dto.response.CommonResponseDTO;
import com.mustafazada.tech_app_3.dto.response.Status;
import com.mustafazada.tech_app_3.dto.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> internalError(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>
                (
                        CommonResponseDTO.builder().status(Status.builder()
                                .statusCode(StatusCode.INTERNAL_ERROR)
                                .message("Internal Error").build()).build(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
    }

    @ExceptionHandler(value = InvalidDTO.class)
    public ResponseEntity<?> invalidDTO(InvalidDTO invalidDTO) {
        return new ResponseEntity<>(invalidDTO.getResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExist.class)
    public ResponseEntity<?> userExist(UserAlreadyExist userAlreadyExist) {
        return new ResponseEntity<>(userAlreadyExist.getResponseDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoSuchUserExist.class)
    public ResponseEntity<?> notFoundUser(NoSuchUserExist noSuchUserExist) {
        return new ResponseEntity<>(noSuchUserExist.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoActiveAccount.class)
    public ResponseEntity<?> notFoundUser(NoActiveAccount noActiveAccount) {
        return new ResponseEntity<>(noActiveAccount.getResponseDTO(), HttpStatus.NOT_FOUND);
    }
}
