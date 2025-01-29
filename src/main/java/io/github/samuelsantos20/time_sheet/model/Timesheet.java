package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "timesheet")
@EntityListeners(AuditingEntityListener.class)
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timesheet_id")
    private UUID timesheetId;

    @ManyToOne
    @JoinColumn(name = "employee")
    private Employee employee;

    @OneToMany(mappedBy = "timesheetId", cascade = CascadeType.PERSIST)
    private List<WorkEntry> workEntries;

    @OneToOne(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "approval")
    private Approval approval;

    @Column(name = "timesheet_month")
    private int month;

    @Column(name = "timesheet_year")
    private int year;

    @Column(name =" time_sheet_update")
    @LastModifiedDate
    private LocalDateTime timeSheetUpdate;

    @CreatedDate
    @Column(name = "time_sheet_created")
    private LocalDateTime timeSheetCreated;

    @Column(name = "total_hours", nullable = false, updatable = true, unique = false)
    private LocalTime totalHours;


}
