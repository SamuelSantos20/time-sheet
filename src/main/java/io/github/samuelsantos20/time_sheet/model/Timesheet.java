package io.github.samuelsantos20.time_sheet.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timesheet")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"workEntries"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "timesheetId")
public class Timesheet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "timesheet_id")
    private UUID timesheetId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", nullable = true)
    private User userId;

    @OneToMany(mappedBy = "timesheetId", cascade = CascadeType.ALL)
    private List<WorkEntry> workEntries;

    @OneToOne(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Approval approval;

    @Column(name = "day_in_month", nullable = false, updatable = true, unique = false)
    private int dayInMonth;

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
    private Duration totalHours;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Timesheet timesheet = (Timesheet) o;
        return getTimesheetId() != null && Objects.equals(getTimesheetId(), timesheet.getTimesheetId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}