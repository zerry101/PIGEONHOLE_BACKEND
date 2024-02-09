package com.appbackend.example.AppBackend.models;

import com.appbackend.example.AppBackend.entities.User;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
    public class UserListData {

    private Page<User> users;
    private ErrorDto errorObj;

}
