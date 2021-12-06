package com.agenatech.test.vetathome.service;

import com.agenatech.test.vetathome.client.KeycloackClient;
import com.agenatech.test.vetathome.config.KeycloakConfig;
import com.agenatech.test.vetathome.payload.request.KeycloakLoginRequest;
import com.agenatech.test.vetathome.payload.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeycloakService {
    @Autowired
    private KeycloackClient keycloackClient;
    @Autowired
    private KeycloakConfig keycloakConfig;


    public AuthResponse login(KeycloakLoginRequest loginRequest){
        return keycloackClient.login(loginRequest);
    }

    public AuthResponse defaultLogin(){
        KeycloakLoginRequest loginRequest =
                KeycloakLoginRequest.builder()
                        .client_id(keycloakConfig.getClient())
                        .grant_type(keycloakConfig.getGrantType())
                        .username(keycloakConfig.getTestUsername())
                        .password(keycloakConfig.getTestPassword())
                        .build();

        return login(loginRequest);
    }

    public AuthResponse login(String username, String password){
        KeycloakLoginRequest loginRequest =
                KeycloakLoginRequest.builder()
                        .client_id(keycloakConfig.getClient())
                        .grant_type(keycloakConfig.getGrantType())
                        .username(username)
                        .password(password)
                        .build();

        return login(loginRequest);
    }
}
