package com.project.tasks.infrastructure.web.controller;

import com.project.tasks.application.service.AuthService;
import com.project.tasks.infrastructure.web.dto.auth.AuthResponseDTO;
import com.project.tasks.infrastructure.web.dto.auth.LoginRequestDTO;
import com.project.tasks.infrastructure.web.dto.auth.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid RegisterRequestDTO request
    ) throws URISyntaxException {
        AuthResponseDTO response = service.register(request);

        return ResponseEntity.created(new URI("/register/")).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request
    ) {
        AuthResponseDTO response = service.login(request);

        return ResponseEntity.ok(response);
    }
}
