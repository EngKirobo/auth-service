package com.maafa.auth_service.repository;

import com.maafa.auth_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);

    Optional<Role> findByName(String name);

    // Get all roles ordered alphabetically by name
    List<Role> findAllByOrderByNameAsc();
}