package com.agenatech.test.vetathome.payload.request;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class UserPetLink {
    private String userLink;
    private String petLink;


    public UUID getPetId(){
        return getIdFromLink(this.petLink);
    }

    public UUID getUserId(){
        return getIdFromLink(this.userLink);
    }

    private UUID getIdFromLink(String link){
        return UUID.fromString(link.substring(link.lastIndexOf("/") +1));
    }
}
