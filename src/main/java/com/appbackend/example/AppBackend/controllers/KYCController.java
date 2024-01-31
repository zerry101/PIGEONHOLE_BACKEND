package com.appbackend.example.AppBackend.controllers;


import com.appbackend.example.AppBackend.entities.KYC;
import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.KYCDataResDto;
//import com.appbackend.example.AppBackend.models.KYCDto;

import com.appbackend.example.AppBackend.models.KYCDocData;
import com.appbackend.example.AppBackend.repositories.KYCRepository;
import com.appbackend.example.AppBackend.services.KYCService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping(value = "/KYC")
public class KYCController {

    @Autowired
    KYCRepository kycRepository;
    @Autowired
    private KYCService kycService;

    @GetMapping(value = "/docData" )
    public ResponseEntity<?> getKYCDocData(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        KYCDocData kycDocData = kycService.getUserKYCDocDataById(user.getId());

        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                    .body(kycDocData);

    }

    @GetMapping("/data")
    public ResponseEntity<KYCDataResDto> getKYCData(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

//        kycService.getUserKYCById(user.getId());


        return new ResponseEntity<>(kycService.getUserKYCDataById(user.getId(), authentication)
                .orElseThrow(() -> new RuntimeException("KYC OF USER NOT FOUND")), HttpStatus.OK);
//
    }


    @PostMapping(value = "/submitData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KYCDataResDto> kycSubmit(@RequestParam(value = "kycRequest", required = false) String kycRequestString,
                                                   @RequestParam(value = "documentData", required = false) MultipartFile documentData,
                                                   @RequestParam(value = "userImage", required = false) MultipartFile userImage) throws IOException {


        ObjectMapper mapper = new ObjectMapper();

        KYCDataResDto kycRequest = mapper.readValue(kycRequestString, KYCDataResDto.class);

        KYCDataResDto kycResponse = kycService.saveUserKYC(kycRequest, documentData, userImage);

        return new ResponseEntity<>(kycResponse, HttpStatus.OK);


    }

}
