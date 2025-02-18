package io.github.samuelsantos20.time_sheet.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "approvalId")
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "approval_id")
    private UUID approvalId;

    @ManyToOne
    @JoinColumn(name = "user_manager")
    private User user;

    @OneToOne
    @JoinColumn(name = "timesheet", nullable = false, unique = true)
    private Timesheet timesheet;

    @Column(name = "approval_status", nullable = false, length = 200, updatable = true, unique = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "approval_date_created")
    @CreatedDate
    private LocalDateTime approvalDateCreated;

    @Column(name = "approval_date_update")
    @LastModifiedDate
    private LocalDateTime approvalDateUpdate;

    @Column(name = "comments", nullable = false, length = 400, unique = false, updatable = true)
    private String comments;

}
