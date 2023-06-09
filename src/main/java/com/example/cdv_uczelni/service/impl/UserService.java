package com.example.cdv_uczelni.service.impl;

import com.example.cdv_uczelni.model.University;
import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.repo.UniversityRepository;
import com.example.cdv_uczelni.repo.UserRepository;
import com.example.cdv_uczelni.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService implements IUserService {

    AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;


    @Autowired
    public UserService(@Lazy AuthenticationManager authenticationManager,
                       @Lazy UserRepository userRepository,
                       @Lazy UniversityRepository universityRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.universityRepository = universityRepository;
    }

    @Override
    public UserDto save(User user) {
        user.setId(UUID.randomUUID());
        User newUser = userRepository.save(user);
        return new UserDto(newUser);
    }

    @Override
    public void addRoleToUser(String login, String roleName) {}

    @Override
    public Collection<UserDto> getUsers() {
        List<User> allUsers = userRepository.findAll();
        Set<UserDto> usersSet = new HashSet<>();
        allUsers.forEach(user -> {
            usersSet.add(new UserDto(user));
        });
        return usersSet;
    }

    @Override
    public boolean delete(UUID id) {
        userRepository.deleteById(id);
        return true;
    }


    @Override
    public UserDto findUserById(UUID id) {
        Optional<User> userFoundById = userRepository.findById(id);
        if (userFoundById.isPresent()) {
            return new UserDto(userFoundById.get());
        } else {
            throw new RuntimeException("User with id = " + id + " was not found!");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        Optional<User> userFoundByEmail = userRepository.findUserByEmail(email);
        if (userFoundByEmail.isPresent()) {
            return new UserDto(userFoundByEmail.get());
        } else {
            throw new RuntimeException("User with email = " + email + " was not found!");
        }
    }

    @Override
    public UserDto findUserByUsername(String username) {
        Optional<User> userFoundByUsername = userRepository.findUserByUsername(username);
        return userFoundByUsername.map(UserDto::new).orElse(null);
    }

    @Override
    public User findUserWithPasswordByUsername(String username) {
        Optional<User> userFoundByUsername = userRepository.findUserByUsername(username);
        if (userFoundByUsername.isPresent()) {
            return userFoundByUsername.get();
        } else {
            throw new RuntimeException("User with name = " + username + " was not found!");
        }
    }

    @Override
    public UserDto voteForUniversity(UUID universityId, String username) {
        UserDto userByUsername = findUserByUsername(username);
        Optional<University> byId = universityRepository.findById(universityId);
        if(userByUsername.getVoteForUniversity() == null && byId.isPresent()) {
            userByUsername.setVoteForUniversity(universityId);
            userRepository.save(new User(userByUsername));
        }
        return userByUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByLogin = findUserWithPasswordByUsername(username);

        if (userByLogin == null) {
            throw new UsernameNotFoundException("Invalid username");
        }

        return new org.springframework.security.core.userdetails.User(userByLogin.getUsername(), userByLogin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
