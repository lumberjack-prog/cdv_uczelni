package com.example.cdv_uczelni.service.impl;

import com.example.cdv_uczelni.model.Favorite;
import com.example.cdv_uczelni.model.University;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.repo.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        Favorite favorite = new Favorite();

        if (userByUsername != null && universityFoundById.isPresent()) {
            favorite.setUsername(username);
            Set<University> favoriteUniversities = favorite.getFavoriteUniversities();
            if (favoriteUniversities == null) {
                favoriteUniversities = new HashSet<>();
            }
            favoriteUniversities.add(universityFoundById.get());
            favorite.setFavoriteUniversities(favoriteUniversities);
        }

        favorite.setId(UUID.randomUUID());
        return favoriteRepository.save(favorite);
    }
}
