package com.appbackend.example.AppBackend.models;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OtpRequest {
    public String email;
    public String otp;

}
