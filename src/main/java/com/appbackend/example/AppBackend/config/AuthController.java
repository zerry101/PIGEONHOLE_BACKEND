package com.appbackend.example.AppBackend.config;

import com.appbackend.example.AppBackend.entities.RefreshToken;
import com.appbackend.example.AppBackend.entities.Role;
import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.*;
import com.appbackend.example.AppBackend.repositories.RefreshTokenRepository;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import com.appbackend.example.AppBackend.services.EmailOtpService;
import com.appbackend.example.AppBackend.services.RefreshTokenService;
import com.appbackend.example.AppBackend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.appbackend.example.AppBackend.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


//    @Autowired
//    private RegisterRequest registerRequest;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailOtpService emailOtpService;


    @Autowired
    JwtHelper jwtHelper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private Authentication authentication;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        this.doAuthenticate(request.getEmail(), request.getPassword());

//        Here saveOtp method  of emailOtpService will return an otp string

        String otp = emailOtpService.saveOtp((User) userDetails);
        emailOtpService.sendVerificationOtpEmail(userDetails.getUsername(), otp);


//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//
//        String token = this.jwtHelper.generateToken(userDetails);
//
//
//        JwtResponse response = JwtResponse.builder().jwtToken(token).refreshTokenString(refreshToken.getRefreshTokenString()).username(userDetails.getUsername()).build();


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object user =this.authentication.getName();

//        System.out.println(user);
        return new ResponseEntity<>("OTP HAS BEEN SEND TO "+authentication.getName(), HttpStatus.OK);
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyUserOtp(@RequestBody OtpRequest otpRequest,@CurrentSecurityContext SecurityContext context) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//            Principal principal ;
//
//            System.out.println(principal.getName());
//            System.out.println("This is ");
//            System.out.println(context.getAuthentication().getName());
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(otpRequest.getUserEmail());
            User user=userRepository.findByEmail(userDetails.getUsername()).get();


            emailOtpService.verifyOtp(otpRequest.userEnteredOtp,user);

            System.out.println(emailOtpService.verifyOtp(otpRequest.userEnteredOtp,user));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());


            String token = this.jwtHelper.generateToken(userDetails);


            JwtResponse response = JwtResponse.builder().jwtToken(token).refreshTokenString(refreshToken.getRefreshTokenString()).username(userDetails.getUsername()).build();



            return new ResponseEntity<>(response, HttpStatus.OK);
//            return  new ResponseEntity<>("You are successfully logged in",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    {

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Optional<User> duplicateEmailUser = userService.getUserByEmail(registerRequest.getEmail());
        Optional<User> duplicateIdUser = userService.getUserById(registerRequest.getId());

        try {
            if (duplicateEmailUser.isPresent()) {
                throw new DuplicateUserException("USER WITH THIS EMAIL ID ALREADY EXISTS");
            }

            if (duplicateIdUser.isPresent()) {
                throw new DuplicateUserException("USER WITH THIS  ID ALREADY EXISTS");
            }


            var user = User.builder().id(registerRequest.getId()).firstName(registerRequest.getFirstname()).lastName(registerRequest.getLastname()).email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword())).phoneNumber(registerRequest.getPhoneNumber()).role(Role.valueOf(registerRequest.getRole())).build();

            userRepository.save(user);

//
//            var token = jwtHelper.generateToken(user);


            return ResponseEntity.ok("USER  HAVE BEEN SUCCESSFULLY REGISTERED");

        } catch (DuplicateUserException e) {

            String errorResponse = e.getMessage();
            return ResponseEntity.badRequest().body(errorResponse);

        }


    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshJWTtoken(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshTokenString());

        User user = refreshToken.getUser();

        JwtResponse jwtResponse = JwtResponse.builder().refreshTokenString(refreshToken.getRefreshTokenString()).jwtToken(jwtHelper.generateToken(user)).username(user.getUsername()).build();

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);


    }


    public void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            this.authentication=authentication;

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID USERNAME OR PASSWORD");

        }


    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "CREDENTIALS ARE INVALID";
    }


}
