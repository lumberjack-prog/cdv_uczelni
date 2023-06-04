package com.example.cdv_uczelni.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String header = request.getHeader(AUTHORIZATION);
            if (header != null && header.startsWith("Basic ")) {
                try {
                    String token = header.substring("Basic ".length());
                    byte[] decodedBytes = Base64.getDecoder().decode(token);
                    String usernamePassword = new String(decodedBytes);
                    String username = usernamePassword.split(":")[0];
                    String password = usernamePassword.split(":")[1];

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, password,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    response.setStatus(SC_NOT_FOUND);
                    Map<String, String> message = new HashMap<>();
                    message.put("_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper()
                            .writerWithDefaultPrettyPrinter()
                            .writeValue(response.getOutputStream(), message);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
