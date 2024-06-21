package com.tammy.identityservice.mapper;

import com.tammy.identityservice.dto.request.RoleRequest;
import com.tammy.identityservice.dto.response.RoleResponse;
import com.tammy.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    //roleRequest -> roleName, entity is a set of permission, so we ignore it, when it map to role, ignore permission
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
