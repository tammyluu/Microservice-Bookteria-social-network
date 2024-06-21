package com.tammy.identityservice.controller;

import com.tammy.identityservice.dto.response.ApiResponse;
import com.tammy.identityservice.dto.request.PermissionRequest;
import com.tammy.identityservice.dto.response.PermissionResponse;
import com.tammy.identityservice.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        var result = permissionService.create(request);
        return ApiResponse.<PermissionResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
          return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }
    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<Void>builder()
                .build();
    }
}
