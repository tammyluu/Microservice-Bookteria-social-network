package com.tammy.identityservice.service;

import com.tammy.identityservice.dto.request.RoleRequest;
import com.tammy.identityservice.dto.response.RoleResponse;
import com.tammy.identityservice.mapper.IRoleMapper;
import com.tammy.identityservice.repository.PermissionRepository;
import com.tammy.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    IRoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        //step 1: map request to entity
        var role = roleMapper.toRole(request);
        //get list of permission from request
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions)); //create a new hashset from a permission
        //step 2: persist entity to db
        role = roleRepository.save(role);
        //convert entity to response
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        // convert entity to response, before return,map ropeResponse,
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }
    public void delete(String roleName){
        roleRepository.deleteById(roleName);
    }
}
