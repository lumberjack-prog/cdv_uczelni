package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.UUID;


public interface IUserService extends UserDetailsService {
//    User save(UserRegistrationDto registrationDto);

    UserDto save(User user);
    void addRoleToUser(String login, String roleName);
    Collection<UserDto> getUsers();
    boolean delete(UUID id);
    UserDto findUserById(UUID id);
    UserDto findUserByEmail(String email);
    UserDto findUserByUsername(String username);
    User findUserWithPasswordByUsername(String username);
//    UserDto addUniversityToUserFavorites(String username, UUID universityId);
}
