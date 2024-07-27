package com.MovieFlix.demo.controller;

import com.MovieFlix.demo.auth.entities.ForgetPassword;
import com.MovieFlix.demo.auth.entities.User;
import com.MovieFlix.demo.auth.repositories.ForgetPasswordRepository;
import com.MovieFlix.demo.auth.repositories.UserRepository;
import com.MovieFlix.demo.auth.utils.ChangePassword;
import com.MovieFlix.demo.dto.MailBody;
import com.MovieFlix.demo.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgot/password")
public class ForgetPasswordController {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ForgetPasswordRepository forgetPasswordRepository;

    private  final PasswordEncoder passwordEncoder;

    public ForgetPasswordController(UserRepository userRepository, EmailService emailService, ForgetPasswordRepository forgetPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgetPasswordRepository = forgetPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!" + email));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request : " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgetPassword fp = ForgetPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 20 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgetPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification!");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp , @PathVariable String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));

        ForgetPassword fp = forgetPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgetPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/changePassword/{email}")
    public  ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword , @PathVariable String email){

        if(!Objects.equals(changePassword.password() , changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again" , HttpStatus.EXPECTATION_FAILED);

        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());

userRepository.updatePassword(email , encodedPassword);
return ResponseEntity.ok("password has been changed");
    }

    //to generate otp
    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000 , 999_999);
    }
}
