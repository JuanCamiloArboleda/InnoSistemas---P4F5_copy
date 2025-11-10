package com.innosistemas.controller;

import com.innosistemas.security.JwtUtils;
import com.innosistemas.dto.JwtResponse;
import com.innosistemas.dto.LoginRequest;
import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    Authentication authentication;
    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUserSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo("user@test.com");
        loginRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@test.com");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        Usuario usuario = new Usuario();
        usuario.setNombres("Juan");
        usuario.setApellidos("Perez");
        usuario.setRol("ADMIN");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));

        JwtResponse response = authController.authenticateUser(loginRequest);
        assertEquals("jwt-token", response.getToken());
        assertEquals("user@test.com", response.getCorreo());
        assertEquals("Juan", response.getNombres());
        assertEquals("Perez", response.getApellidos());
        assertEquals("ADMIN", response.getRol());
    }

    @Test
    void testAuthenticateUserUsuarioNoExiste() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo("noexiste@test.com");
        loginRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("noexiste@test.com");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(java.util.Optional.empty());

        JwtResponse response = authController.authenticateUser(loginRequest);
        assertEquals("jwt-token", response.getToken());
        assertEquals("noexiste@test.com", response.getCorreo());
        assertEquals("", response.getNombres());
        assertEquals("", response.getApellidos());
        assertEquals("", response.getRol());
    }

    @Test
    void testLogoutCurrentUser() {
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
    authController.logoutCurrentUser(authentication);
    verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testLogoutCurrentUserNullAuthentication() {
        authController.logoutCurrentUser(null);
        verify(usuarioRepository, never()).save(any());
    }
}
