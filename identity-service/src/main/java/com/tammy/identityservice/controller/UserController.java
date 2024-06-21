package com.tammy.identityservice.controller;

import com.tammy.identityservice.dto.response.ApiResponse;
import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.request.UserUpdateRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Controller")
@Slf4j
public class UserController {

    UserService userService;



    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){

        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list ")
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        // SecurityContextHolder contains infos of user which is signing in now
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    @Operation(summary = "Get user detail", description = "Send a request via this API to get user information by Id")
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }
    // this is for user to get their own info by token without param
    @Operation(summary = "Get user detail", description = "Send a request via this API to get user information")
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @Operation(summary = "Update user", description = "Send a request via this API to update user")
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }
}
