package com.mustafazada.fintech.config.security;

import com.mustafazada.fintech.dto.response.CommonResponseDTO;
import com.mustafazada.fintech.dto.response.Status;
import com.mustafazada.fintech.dto.response.StatusCode;
import com.mustafazada.fintech.entity.TechUser;
import com.mustafazada.fintech.exception.NoSuchUserExistException;
import com.mustafazada.fintech.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    Logger logger;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        Optional<TechUser> userRepositoryByPin = userRepository.findByPin(pin);
        if (userRepositoryByPin.isPresent()) {
            return new UserDetailsImpl(userRepositoryByPin.get());
        } else {
            logger.error("There is no user with pin {} ", pin);
            throw NoSuchUserExistException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.USER_NOT_EXIST)
                            .message("There is no user with pin: " + pin)
                            .build()).build()).build();
        }
    }
}
