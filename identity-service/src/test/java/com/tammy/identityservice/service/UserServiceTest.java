package com.tammy.identityservice.service;

import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.entity.User;
import com.tammy.identityservice.exception.AppException;
import com.tammy.identityservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    //UserMapper, PasswordEncoder are third-party libraries, so we don't need to test them. ByPass them and run real code
    //UserRepository is at other layer, so we need to mock it
    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;

    @Override
    public String toString() {
        return super.toString();
    }

    @BeforeEach
    public void initData(){
        LocalDate dob = LocalDate.of(1990, 1, 1);
        request = UserCreationRequest.builder()
                .username("tammy")
                .password("12345678")
                .firstName("Tammy")
                .lastName("Nguyen")
                .dob(dob)
                .build();
        userResponse = UserResponse.builder()
                .id("c0df4d3ace19")
                .username("tammy")
                .firstName("Tammy")
                .lastName("Nguyen")
                .dob(dob)
                .build();
        user = User.builder()
                .id("c0df4d3ace19")
                .username("tammy")
               // .password("12345678") -> password is hash, so we don't need to check it
                .firstName("Tammy")
                .lastName("Nguyen")
                .dob(dob)
                .build();
    }

    @Test
    void createUserWithValidRequestIsSuccessful() throws Exception {
        // Test happy case 1: create user successfully
        // Given : Input data -> InitData
        // object to String

        // Mock service return response directly without call real service
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false); // return false when check username exist
        when(userRepository.save(ArgumentMatchers.any()))
                .thenReturn(user);

        // WHEN : Call API
         var  response= userService.createUser(request);

        //  THEN : Expect Return response is success
        Assertions.assertThat(response.getId()).isEqualTo("c0df4d3ace19");
        Assertions.assertThat(response.getUsername()).isEqualTo("tammy");

    }
    @Test
    void createUserWithExistingUserIsFail() throws Exception {
        // Test happy case 1: create user successfully
        // Given : Input data -> InitData
        // object to String

        // Mock service return response directly without call real service
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true); // return true because check username existing

        // WHEN : Call API
        // assertThrows is used to check exception has 2 params: AppException.class and Executable is a lambda expression
       var exception = assertThrows(AppException.class, () -> userService.createUser(request));

       /* THEN : Expect Return response is fail
        return  exactly message is "USERNAME_EXISTED"*/
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("User existed");

    }
}
