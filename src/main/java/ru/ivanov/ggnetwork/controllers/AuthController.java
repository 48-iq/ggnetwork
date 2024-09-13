package ru.ivanov.ggnetwork.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.auth.LoginDto;
import ru.ivanov.ggnetwork.dto.auth.RegisterDto;
import ru.ivanov.ggnetwork.entities.Role;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserRepository;
import ru.ivanov.ggnetwork.security.JwtUtils;
import ru.ivanov.ggnetwork.services.ImageService;
import ru.ivanov.ggnetwork.services.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${app.admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Incorrect credentials!");
        }
        String token = jwtUtils.generateToken(userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found!")));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDto) {
        if (registerDto.getRole().equals("ROLE_ADMIN") &&
                !adminPassword.equals(registerDto.getAdminPassword()))
            return ResponseEntity.badRequest().body("incorrect admin password");

        var user = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role(Role.valueOf(registerDto.getRole()))
                .name(registerDto.getName())
                .surname(registerDto.getSurname())
                .status(registerDto.getStatus())
                .build();

        var savedUser = userRepository.saveAndFlush(user);
        var authToken = new UsernamePasswordAuthenticationToken(savedUser.getUsername(), savedUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(savedUser.getRole().name())));
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            SecurityContextHolder.getContext().setAuthentication(authToken);
        var jwtToken = jwtUtils.generateToken(savedUser);
        return ResponseEntity.ok(jwtToken);
    }
}
