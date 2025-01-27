package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeData extends JpaRepository<Employee, UUID> {

    @Query("select e from Employee e where e.email = ?1 and e.firstName = ?2 and e.lastName = ?3 and e.registration = ?4")
    Optional<Employee> entitySelection(String email, String firstName, String lastName, String registration);
}
