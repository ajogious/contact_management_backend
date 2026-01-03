package com.a3tech.contact_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_numbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String type;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonBackReference
    private Contact contact;
}
