package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimesheetData extends JpaRepository<Timesheet, UUID> {

    Optional<Timesheet> findByMonthAndYearAndUserId(int month, int year, User userId);

    @Query("SELECT t FROM Timesheet t WHERE t.month = :month AND t.year = :year AND t.userId = :userId")
    List<Timesheet> ListFindByMonthAndUserId(@Param("month") int month,
                                               @Param("year") int year,
                                               @Param("userId") User userId);



}
