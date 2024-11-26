package com.appbackend.example.AppBackend.repositories;

import com.appbackend.example.AppBackend.entities.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken,String> {


    Optional<RefreshToken> findByRefreshTokenString(String refreshTokenString);

//    Optional<RefreshToken> findByuser(Integer id);
//    Custom methods


}
