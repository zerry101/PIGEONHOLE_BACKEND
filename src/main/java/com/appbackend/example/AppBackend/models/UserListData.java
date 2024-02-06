package com.appbackend.example.AppBackend.models;

import com.appbackend.example.AppBackend.entities.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
    public class UserListData {

    private List<User> users;
    private ErrorDto errorObj;

}
