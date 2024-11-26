package com.appbackend.example.AppBackend.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class KYCDataResDto {
    private Integer workId;
    //    firstname will be avaialble from current authentication principal
    private String firstName;
    //    lastname will be avaialble from current authentication principal
    private String lastName;
    private String email;
    private String dob;
    private String address;
    private String phoneNumber;
    private String maritalStatus;
    private String kin;
    private String nationalId;
    private boolean isDocumentDataSubmitted;
    private boolean isUserImageSubmitted;
    //    @Lob

}
