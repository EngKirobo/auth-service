package com.maafa.auth_service.controller;

import com.maafa.auth_service.entity.Role;
import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.repository.RoleRepository;
import com.maafa.auth_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // ==========================================
    // GET ALL ROLES
    // ==========================================
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    // ==========================================
    // GET ROLE BY ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Role not found"));
        }

        return ResponseEntity.ok(role.get());
    }

    // ==========================================
    // CREATE ROLE
    // ==========================================
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role roleRequest) {

        if (roleRepository.existsByName(roleRequest.getName())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Role name already exists"));
        }

        // Handle permissions if provided
        Set<Permission> permissions = new HashSet<>();
        if (roleRequest.getPermissions() != null) {
            for (Permission p : roleRequest.getPermissions()) {
                permissionRepository.findById(p.getId()).ifPresent(permissions::add);
            }
        }

        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .permissions(permissions)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Role savedRole = roleRepository.save(role);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Role created successfully");
        response.put("roleId", savedRole.getId());
        response.put("name", savedRole.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==========================================
    // UPDATE ROLE
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role roleRequest) {

        Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Role not found"));
        }

        Role role = optionalRole.get();

        if (roleRequest.getName() != null) {
            role.setName(roleRequest.getName());
        }

        if (roleRequest.getDescription() != null) {
            role.setDescription(roleRequest.getDescription());
        }

        // Update permissions if provided
        if (roleRequest.getPermissions() != null) {
            Set<Permission> permissions = new HashSet<>();
            for (Permission p : roleRequest.getPermissions()) {
                permissionRepository.findById(p.getId()).ifPresent(permissions::add);
            }
            role.setPermissions(permissions);
        }

        role.setUpdatedAt(LocalDateTime.now());

        roleRepository.save(role);

        return ResponseEntity.ok(Map.of("message", "Role updated successfully"));
    }

    // ==========================================
    // DELETE ROLE
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {

        if (!roleRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Role not found"));
        }

        roleRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
    }
}