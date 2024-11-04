package com.collins.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "admin_id")
public class Admin extends User{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
