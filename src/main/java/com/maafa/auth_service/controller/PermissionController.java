package com.maafa.auth_service.controller;

import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionRepository permissionRepository;

    // ==========================================
    // GET ALL PERMISSIONS
    // ==========================================
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionRepository.findAll());
    }

    // ==========================================
    // GET PERMISSION BY ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer id) {
        Optional<Permission> permission = permissionRepository.findById(id);

        if (permission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Permission not found"));
        }

        return ResponseEntity.ok(permission.get());
    }

    // ==========================================
    // CREATE PERMISSION
    // ==========================================
    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody Permission permissionRequest) {

        if (permissionRepository.existsByName(permissionRequest.getName())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Permission name already exists"));
        }

        Permission permission = Permission.builder()
                .name(permissionRequest.getName())
                .description(permissionRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Permission savedPermission = permissionRepository.save(permission);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Permission created successfully");
        response.put("permissionId", savedPermission.getId());
        response.put("name", savedPermission.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==========================================
    // UPDATE PERMISSION
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Integer id,
                                              @RequestBody Permission permissionRequest) {

        Optional<Permission> optionalPermission = permissionRepository.findById(id);

        if (optionalPermission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Permission not found"));
        }

        Permission permission = optionalPermission.get();

        if (permissionRequest.getName() != null) {
            permission.setName(permissionRequest.getName());
        }

        if (permissionRequest.getDescription() != null) {
            permission.setDescription(permissionRequest.getDescription());
        }

        permission.setUpdatedAt(LocalDateTime.now());

        permissionRepository.save(permission);

        return ResponseEntity.ok(Map.of("message", "Permission updated successfully"));
    }

    // ==========================================
    // DELETE PERMISSION
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Integer id) {

        if (!permissionRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Permission not found"));
        }

        permissionRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("message", "Permission deleted successfully"));
    }
}