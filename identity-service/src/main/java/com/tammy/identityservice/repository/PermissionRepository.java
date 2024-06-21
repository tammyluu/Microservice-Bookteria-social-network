package com.tammy.identityservice.repository;

import com.tammy.identityservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String>{

}
