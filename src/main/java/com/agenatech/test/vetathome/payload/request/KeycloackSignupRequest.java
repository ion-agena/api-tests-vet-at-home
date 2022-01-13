package com.agenatech.test.vetathome.payload.request;

import com.agenatech.test.vetathome.payload.KeycloackCredentials;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeycloackSignupRequest {
    private String email;
    private boolean enabled;
    private List<KeycloackCredentials> credentials;
}
