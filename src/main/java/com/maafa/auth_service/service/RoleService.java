package com.maafa.auth_service.service;

import com.maafa.auth_service.entity.Permission;
import com.maafa.auth_service.entity.Role;
import com.maafa.auth_service.repository.PermissionRepository;
import com.maafa.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // ==========================================
    // GET ALL ROLES
    // ==========================================
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // ==========================================
    // GET ROLE BY ID
    // ==========================================
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    // ==========================================
    // CREATE ROLE
    // ==========================================
    @Transactional
    public Role createRole(Role roleRequest) {

        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new RuntimeException("Role name already exists");
        }

        Set<Permission> permissions = new HashSet<>();

        if (roleRequest.getPermissions() != null) {
            for (Permission p : roleRequest.getPermissions()) {
                Permission permission = permissionRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("Permission not found with id: " + p.getId()));
                permissions.add(permission);
            }
        }

        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .permissions(permissions)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return roleRepository.save(role);
    }

    // ==========================================
    // UPDATE ROLE
    // ==========================================
    @Transactional
    public Role updateRole(Integer id, Role roleRequest) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (roleRequest.getName() != null) {
            role.setName(roleRequest.getName());
        }

        if (roleRequest.getDescription() != null) {
            role.setDescription(roleRequest.getDescription());
        }

        if (roleRequest.getPermissions() != null) {
            Set<Permission> permissions = new HashSet<>();
            for (Permission p : roleRequest.getPermissions()) {
                Permission permission = permissionRepository.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("Permission not found with id: " + p.getId()));
                permissions.add(permission);
            }
            role.setPermissions(permissions);
        }

        role.setUpdatedAt(LocalDateTime.now());

        return roleRepository.save(role);
    }

    // ==========================================
    // DELETE ROLE
    // ==========================================
    @Transactional
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}