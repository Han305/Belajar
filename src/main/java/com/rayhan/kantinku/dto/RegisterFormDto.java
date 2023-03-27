package com.rayhan.kantinku.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterFormDto {

    @NotNull @NotEmpty @Size(min = 3, max = 255)
    private String name;

    @NotNull @NotEmpty @Email
    @Size(min = 3, max = 255)
    private String email;

    @NotNull @NotEmpty @Size(min = 3, max = 255)
    private String phone;

}
