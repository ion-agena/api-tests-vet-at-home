package com.agenatech.test.vetathome.payload;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeycloackCredentials {
    private String type;
    private String value;
    boolean temporary;
}
