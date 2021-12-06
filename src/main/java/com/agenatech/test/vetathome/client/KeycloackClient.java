package com.agenatech.test.vetathome.client;


import com.agenatech.test.vetathome.payload.request.KeycloakLoginRequest;
import com.agenatech.test.vetathome.payload.response.AuthResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${auth-server.url}", value = "${auth-server.url}")
@Service
public interface KeycloackClient {

    @PostMapping(value = "${auth-server.token-uri}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    AuthResponse login(KeycloakLoginRequest request);

//    @PostMapping(value = "${auth-server.users-uri}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity registerUser(KeycloackSignupRequest signupRequest, @RequestHeader("Authorization") String adminToken);

//    @PutMapping(value = "${auth-server.email-actions-uri}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity emailAction(@PathVariable("id") String id, List<KeycloackRequiredAction> actions, @RequestHeader("Authorization") String adminToken);


}

