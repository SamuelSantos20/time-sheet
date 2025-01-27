package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimesheetData extends JpaRepository<Timesheet, UUID> {

    Optional<Timesheet> findByMonthAndYearAndEmployee_id(int month,int year, Employee employee_id);
    Optional<Timesheet> findByMonthAndYearAndEmployee(int month, int year, Employee employee);

    @Query("SELECT t FROM Timesheet t WHERE t.month = :month AND t.year = :year AND t.employee = :employee")
    List<Timesheet> ListFindByMonthAndEmployee(@Param("month") int month,
                                               @Param("year") int year,
                                               @Param("employee") Employee employee);



}
