package com.example.cdv_uczelni.service.impl;

import com.example.cdv_uczelni.model.Favorite;
import com.example.cdv_uczelni.model.University;
import com.example.cdv_uczelni.model.UniversityUDT;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.repo.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final UniversityService universityService;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, UserService userService, UniversityService universityService) {
        this.favoriteRepository = favoriteRepository;
        this.userService = userService;
        this.universityService = universityService;
    }

    public Favorite save(String username, UUID universityId) {
        UserDto userByUsername = userService.findUserByUsername(username);
        Optional<University> universityFoundById = universityService.getById(universityId);
        Set<Favorite> allByUsername = getAllByUsername(username);
        Optional<Favorite> favoriteExists;
        Favorite favorite = null;
        if (universityFoundById.isPresent() && userByUsername != null) {
            favoriteExists = allByUsername.stream().filter(favoriteByUserName -> favoriteByUserName.getFavoriteUniversities().contains(universityFoundById.get())).findFirst();
            if (favoriteExists.isEmpty()) {
                favorite = new Favorite();
                favorite.setUsername(username);
                Set<UniversityUDT> favoriteUniversities = favorite.getFavoriteUniversities();
                if (favoriteUniversities == null) {
                    favoriteUniversities = new HashSet<>();
                    favorite.setId(UUID.randomUUID());
                }
                favoriteUniversities.add(University.universityToUniversityUDT(universityFoundById.get()));
                favorite.setFavoriteUniversities(favoriteUniversities);
            } else {
                favorite = favoriteExists.get();
            }
        } else {
            throw new RuntimeException("University with id=" + universityId + " was not found!");
        }
        return favoriteRepository.save(favorite);
    }

    public Set<Favorite> getAll() {
        return new HashSet<>(favoriteRepository.findAll());
    }

    public Set<Favorite> getAllByUsername(String username) {
        Set<Favorite> all = getAll();
        return all.stream().filter(favorite ->
                favorite.getUsername().equals(username)).collect(Collectors.toSet());
    }
}
