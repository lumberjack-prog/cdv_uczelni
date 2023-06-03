package com.example.cdv_uczelni.service;

import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;


public interface IUserService extends UserDetailsService {
//    User save(UserRegistrationDto registrationDto);

    UserDto saveUser(User user);
    void addRoleToUser(String login, String roleName);
    Collection<UserDto> getUsers();
    UserDto updateUser(User user);
    boolean deleteUser(String id);
    UserDto findUserByKey(String key);
    UserDto findUserByEmail(String email);
    UserDto findUserByUsername(String username);
    User findUserWithPasswordByUsername(String username);
    UserDto addUniversityToUserFavorites(String username, String uczelniaKey);
}
