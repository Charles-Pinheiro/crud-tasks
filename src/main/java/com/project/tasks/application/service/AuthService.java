package com.project.tasks.application.service;

import com.project.tasks.application.exception.BusinessException;
import com.project.tasks.application.exception.ErrorConstants;
import com.project.tasks.domain.enumeration.UserRole;
import com.project.tasks.domain.model.User;
import com.project.tasks.domain.repository.UserRepository;
import com.project.tasks.infrastructure.security.JwtService;
import com.project.tasks.infrastructure.web.dto.auth.AuthResponseDTO;
import com.project.tasks.infrastructure.web.dto.auth.LoginRequestDTO;
import com.project.tasks.infrastructure.web.dto.auth.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        BusinessException.throwIf(
                userRepository.existsByEmail(request.email()),
                ErrorConstants.EMAIL_ALREADY_EXISTS,
                HttpStatus.BAD_REQUEST
        );

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.ROLE_USER)
                .build();

        user = userRepository.save(user);

        String jwt = jwtService.generateToken(user);

        return new AuthResponseDTO(
                jwt,
                expirationMs,
                user.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(BusinessException.notFound(User.class));

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(
                token,
                expirationMs,
                user.getRole().name()
        );
    }
}
