package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "work_entry")
@EntityListeners(AuditingEntityListener.class)
public class WorkEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "work_entry_id")
    private UUID workEntryId;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheetId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

    @Column(name = "start_time", unique = false, updatable = true)
    private LocalDateTime startTime;

    @Column(name = "end_time", unique = false, updatable = true)
    private LocalDateTime endTime;

    @Column(name = "date_created")
    @CreatedDate
    private LocalDateTime dateCreated;

    @Column(name = "date_update")
    @LastModifiedDate
    private LocalDateTime dateUpdate;

}


