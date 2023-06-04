package com.example.cdv_uczelni.filter;

import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.service.impl.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        if (username != null && password != null) {
            log.info("Username: {}", username);
            log.info("Password: {}", password);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
                username = jsonNode.get("username").asText();
                password = jsonNode.get("password").asText();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (username == null || password == null) {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> message = new HashMap<>();
                message.put("_message", "Bad credentials");
                try {
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            log.info("inputstream, {}", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        User user = (User) authResult.getPrincipal();

        Map<String, Object> responseMap = new HashMap<>();
        UserDto user1 = userService.findUserByUsername(user.getUsername());
        assert user1 != null;
        responseMap.put("user_key", user1.getId());
        responseMap.put("login", user1.getUsername());
        responseMap.put("email", user1.getEmail());
        String basicToken = user.getUsername() + ":" + "null";
        responseMap.put("basic_token", Base64.getEncoder().encodeToString(basicToken.getBytes()));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
    }

}