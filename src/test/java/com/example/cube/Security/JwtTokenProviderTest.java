package com.example.cube.Security;

import com.example.cube.Exception.CubeAPIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class JwtTokenProviderTest {

    @Test
    void testValidateToken_ExpiredToken() {
        // Arrange
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = "expired_token";

        // Act and Assert
        Assertions.assertThrows(CubeAPIException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_MalformedToken() {
        // Arrange
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = "malformed_token";

        // Act and Assert
        Assertions.assertThrows(CubeAPIException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_UnsupportedToken() {
        // Arrange
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = "unsupported_token";

        // Act and Assert
        Assertions.assertThrows(CubeAPIException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_EmptyClaimsString() {
        // Arrange
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = "empty_claims_token";

        // Act and Assert
        Assertions.assertThrows(CubeAPIException.class, () -> jwtTokenProvider.validateToken(token));
    }

}
