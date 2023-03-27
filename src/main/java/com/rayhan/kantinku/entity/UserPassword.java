package com.rayhan.kantinku.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data @Entity
@Table(name = "s_users_passwords")
public class UserPassword {

    @Id @Column(name = "id_user")
    private String id;

    @OneToOne@MapsId
    @JoinColumn(name = "id_user")
    private User user;
    private String password;
}
