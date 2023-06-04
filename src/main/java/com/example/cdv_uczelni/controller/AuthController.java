package com.example.cdv_uczelni.controller;

import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.service.impl.UserService;
import com.example.cdv_uczelni.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AuthController {


    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public void register(@RequestBody User user, HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        UserDto userByEmail = userService.findUserByEmail(user.getEmail());
        UserDto userByUsername = userService.findUserByUsername(user.getUsername());
        Map<String, String> messageMap = new HashMap<>();

        if (!user.getUsername().isEmpty() && !user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
            if (userByEmail != null && userByUsername != null) {
                messageMap.put("_message", "Email and Username already exist");
                response.setStatus(SC_BAD_REQUEST);
            } else if (userByEmail != null) {
                messageMap.put("_message", "Email already exists!");
                response.setStatus(SC_BAD_REQUEST);
            } else if (userByUsername != null) {
                messageMap.put("_message", "Username already exists!");
                response.setStatus(SC_BAD_REQUEST);
            } else {
                if (Util.validateEmailAddress(user.getEmail())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    UserDto user1 = userService.save(user);
                    messageMap.put("user_key", user1.getId().toString());
                    messageMap.put("login", user1.getUsername());
                    messageMap.put("email", user1.getEmail());
                    String basicToken = user.getUsername() + ":" + "null";
                    messageMap.put("basic_token", Base64.getEncoder().encodeToString(basicToken.getBytes()));
                } else {
                    messageMap.put("_message", "Email is invalid!");
                    response.setStatus(SC_BAD_REQUEST);
                }
            }
        } else {
            messageMap.put("_message", "Please, provide username, email and password!");
            response.setStatus(SC_BAD_REQUEST);
        }

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(response.getOutputStream(), messageMap);

    }
}
