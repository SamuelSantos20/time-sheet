package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerData extends JpaRepository<Manager, UUID> {


    @Query("select m from Manager m where m.email = ?1 and m.firstName = ?2 and m.lastName = ?3 and m.department = ?4")
    Optional<Manager> findByEmailAndFirstNameAndLastNameAndDepartment(String email, String firstName, String lastName, String department);
}
