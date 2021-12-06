package com.agenatech.test.vetathome.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmbeddedProfilesResponseRoot {
    private EmbeddedProfilesResponse _embedded;
}
