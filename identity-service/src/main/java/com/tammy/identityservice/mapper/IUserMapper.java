package com.tammy.identityservice.mapper;

import com.tammy.identityservice.dto.request.UserCreationRequest;
import com.tammy.identityservice.dto.request.UserUpdateRequest;
import com.tammy.identityservice.dto.response.UserResponse;
import com.tammy.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface IUserMapper {
   /* -----Map with obj has the same properties----------*/
    //Mapper userCreationRequest with obj User: receive param request which has type usercreationReq and return une class User
    User toUser(UserCreationRequest request);
    // -> to use UserMapping, we are declared annotation @Mapper with componentModel ="spring" so we can autowired in userService for using

    UserResponse toUserResponse(User user);

    // @MappingTarget for mapping UserUpdateRequest into Obj User
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    /* -----Map with obj hasn't the same properties -> @Mapping----------*/

   /* //@Mapping(source = "firstName", target = "lastName") => firstName map to lastName -> firstName == lastName
    UserResponse toUserResponse(User user);*/

   /* //@Mapping(target = "lastName", ignore = true) ignore lastName, lasName = null
    UserResponse toUserResponse(User user);*/
}
