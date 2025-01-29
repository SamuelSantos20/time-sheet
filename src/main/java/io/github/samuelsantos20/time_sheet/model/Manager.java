package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "manager")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "first_name", nullable = false, unique = false, updatable = true,length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false, updatable = true, unique = false)
    private String lastName;

    @Column(name = "email", length = 200, unique = true, nullable = false, updatable = true)
    private String email;

    @Column(name = "department", nullable = false, updatable = true, length = 200, unique = false)
    private String department;



}
