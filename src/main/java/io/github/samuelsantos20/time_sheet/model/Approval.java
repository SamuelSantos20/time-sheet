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
@Table(name = "approval")
@EntityListeners(AuditingEntityListener.class)
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "approvalId")
    private UUID approvalId;

    @ManyToOne
    @JoinColumn(name = "manager")
    private Manager manager;

    @OneToOne
    @JoinColumn(name = "timesheet", nullable = false, unique = true)
    private Timesheet timesheet;

    @Column(name = "approvalStatus", nullable = false, length = 200, updatable = true, unique = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "approvalDateCreated")
    @CreatedDate
    private LocalDateTime approvalDateCreated;

    @Column(name = "approvalDateUpdate")
    @LastModifiedDate
    private LocalDateTime approvalDateUpdate;

    @Column(name = "comments", nullable = false, length = 400, unique = false, updatable = true)
    private String comments;

}
