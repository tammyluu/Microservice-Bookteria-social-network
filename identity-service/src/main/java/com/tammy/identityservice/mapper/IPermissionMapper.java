package com.tammy.identityservice.mapper;

import com.tammy.identityservice.dto.request.PermissionRequest;
import com.tammy.identityservice.dto.response.PermissionResponse;
import com.tammy.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}