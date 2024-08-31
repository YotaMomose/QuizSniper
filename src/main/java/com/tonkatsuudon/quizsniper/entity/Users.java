package com.tonkatsuudon.quizsniper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {
    @Id
    @Column(length = 20)
    private String id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String password;

}


