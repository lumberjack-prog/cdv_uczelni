package com.example.cdv_uczelni.service.impl;

import com.example.cdv_uczelni.model.Major;
import com.example.cdv_uczelni.model.University;
import com.example.cdv_uczelni.model.UniversityJson;
import com.example.cdv_uczelni.model.Faculty;
import com.example.cdv_uczelni.repo.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UniversityService {

    private UniversityRepository universityRepository;

    @Autowired
    public void setUniversityRepository(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public Set<University> getAll() {
        List<University> all = universityRepository.findAll();
        return new HashSet<>(all);
    }

    public Optional<University> getById(UUID key) {
        return universityRepository.findById(key);
    }

    public Set<University> getByMajorType(String majorType) {
        Set<University> all = getAll();
        Set<University> result = new HashSet<>();
        all.forEach(university -> {
            List<Faculty> faculties = university.getFaculties();
            faculties.forEach(faculty -> {
                Set<Major> majors = faculty.getMajors();
                majors.forEach(major -> {
                    if (major.getType().equals(majorType)) {
                        result.add(university);
                    }
                });
            });
        });
        return result;
    }

    public boolean deleteById(UUID key) {
        Optional<University> byId = getById(key);

        if (byId.isPresent()) {
            universityRepository.deleteById(key);
            return true;
        } else {
            return false;
        }
    }

    public Optional<University> getByName(String name) {
       return universityRepository.findUniversityByName(name);
    }

    public Set<University> saveAll(UniversityJson universityJson) {
        List<University> universities = universityJson.getUniversities();
        Set<University> universitySet = new HashSet<>(universities);
        for (University university : universitySet) {
            save(university);
        }
        return universitySet;
    }

    public University save(University university) {
        university.setId(UUID.randomUUID());
        return universityRepository.save(university);
    }
}
