package com.appbackend.example.AppBackend.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class JwtResponse {
    private String jwtToken;

    private  String refreshTokenString;

    private String username;


}
