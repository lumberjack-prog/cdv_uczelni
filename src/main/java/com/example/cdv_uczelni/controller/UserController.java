package com.example.cdv_uczelni.controller;

import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.service.IUserService;
import com.example.cdv_uczelni.service.impl.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private FavoriteService favoriteService;


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Map<String, Collection<UserDto>> responseMap = new HashMap<>();
        responseMap.put("users", userService.getUsers());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByUsername/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody User user){
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody User user){
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteUserById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> delete(@PathVariable UUID id){
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/addToFavorites/{universityKey}", method = RequestMethod.GET)
    public ResponseEntity<?> addToFavorites(@PathVariable UUID universityKey){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(favoriteService.save(authentication.getName(), universityKey), HttpStatus.OK);
    }

    @RequestMapping(value = "/voteFor/{universityId}", method = RequestMethod.GET)
    public ResponseEntity<?> removeFromFavorites(@PathVariable UUID universityId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(userService.voteForUniversity(universityId, authentication.getName()), HttpStatus.OK);
    }
}
