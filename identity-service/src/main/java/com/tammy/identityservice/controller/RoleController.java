package com.tammy.identityservice.controller;

import com.tammy.identityservice.dto.response.ApiResponse;
import com.tammy.identityservice.dto.request.RoleRequest;
import com.tammy.identityservice.dto.response.RoleResponse;
import com.tammy.identityservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
          return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        roleService.delete(permissionName);
        return ApiResponse.<Void>builder()
                .build();
    }
}
