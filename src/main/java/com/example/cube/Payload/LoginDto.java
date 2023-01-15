package com.example.cube.Payload;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String userEmail;
    private String password;
}
