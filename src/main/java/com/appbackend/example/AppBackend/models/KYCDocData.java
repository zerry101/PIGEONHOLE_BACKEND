package com.appbackend.example.AppBackend.models;

import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class KYCDocData {
    @Lob
    private byte[] documentData;

    @Lob
    private byte[] userImage;

    @Lob
    private byte[] digitalSignature;


    int docSize;
    int userImgSize;
    int digitalSignSize;

}
