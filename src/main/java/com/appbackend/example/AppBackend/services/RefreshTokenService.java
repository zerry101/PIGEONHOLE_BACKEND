package com.appbackend.example.AppBackend.services;

import com.appbackend.example.AppBackend.entities.RefreshToken;
import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.repositories.RefreshTokenRepository;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenService {

    public long refreshTokenValidity = 24 * 60 * 60 * 1000;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    public RefreshToken createRefreshToken(String userName) {

        User user = userRepository.findByEmail(userName).get();

        RefreshToken refreshTokenOfUser = user.getRefreshToken();


        if (refreshTokenOfUser == null) {
            refreshTokenOfUser = RefreshToken.builder()
                    .refreshTokenString(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user).build();


        } else {
            refreshTokenOfUser.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        refreshTokenRepository.save(refreshTokenOfUser);

        return refreshTokenOfUser;


    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshTokenString(refreshToken)
                .orElseThrow(() -> new RuntimeException("Given token does not exist"));

        if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshTokenOb);
        }

        return refreshTokenOb;


    }


}
