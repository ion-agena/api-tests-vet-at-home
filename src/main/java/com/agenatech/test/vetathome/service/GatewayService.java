package com.agenatech.test.vetathome.service;

import com.agenatech.test.vetathome.client.ApiGwClient;
import com.agenatech.test.vetathome.payload.request.UserPetLink;
import com.agenatech.test.vetathome.payload.response.EmbeddedProfilesResponseRoot;
import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private ApiGwClient apiGwClient;


    public UserProfile getMyProfile(){
        String bearerToken = keycloakService.defaultLogin().accessToken();
        return apiGwClient.getMyProfile("Bearer "  + bearerToken);
    }


    public UserProfile getThePetOwner(String petId){
        return apiGwClient.getThePetOwner(petId);
    }

    public UserProfile putProfile(String id, UserProfile profile){
        return apiGwClient.putProfile(id, profile);
    }

    public PetProfile savePet(PetProfile petProfile){
        return apiGwClient.savePet(petProfile);
    }

    public PetProfile saveAndLink(PetProfile petProfile){
        String bearerToken = keycloakService.defaultLogin().accessToken();
        return apiGwClient.saveAndLink(petProfile, "Bearer "  + bearerToken);
    }

    public void link(UserPetLink userPetLink){
        String bearerToken = keycloakService.defaultLogin().accessToken();
        apiGwClient.link(userPetLink, "Bearer "  + bearerToken);
    }

    public void deletePet(String petId){
        apiGwClient.deletePet(petId);
    }


}
