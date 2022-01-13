package com.agenatech.test.vetathome.client;

import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(url = "${api-gw.url}", value = "${api-gw.url}")
@Service
public interface ApiGwClient {

    @GetMapping(value = "${profiles.profiles-me-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile getMyProfile(@RequestHeader("Authorization") String token);

    @GetMapping(value = "${profiles.profiles-url}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile getProfileById(@RequestHeader("Authorization") String token, @PathVariable("id") String id);

    @GetMapping(value = "${profiles.pets-url}/{id}/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile getThePetOwner(@PathVariable("id") String id);

    @PutMapping(value = "${profiles.profiles-me-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile putProfile(@RequestHeader("Authorization") String token, UserProfile profile);

    @DeleteMapping(value = "${profiles.profiles-me-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile deleteMyProfile(@RequestHeader("Authorization") String token);

    @PutMapping(value = "${profiles.profiles-url}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile putProfileById(@RequestHeader("Authorization") String token, @PathVariable("id") String id, UserProfile profile);

    @PatchMapping(value = "${profiles.profiles-me-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile patchProfile(@RequestHeader("Authorization") String token, Map attrs);

    @PatchMapping(value = "${profiles.profiles-url}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfile patchProfileById(@RequestHeader("Authorization") String token, @PathVariable("id") String id, Map attrs);

    @PostMapping(value = "${profiles.pets-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    PetProfile savePet(PetProfile petProfile);

    @PostMapping(value = "${profiles.profiles-me-url}/pets", consumes = MediaType.APPLICATION_JSON_VALUE)
    PetProfile saveAndLink(PetProfile petProfile, @RequestHeader("Authorization") String token);


    @DeleteMapping(value = "${profiles.pets-url}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deletePet(@PathVariable("id") String id);
}
