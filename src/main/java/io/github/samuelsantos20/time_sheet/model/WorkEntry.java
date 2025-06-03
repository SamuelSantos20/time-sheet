package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "work_entry")
@EntityListeners(AuditingEntityListener.class)
public class WorkEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "work_entry_id")
    private UUID workEntryId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheetId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private User userId;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WorkEntry entry = (WorkEntry) o;
        return getWorkEntryId() != null && Objects.equals(getWorkEntryId(), entry.getWorkEntryId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }


    @Override
    public String toString() {
        return "WorkEntry{id=" + workEntryId + ", user=" + userId + ", start=" + startTime + ", end=" + endTime + "}";
    }

}


