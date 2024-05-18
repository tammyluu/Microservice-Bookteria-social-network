package com.tammy.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
   // id auto-incrément.
   // private String username; never update username
     String password;
     String firstName;
     String lastName;
     LocalDate dob;
}
