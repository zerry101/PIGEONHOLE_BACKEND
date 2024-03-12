package com.appbackend.example.AppBackend.controllers;


import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.PaginationModel.Filter;
import com.appbackend.example.AppBackend.models.PaginationModel.SearchInfo;
import com.appbackend.example.AppBackend.models.PaginationModel.SortField;
import com.appbackend.example.AppBackend.models.UserListData;
import com.appbackend.example.AppBackend.services.AdminServices.CreditScoreService;
import com.appbackend.example.AppBackend.services.AdminServices.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserDataService userDataService;

    @Autowired
    CreditScoreService creditScoreService;

    @PostMapping("/users")
    public ResponseEntity<?> getUsersData(@RequestBody Map<String, Object> searchInfo) {



//        AuthenticationPrincipal principal = (AuthenticationPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("Principle name is \n\n");
        System.out.println(user.getFirstName());

        List<SortField> sortFields = (List<SortField>) searchInfo.get("sortFields");
        List<Filter> filters=(List<Filter>) searchInfo.get("filters");

//        System.out.println();
        System.out.println(sortFields);
        System.out.println(filters);

//        UserListData userListData = userDataService.getUsers(searchInfo.getPageOffset(), searchInfo.getPageSize(), sortFields.sortField);
//
//        if (userListData.getErrorObj() != null) {
////            ErrorDto errorDto= ErrorDto.builder().code(400).status("ERROR").message(userListData.getErrorMessage()).build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userListData);
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(userListData);
//        }
        return null;

    }

    @PostMapping("/calculate_creditscore")
    public ResponseEntity<?> calculateCreditScore(@RequestBody Map<String, Map<String, Object>> objectMap) {

        creditScoreService.calculateCreditScore(objectMap);


        return null;

    }


}
