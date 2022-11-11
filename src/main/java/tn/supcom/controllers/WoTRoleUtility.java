package tn.supcom.controllers;

import tn.supcom.entities.User;

import java.util.HashSet;
import java.util.Set;

public interface WoTRoleUtility {
    default Role[] getRoles(User user) {
        Set<Role> roles = new HashSet<>();
        for(Role role: Role.values()){
            if((user.getPermissionLevel() & role.getValue()) != 0L){
                roles.add(role);
            }
        }
        return roles.toArray(new Role[0]);
    }

    default boolean hasRole(User user, Role role){
        return (user.getPermissionLevel() & role.getValue()) != 0L;
    }

    default void addRole(User user, Role role){
        user.setPermissionLevel(user.getPermissionLevel() | role.getValue());
    }

    default void removeRole(User user, Role role){
        user.setPermissionLevel(user.getPermissionLevel() & (~role.getValue()));
    }
}
