package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    boolean existsByName(String name);

    Optional<Permission> findByName(String name);
}