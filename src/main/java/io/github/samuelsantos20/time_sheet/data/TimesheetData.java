package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
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



    //    @Query("select t from Timesheet t where t.day = ?1 and t.month = ?2 and t.year = ?3 and t.userId= ?4")
//    List<Timesheet> findByDayAndMonthAndYearAndUserId_Id(int day, int month, int year, User id);


}
