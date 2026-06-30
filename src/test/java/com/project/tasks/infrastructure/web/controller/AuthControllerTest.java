package com.project.tasks.infrastructure.web.controller;

import com.project.tasks.application.service.AuthService;
import com.project.tasks.infrastructure.web.dto.auth.AuthResponseDTO;
import com.project.tasks.infrastructure.web.dto.auth.LoginRequestDTO;
import com.project.tasks.infrastructure.web.dto.auth.RegisterRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService service;

    @InjectMocks
    private AuthController controller;

    @Test
    void shouldRegister() {
        RegisterRequestDTO registerDto = new RegisterRequestDTO(
                "name",
                "email@mail.com",
                "123456789"
        );

        AuthResponseDTO authDto = new AuthResponseDTO(
                "token1234",
                3600L,
                "ROLE_USER"
        );

        Mockito.when(service.register(registerDto))
                .thenReturn(authDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(201);

        ResponseEntity<AuthResponseDTO> response = controller.register(registerDto);

        Assertions.assertEquals(authDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).register(registerDto);
    }

    @Test
    void shouldLogin() {
        LoginRequestDTO loginDto = new LoginRequestDTO(
                "email@mail.com",
                "123456789"
        );

        AuthResponseDTO authDto = new AuthResponseDTO(
                "token1234",
                3600L,
                "ROLE_USER"
        );

        Mockito.when(service.login(loginDto))
                .thenReturn(authDto);

        HttpStatusCode statusCodeExpected = HttpStatusCode.valueOf(200);

        ResponseEntity<AuthResponseDTO> response = controller.login(loginDto);

        Assertions.assertEquals(authDto, response.getBody());
        Assertions.assertEquals(statusCodeExpected, response.getStatusCode());

        Mockito.verify(service).login(loginDto);
    }
}
