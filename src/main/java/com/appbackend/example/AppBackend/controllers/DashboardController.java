package com.appbackend.example.AppBackend.controllers;


import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.UserListData;
import com.appbackend.example.AppBackend.services.AdminServices.CreditScoreService;
import com.appbackend.example.AppBackend.services.AdminServices.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserDataService userDataService;

    @Autowired
    CreditScoreService creditScoreService;

    @GetMapping("/users")
    public ResponseEntity<UserListData> getUsersData(@RequestParam(defaultValue = "0") Integer pageNo,
                                                     @RequestParam(defaultValue = "50") Integer pageSize,

                                                     @RequestParam(defaultValue = "id") String sortBy) {

//        AuthenticationPrincipal principal = (AuthenticationPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("Principle name is \n\n");
        System.out.println(user.getFirstName());


        UserListData userListData = userDataService.getUsers(pageNo, pageSize, sortBy);

        if (userListData.getErrorObj() != null) {
//            ErrorDto errorDto= ErrorDto.builder().code(400).status("ERROR").message(userListData.getErrorMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userListData);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userListData);
        }


    }

    @PostMapping("/calculate_creditscore")
    public ResponseEntity<?> calculateCreditScore(@RequestBody Map<String , Map<String,Object>> objectMap ){

       creditScoreService.calculateCreditScore(objectMap);


        return null;

    }


}
