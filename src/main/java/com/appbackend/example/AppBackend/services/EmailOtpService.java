package com.appbackend.example.AppBackend.services;

import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;


@Service
public class EmailOtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    private AuthenticationEntryPoint authenticationEntryPoint;


//    private User user;


    public String saveOtp(User user) {
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(Instant.now());
        userRepository.save(user);
        return otp;
    }

    public void sendVerificationOtpEmail(String email, String otp) {
        String subject = "Email Verification";
        String body = "your verification otp is" + otp;
        sendEmail(email, subject, body);
    }


    public String verifyOtp(String reqUserOtp, User savedUser) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        UserDetails userDetails = userDetailsService.loadUserByUsername(otpRequest.getUserEmail());


//        System.out.println(reqUserEmail.equals(authentication.getName()));
//        System.out.println("reqUserEmail"+reqUserEmail);
//        System.out.println("authuser"+authentication.getName());

//        if(reqUserEmail.equals(savedUser.getUsername())){

//            User savedUser=userRepository.findByEmail(reqUserEmail).get();

        if (Duration.between(savedUser.getOtpGeneratedTime(), Instant.now()).getSeconds() < 60) {
            if (reqUserOtp.equals(savedUser.getOtp())) {
                savedUser.setLoginTimeStamp(Instant.now());
                return "you are logged in successfully";
            } else {
                throw new RuntimeException("otp is Invalid ");
            }
        } else {
            throw new RuntimeException("your one-time password (otp) has expired");
        }

//        }
//        else {
//            throw new RuntimeException("The user email is not found");
//        }

    }

    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int otpValue = 1000 + secureRandom.nextInt(8999);
        return String.valueOf(otpValue);
    }


    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom("190320107124.ce.zishan.shaikh@gmail.com");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            e.getMessage();
            throw new RuntimeException(e);

        }
    }


}
