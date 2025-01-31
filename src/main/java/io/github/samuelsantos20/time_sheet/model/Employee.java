package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID id;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Timesheet> timesheets = new ArrayList<>();


    @Column(name = "first_name", length = 100, nullable = false, updatable = true, unique = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false, updatable = true, unique = false)
    private String lastName;

    @Column(name = "email", length = 200, nullable = false, updatable = true, unique = true)
    private String email;

    @Column(name = "position", length = 200, updatable = false, unique = false,nullable = false)
    private String position;

    @Column(name = "department", length = 200, nullable = false, updatable = false, unique = false)
    private String department;

    @Column(name = "registration", length = 7, nullable = false, unique = true, updatable = false)
    private String registration;






}
