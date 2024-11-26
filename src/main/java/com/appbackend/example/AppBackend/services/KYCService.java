package com.appbackend.example.AppBackend.services;


import com.appbackend.example.AppBackend.entities.KYC;
import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.KYCDataResDto;
//import com.appbackend.example.AppBackend.models.KYCDto;

import com.appbackend.example.AppBackend.models.KYCDocData;
import com.appbackend.example.AppBackend.repositories.KYCRepository;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import com.appbackend.example.AppBackend.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service
public class KYCService {


    @Autowired
    private KYCRepository kycRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public KYCDataResDto saveUserKYC(KYCDataResDto kycRequest, MultipartFile documentData, MultipartFile userImage) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //        if some of kyc details exists then update only those kyc details which are remaining and are provide by client side

        KYC existingKyc = kycRepository.findKYCById((((User) authentication.getPrincipal()).getId()));


        if (existingKyc != null) {

            if (kycRequest.getDob() != null && existingKyc.getDob() == null) {
                existingKyc.setDob(kycRequest.getDob());
            }
            if (kycRequest.getAddress() != null && existingKyc.getAddress() == null) {
                existingKyc.setAddress(kycRequest.getAddress());
            }
            if (kycRequest.getMaritalStatus() != null && existingKyc.getMaritalStatus() == null) {
                existingKyc.setMaritalStatus(kycRequest.getMaritalStatus());
            }
            if (kycRequest.getKin() != null && existingKyc.getKin() == null) {
                existingKyc.setKin(kycRequest.getKin());
            }
            if (kycRequest.getNationalId() != null && existingKyc.getNationalId() == null) {
                existingKyc.setNationalId(kycRequest.getNationalId());
            }

            if (documentData != null && existingKyc.getDocumentData() == null) {
                existingKyc.setDocumentData(ImageUtils.compressImage(documentData.getBytes()));
            }

            if (userImage != null && existingKyc.getUserImage() == null) {
                existingKyc.setUserImage(ImageUtils.compressImage(userImage.getBytes()));
            }

            kycRepository.save(existingKyc);

            assert documentData != null;
            assert userImage != null;
            KYCDataResDto kycResponse = KYCDataResDto.builder()
                    .workId(existingKyc.getId())
                    .dob(existingKyc.getDob())
                    .phoneNumber(existingKyc.getUser().getPhoneNumber())
                    .firstName(existingKyc.getUser().getFirstName())
                    .lastName(existingKyc.getUser().getLastName())
                    .nationalId(existingKyc.getNationalId())
                    .kin(existingKyc.getKin())
                    .address(existingKyc.getAddress())
                    .email(existingKyc.getUser().getUsername())
                    .isDocumentDataSubmitted(existingKyc.getDocumentData() != null)
                    .isUserImageSubmitted(existingKyc.getUserImage() != null)
                    .build();
            return kycResponse;

        }

        assert documentData != null;
        assert userImage != null;


//            if user is first time filling the kyc form then first build kyc object then save in db
        KYC kyc = KYC.builder()
                .id(user.getId())
                .user(user)
                .dob(kycRequest.getDob())
                .address(kycRequest.getAddress())
                .maritalStatus(kycRequest.getMaritalStatus())
                .kin(kycRequest.getKin())
                .nationalId(kycRequest.getNationalId())
                .build();

        if (userImage != null) {
            kyc.setUserImage(ImageUtils.compressImage(userImage.getBytes()));
        }
        if (documentData != null) {
            kyc.setDocumentData(ImageUtils.compressImage(documentData.getBytes()));
        }

//        save kyc in db
        kycRepository.save(kyc);


//        build kyc first time response for first time registration
        KYCDataResDto kycFirstTimeResponse = KYCDataResDto.builder()
                .workId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getUsername())
                .dob(kyc.getDob())
                .address(kyc.getAddress())
                .maritalStatus(kyc.getMaritalStatus())
                .kin(kyc.getKin())
                .nationalId(kyc.getNationalId())
                .isDocumentDataSubmitted(kyc.getDocumentData() != null)
                .isUserImageSubmitted(kyc.getUserImage() != null)
                .build();

        return kycFirstTimeResponse;


    }

    private static byte[] docToByte(byte[] document) {
        byte[] imageBytes = ImageUtils.decompressImage(document);
        return imageBytes;
    }

    @Transactional
    public Object getUserKYCDocDataById(Integer id) {
        KYC kycData = kycRepository.findKYCById(id);

        if(kycData !=null )
        {

            KYCDocData kycDocumentData = new KYCDocData().builder()
                    .documentData(docToByte(kycData.getDocumentData()))
                    .userImage(docToByte(kycData.getUserImage()))
                    .docSize(docToByte(kycData.getDocumentData()).length)
                    .userImgSize(docToByte(kycData.getUserImage()).length)
                    .build();
            return kycDocumentData;




        }
        else{
            return "KYC data with id " + id + " not found";
        }

    }


    //    The getUserKYCDataById wil only fetch user data not documents or images
    @Transactional
    public Optional<KYCDataResDto> getUserKYCDataById(Integer id, Authentication authentication) {

        //get KYC user details if present kycid exists in KYC db
        if (kycRepository.findKYCById(id)!=null) {


            KYC kyc = kycRepository.findKYCById(id);

            KYCDataResDto kycResponse = KYCDataResDto.builder()
                    .email(kyc.getUser().getUsername())
                    .address(kyc.getAddress())
                    .maritalStatus(kyc.getMaritalStatus())
                    .kin(kyc.getKin())
                    .nationalId(kyc.getNationalId())
                    .dob(kyc.getDob())
                    .phoneNumber(kyc.getUser().getPhoneNumber())
                    .firstName(kyc.getUser().getFirstName())
                    .lastName(kyc.getUser().getLastName())
                    .workId(kyc.getId())
                    .isUserImageSubmitted(ImageUtils.decompressImage(kyc.getUserImage()) != null)
                    .isDocumentDataSubmitted(ImageUtils.decompressImage(kyc.getDocumentData()) != null)
                    .build();

            return Optional.ofNullable(kycResponse);

        }
        //  else get current user details for form field data binding
        else {

            User kycUser = (User) authentication.getPrincipal();

            //            User kycUser = userRepository.getReferenceById(id);

            KYCDataResDto kycResponse = KYCDataResDto.builder()
                    .firstName(kycUser.getFirstName())
                    .lastName(kycUser.getLastName())
                    .phoneNumber(kycUser.getPhoneNumber())
                    .workId(kycUser.getId())
                    .email(kycUser.getUsername())
                    .build();

            return Optional.ofNullable(kycResponse);
        }


    }


}
