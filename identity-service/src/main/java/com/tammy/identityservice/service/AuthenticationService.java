package com.tammy.identityservice.service;

import com.tammy.identityservice.dto.request.AuthenticationResquest;
import com.tammy.identityservice.exception.AppException;
import com.tammy.identityservice.exception.ErrorCode;
import com.tammy.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

     public boolean authenticate (AuthenticationResquest resquest){
        var user = userRepository.findByUsername(resquest.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(resquest.getPassword(),user.getPassword());
    }
}
