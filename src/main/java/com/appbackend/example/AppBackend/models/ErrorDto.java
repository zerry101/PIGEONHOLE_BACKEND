package com.appbackend.example.AppBackend.models;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ErrorDto {

    private int code;
    private String status;
    private String message;


}
