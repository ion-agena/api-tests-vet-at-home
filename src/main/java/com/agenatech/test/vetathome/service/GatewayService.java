package com.agenatech.test.vetathome.service;

import com.agenatech.test.vetathome.client.ApiGwClient;
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


    public EmbeddedProfilesResponseRoot searchProfilesByPetIds(List<String> petIds){
        return apiGwClient.searchProfilesByPets(petIds);
    }

    public UserProfile putProfile(UserProfile profile){
        return apiGwClient.putProfile(profile.getAvatarUrl(), profile);
    }

    public PetProfile savePet(PetProfile petProfile){
        return apiGwClient.savePet(petProfile);
    }

    public PetProfile saveAndLink(PetProfile petProfile){
        String bearerToken = keycloakService.defaultLogin().accessToken();
        return apiGwClient.saveAndLink(petProfile, "Bearer "  + bearerToken);
    }

//    todo change implementation to link to other people
    public void link(String petId){
        String bearerToken = keycloakService.defaultLogin().accessToken();
        apiGwClient.link(petId, "Bearer "  + bearerToken);
    }

}
