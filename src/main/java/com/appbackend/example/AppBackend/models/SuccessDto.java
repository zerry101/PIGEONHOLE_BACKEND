package com.appbackend.example.AppBackend.models;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuccessDto {
    private int code;
    private String status;
    private String message;
}
