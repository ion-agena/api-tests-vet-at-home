package com.agenatech.test.vetathome.service;

import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DataManager {
    public UserProfile generateUserProfile(){
        String id = UUID.randomUUID().toString();
        return UserProfile.builder()
                .email(id+"@gmail.com")
                .firstName("fn " + id)
                .lastName("ln " + id)
                .avatarUrl(id)
                .build();
    }

    public UserProfile generateUserProfile(String email){
        String id = UUID.randomUUID().toString();
        return UserProfile.builder()
                .email(email)
                .firstName("fn " + id)
                .lastName("ln " + id)
                .avatarUrl(id)
                .build();
    }

    public PetProfile generatePetProfile(){
        String id = UUID.randomUUID().toString();
        return PetProfile.builder()
                .name(id)
                .animalType("dog")
                .avatarUrl(id)
                .allergies(id)
                .breed("pug")
                .neutered(true)
                .sex("male")
                .vaccinationStatus("ff")
                .build();
    }
}
