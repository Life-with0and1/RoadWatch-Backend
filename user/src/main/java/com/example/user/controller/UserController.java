package com.example.user.controller;

import com.example.user.dto.AuthResponse;
import com.example.user.dto.LoginDTO;
import com.example.user.dto.LoginResponse;
import com.example.user.dto.PendingRegisterResponse;
import com.example.user.dto.UserDTO;
import com.example.user.dto.VerificationDTO;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<PendingRegisterResponse> signUp(@Valid @RequestBody UserDTO userDTO) {
        PendingRegisterResponse response = userService.signUp(userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyUser(@Valid @RequestBody VerificationDTO request){
        AuthResponse response= userService.verifyUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponse response = userService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public String login() {
        return "Ho";
    }

}
