package com.example.cube.Security;

import com.example.cube.Model.Role;
import com.example.cube.Model.User;
import com.example.cube.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setPassword("password");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(Collections.singleton(role));

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        // Assert
        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());

        Set<SimpleGrantedAuthority> expectedAuthorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        Assertions.assertEquals(expectedAuthorities, userDetails.getAuthorities());

        Mockito.verify(userRepository).findByEmail(userEmail);
    }

    @Test
    void testLoadUserByUsername_UsernameNotFoundException() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(userEmail);
        });

        Mockito.verify(userRepository).findByEmail(userEmail);
    }

}
