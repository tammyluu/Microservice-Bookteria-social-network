package com.tammy.identityservice.service;

import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.request.UserUpdateRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.entity.User;
import com.tammy.identityservice.exception.AppException;
import com.tammy.identityservice.exception.ErrorCode;
import com.tammy.identityservice.mapper.IUserMapper;
import com.tammy.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    IUserMapper userMapper;


    /* V1
    public User createUser(UserCreationRequest req) {
        // req est les infos important pour créer une table user
        User user = new User();
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());
        return userRepository.save(user);

    }*/

    // V2 avec mapstruct
    public User createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        // param: strength of bcrypt, par default is 10, if it's the more greater the more difficult but it need too much time for hash so performance not good,
        //par convention the time tp crypt is less than 1s
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();

    }
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }



   /*// V1
   public User updateUser(String id, UserUpdateRequest req) {
        User user = getUserById(id);
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDob());
        return userRepository.save(user);
    }*/

    // V2 with mapstruct
    public UserResponse updateUser(String id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, req);
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
