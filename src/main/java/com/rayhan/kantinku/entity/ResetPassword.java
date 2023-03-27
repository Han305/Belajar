package com.rayhan.kantinku.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Entity
@Table(name = "reset_password")
public class ResetPassword {
    private static final Integer RESET_PASSWORD_EXPIRY_DAYS = 15;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private LocalDateTime generated = LocalDateTime.now();
    private LocalDateTime expired = LocalDateTime.now().plusDays(RESET_PASSWORD_EXPIRY_DAYS);

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @NotNull @NotEmpty
    private String uniqueCode = UUID.randomUUID().toString();
}
