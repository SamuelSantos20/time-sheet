package io.github.samuelsantos20.time_sheet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "manager")
public class Manager implements Serializable {
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Manager manager = (Manager) o;
        return getManagerId() != null && Objects.equals(getManagerId(), manager.getManagerId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
