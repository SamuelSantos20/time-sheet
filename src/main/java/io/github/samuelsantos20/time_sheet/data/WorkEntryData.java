package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkEntryData extends JpaRepository<WorkEntry, UUID> {

    @Query(value = "SELECT e.* FROM WorkEntry e WHERE DATE(e.start_time) =:date AND e.employee_id =:#{#employee_id.id}", nativeQuery = true)
    Optional<WorkEntry> findByStart_timeAndEmployee_id(@Param("date") LocalDate date,@Param("employee_id") Employee employee_id);

   @Query(value = "SELECT e.* FROM WorkEntry e WHERE DATE(e.start_time) =:date AND DATE(e.end_time) =:date", nativeQuery = true)
   List<WorkEntry> findByStart_timeAndExit_time(@Param("date") LocalDate date);

}
