package com.maafa.auth_service.service;

import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    // ==========================================
    // GET ALL PERMISSIONS
    // ==========================================
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    // ==========================================
    // GET PERMISSION BY ID
    // ==========================================
    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    // ==========================================
    // CREATE PERMISSION
    // ==========================================
    @Transactional
    public Permission createPermission(Permission permissionRequest) {

        if (permissionRepository.existsByName(permissionRequest.getName())) {
            throw new RuntimeException("Permission name already exists");
        }

        Permission permission = Permission.builder()
                .name(permissionRequest.getName())
                .description(permissionRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return permissionRepository.save(permission);
    }

    // ==========================================
    // UPDATE PERMISSION
    // ==========================================
    @Transactional
    public Permission updatePermission(Integer id, Permission permissionRequest) {

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        if (permissionRequest.getName() != null) {
            permission.setName(permissionRequest.getName());
        }

        if (permissionRequest.getDescription() != null) {
            permission.setDescription(permissionRequest.getDescription());
        }

        permission.setUpdatedAt(LocalDateTime.now());

        return permissionRepository.save(permission);
    }

    // ==========================================
    // DELETE PERMISSION
    // ==========================================
    @Transactional
    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }
}