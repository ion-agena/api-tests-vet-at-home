package com.agenatech.test.vetathome.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KeycloakLoginRequest  {
    @JsonProperty("client_id")
    private String client_id;
    @JsonProperty("grant_type")
    private String grant_type;
    private String username;
    private String password;
}
