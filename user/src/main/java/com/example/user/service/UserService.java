    package com.example.user.service;

    import com.example.user.config.JwtService;
    import com.example.user.dto.*;
import com.example.user.event.OtpVerificationEvent;
import com.example.user.exception.EmailAlreadyExistsException;
    import com.example.user.exception.InvalidCredentialsException;
    import com.example.user.model.PendingRegisterUser;
    import com.example.user.model.User;
    import com.example.user.producer.UserEventProducer;
    import com.example.user.repository.PendingRegisterRepository;
    import com.example.user.repository.UserRepository;
    import com.example.user.util.OtpGenerator;

    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
    import java.util.Optional;

    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PendingRegisterRepository pendingRegisterRepository;
        private final PasswordEncoder passwordEncoder;
        private final UserEventProducer userEventProducer;
        private final JwtService jwtService;
        private final RedisTemplate<String,String> redisTemplate;

        public UserService(
                UserRepository userRepository, 
                PendingRegisterRepository pendingRegisterRepository, 
                PasswordEncoder passwordEncoder,
                UserEventProducer userEventProducer,
                JwtService jwtService,
                RedisTemplate<String,String> redisTemplate
            ){
            this.userRepository = userRepository;
            this.pendingRegisterRepository = pendingRegisterRepository;
            this.passwordEncoder = passwordEncoder;
            this.userEventProducer = userEventProducer;
            this.jwtService = jwtService;
            this.redisTemplate = redisTemplate;
        }

 
        public PendingRegisterResponse signUp(UserDTO userDto){
            Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
            if(existingUser.isPresent()){
                throw new EmailAlreadyExistsException("Email already exists");
            }


            PendingRegisterUser user = new PendingRegisterUser();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            LocalDateTime currentDateTime = LocalDateTime.now();
            user.setExpirationTime(currentDateTime.plusMinutes(15));
            pendingRegisterRepository.save(user);

            String otp = OtpGenerator.generateOTP();
            String redisKey = "otp:" + user.getId();
            redisTemplate.opsForValue().set(redisKey, otp, Duration.ofMinutes(15));

            OtpVerificationEvent event = new OtpVerificationEvent(user.getEmail(), otp);

            userEventProducer.publishOtpSent(event);
            
            return new PendingRegisterResponse(
                user.getEmail(),
                "Verification OTP sent"
            );



            // User savedUser = userRepository.save(user);

            // String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getId());

            // UserRegisterEvent event = new UserRegisterEvent(
            //         savedUser.getId(),
            //         savedUser.getEmail(),
            //         savedUser.getName()
            // );
            // userEventProducer.publishUserRegister(event);
            // return new AuthResponse(
            //         token,
            //         savedUser.getId(),
            //         savedUser.getName(),
            //         savedUser.getEmail()
            // );
        }

        public LoginResponse login(LoginDTO loginDTO){
            Optional<User> user = userRepository.findByEmail(loginDTO.getEmail());
            if(user.isEmpty()){
                throw new InvalidCredentialsException("Invalid user credentials");
            }

            String email = user.get().getEmail();
            String hashedPassword = user.get().getPassword();

            if (!passwordEncoder.matches(loginDTO.getPassword(), hashedPassword)) {
                throw new InvalidCredentialsException("Invalid user credentials");
            }

            String token = jwtService.generateToken(user.get().getEmail(), user.get().getId());

            return new LoginResponse(token);
        }
    }
