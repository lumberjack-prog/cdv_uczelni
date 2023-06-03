package com.example.cdv_uczelni.service.impl;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.cdv_uczelni.model.Uczelnia;
import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.repo.UczelniaRepository;
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

    private final CqlSession cqlSession;

    private final UserRepository userRepository;
    private final UczelniaRepository uczelniaRepository;


    @Autowired
    public UserService(@Lazy AuthenticationManager authenticationManager, CqlSession cqlSession, UserRepository userRepository, UczelniaRepository uczelniaRepository) {
        this.authenticationManager = authenticationManager;
        this.cqlSession = cqlSession;
        this.userRepository = userRepository;
        this.uczelniaRepository = uczelniaRepository;
    }

    @Override
    public UserDto saveUser(User user) {

        UserDto user1 = findUserByUsername(user.getUsername());
        if (user1 == null) {
            String userKey;
            if (user.getUsername().equals("admin")) {
                userKey = user.setKeyForAdmin();
            } else {
                userKey = user.setNextKey();
            }

            user.setKey(userKey);
            String cql = "INSERT INTO users (key, username, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
            BoundStatement boundStatement = preparedStatement.bind(
                    userKey,
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword());

            ResultSet resultSet = cqlSession.execute(boundStatement);

            if (resultSet.wasApplied()) {
                log.info("User {} was added", user);
            }

        } else {
            String cql = "UPDATE users SET email = ?, password = ? WHERE username = ? ALLOW FILTERING";
            PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
            BoundStatement boundStatement = preparedStatement.bind(
                    user.getEmail(),
                    user.getPassword(),
                    user.getUsername());

            ResultSet resultSet = cqlSession.execute(boundStatement);
            if (resultSet.wasApplied()) {
                log.info("User {} was updated", user);
            }
        }
        return new UserDto(user);
    }

    @Override
    public void addRoleToUser(String login, String roleName) {}

    @Override
    public Collection<UserDto> getUsers() {
        String cql = "SELECT key, username, email, password FROM users";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind();
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Set<UserDto> users = new HashSet<>();

        for (Row row : resultSet) {
            User user = new User();
            user.setKey(row.getString("key"));
            user.setKey(row.getString("key"));
            user.setUsername(row.getString("username"));
            user.setEmail(row.getString("email"));
            users.add(new UserDto(user));
        }
        return users;
    }

    @Override
    public UserDto updateUser(User user) {
        return saveUser(user);
    }

    @Override
    public boolean deleteUser(String id) {
        String cql = "DELETE FROM users WHERE key = ? IF EXISTS";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind("users:" + id);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        if (resultSet.wasApplied()) {
            log.info("User has been removed");
        }
        return resultSet.wasApplied();
    }

    @Override
    public UserDto findUserByKey(String key) {
        String cql = "SELECT key, username, email FROM users WHERE key = ? ALLOW FILTERING";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind(key);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Row row = resultSet.one();

        if (row != null) {
            User user = new User();
            user.setKey(row.getString("key"));
            user.setUsername(row.getString("username"));
            user.setEmail(row.getString("email"));
            return new UserDto(user);
        }
        return null;
    }

    @Override
    public UserDto findUserByEmail(String email) {
        String cql = "SELECT key, username, email FROM users WHERE email = ? ALLOW FILTERING";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind(email);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Row row = resultSet.one();

        if (row != null) {
            User user = new User();
            user.setKey(row.getString("key"));
            user.setUsername(row.getString("username"));
            user.setEmail(row.getString("email"));
            return new UserDto(user);
        }
        return null;
    }

    @Override
    public UserDto findUserByUsername(String username) {
        String cql = "SELECT key, username, email, password FROM users WHERE username = ? ALLOW FILTERING";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind(username);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Row row = resultSet.one();

        if (row != null) {
            User user = new User();
            user.setKey(row.getString("key"));
            user.setUsername(row.getString("username"));
            user.setEmail(row.getString("email"));
            user.setPassword(row.getString("password"));
            return new UserDto(user);
        }
        return null;
    }

    @Override
    public User findUserWithPasswordByUsername(String username) {
        String cql = "SELECT key, username, email, password FROM users WHERE username = ? ALLOW FILTERING";
        PreparedStatement preparedStatement = (PreparedStatement) cqlSession.prepare(cql);
        BoundStatement boundStatement = preparedStatement.bind(username);
        ResultSet resultSet = cqlSession.execute(boundStatement);
        Row row = resultSet.one();

        if (row != null) {
            User user = new User();
            user.setKey(row.getString("key"));
            user.setUsername(row.getString("username"));
            user.setEmail(row.getString("email"));
            user.setPassword(row.getString("password"));
            return user;
        }
        return null;
    }

    public UserDto addUniversityToUserFavorites(String username, String uczelniaKey) {
        Optional<User> user = userRepository.findUserByUsername(username);
        Optional<Uczelnia> uczelnia = uczelniaRepository.findById(uczelniaKey);
        User userSaved = null;

        if (user.isPresent() && uczelnia.isPresent()) {
            Set<Uczelnia> ulubioneUczelnie = user.get().getUlubioneUczelnie();
            if (ulubioneUczelnie == null) {
                ulubioneUczelnie = new HashSet<>();
                user.get().setUlubioneUczelnie(ulubioneUczelnie);
            }

            ulubioneUczelnie.add(uczelnia.get());
            userSaved = userRepository.save(user.get());

        } else {
            throw new RuntimeException("Uczelnia with key: " + uczelniaKey + " was not found!");
        }
        return new UserDto(userSaved);
    }

    public boolean removeUniversityFromUserFavorites(String username, String uczelniaKey) {
        Optional<User> user = userRepository.findUserByUsername(username);
        Optional<Uczelnia> uczelnia = uczelniaRepository.findById(uczelniaKey);
        User userSaved = null;
        if (user.isPresent() && uczelnia.isPresent()) {
            Set<Uczelnia> ulubioneUczelnie = user.get().getUlubioneUczelnie();
            if (ulubioneUczelnie != null) {
                ulubioneUczelnie.remove(uczelnia.get());
                userRepository.save(user.get());
                return true;
            }
        } else {
            throw new RuntimeException("Uczelnia with key: " + uczelniaKey + " was not found!");
        }
        return false;
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
