package com.sparta.business.domain.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username must contain only lowercase letters and numbers")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "Nickname is required")
    private String nickname;

    @NotBlank(message = "Address is required")
    private String address;

    /**
     * 1 : CUSTOMER
     * 2 : OWNER
     * 3 : MASTER
     */
    @NotBlank(message = "typeNumber is required")
    private String type;

    private String adminToken = "";
}
