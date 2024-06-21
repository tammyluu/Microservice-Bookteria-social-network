package com.tammy.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // avoid null message when code success
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    @Builder.Default
    int code = 200;
    String message; //for error only
    T result;
}
