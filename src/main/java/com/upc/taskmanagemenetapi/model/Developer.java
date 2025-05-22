package com.upc.taskmanagemenetapi.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "developer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;


}
