package com.salon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private LocalDateTime openingTime;

    @Column(nullable = false)
    private LocalDateTime closingTime;
}
