package com.tammy.identityservice.repository;

import com.tammy.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String>{
}
