package com.agenatech.test.vetathome.payload.response;

import lombok.*;

import java.io.ByteArrayInputStream;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder @ToString
public class PetProfile {

    private String name;


    private String animalType;

    private String breed;


    private String sex;

    private boolean neutered;

    private String allergies;

    private String vaccinationStatus;

    private String avatarUrl;


    private HalLinks _links;


}
