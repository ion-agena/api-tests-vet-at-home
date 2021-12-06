package com.agenatech.test.vetathome.payload.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HalLinks {
    private Map<String, String> self;
    private Map<String, String> owners;
    private Map<String, String> pet;

}
