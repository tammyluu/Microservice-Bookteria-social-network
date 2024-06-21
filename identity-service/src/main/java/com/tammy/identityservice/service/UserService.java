package com.tammy.identityservice.service;

import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.request.UserUpdateRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.entity.User;
import com.tammy.identityservice.enums.Role;
import com.tammy.identityservice.exception.AppException;
import com.tammy.identityservice.exception.ErrorCode;
import com.tammy.identityservice.mapper.IUserMapper;
import com.tammy.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    IUserMapper userMapper;
    PasswordEncoder passwordEncoder;

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
    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        //user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        //get role
        //context.getAuthentication().getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    // check if user has role admin before executing this method
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();

    }

    // check after method executed and return object ,user can only get his own info
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        log.info("In method get User by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }



   /*
   // V1
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
