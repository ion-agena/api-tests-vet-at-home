package com.agenatech.test.vetathome.client;

import com.agenatech.test.vetathome.payload.request.UserPetLink;
import com.agenatech.test.vetathome.payload.response.EmbeddedProfilesResponseRoot;
import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url = "${api-gw.url}", value = "${api-gw.url}")
@Service
public interface ApiGwClient {

    @GetMapping(value = "${profiles.profiles-me-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile getMyProfile(@RequestHeader("Authorization") String token);

    @GetMapping(value = "${profiles.search-profiles-url}/findByPetsIdIn", consumes = MediaType.APPLICATION_JSON_VALUE)
    EmbeddedProfilesResponseRoot searchProfilesByPets(@RequestParam("petIds") List<String> petIds);

    @PutMapping(value = "${profiles.profiles-url}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile putProfile(@PathVariable("id") String id, UserProfile profile);

    @PostMapping(value = "${profiles.pets-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    PetProfile savePet(PetProfile petProfile);

    @PostMapping(value = "${profiles.service-url}/me/pets", consumes = MediaType.APPLICATION_JSON_VALUE)
    PetProfile saveAndLink(PetProfile petProfile, @RequestHeader("Authorization") String token);

    @PutMapping(value = "${profiles.root-service-url}/me/pets/link", consumes = MediaType.APPLICATION_JSON_VALUE)
    PetProfile link(UserPetLink userPetLink, @RequestHeader("Authorization") String token);

}
