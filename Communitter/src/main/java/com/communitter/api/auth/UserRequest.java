package com.communitter.api.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;

    private String email;
    private String password;
    private String about;

    private String header;

    private String avatar;
}
