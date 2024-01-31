package com.appbackend.example.AppBackend.models;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder

public class RefreshTokenRequest {


    private String refreshTokenString;



}
