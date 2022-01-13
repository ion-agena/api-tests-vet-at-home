package com.agenatech.test.vetathome.service;

import com.agenatech.test.vetathome.client.ApiGwClient;
import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.agenatech.test.vetathome.config.Constants.DEFAULT_PASSWORD;

@Service
public class GatewayService {
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private ApiGwClient apiGwClient;


    public UserProfile getDefaultProfile(){
        return apiGwClient.getMyProfile(getDefaultBearer());
    }

    public UserProfile getMyProfile(String email){
        return apiGwClient.getMyProfile(getBearer(email));
    }

    public UserProfile getProfileById(String myEmail, String profileId){
        return apiGwClient.getProfileById(getBearer(myEmail), profileId);
    }

    public UserProfile getThePetOwner(String petId){
        return apiGwClient.getThePetOwner(petId);
    }

    public UserProfile putProfile(String email, UserProfile profile){
        return apiGwClient.putProfile(getBearer(email), profile);
    }

    public UserProfile putProfileById(String email, String profileId, UserProfile profile){
        return apiGwClient.putProfileById(getBearer(email), profileId, profile);
    }

    public void deleteMyProfile(String email){
        apiGwClient.deleteMyProfile(getBearer(email));
    }

    public UserProfile patchProfile(String email, Map attrs){
        return apiGwClient.patchProfile(getBearer(email), attrs);
    }

    public UserProfile patchProfileById(String email, String profileId, Map attrs){
        return apiGwClient.patchProfileById(getBearer(email), profileId, attrs);
    }


    public PetProfile saveAndLink(PetProfile petProfile){
        return apiGwClient.saveAndLink(petProfile, getDefaultBearer());
    }


    public void deletePet(String petId){
        apiGwClient.deletePet(petId);
    }


    private String getDefaultBearer(){
        return  "Bearer "  + keycloakService.defaultLogin().accessToken();
    }

    public String getBearer(String email){
        return  "Bearer "  + keycloakService.login(email, DEFAULT_PASSWORD).accessToken();
    }

}
