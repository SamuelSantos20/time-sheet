package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
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

    @Query(value = "SELECT e.* FROM WorkEntry e WHERE DATE(e.startTime) =:date AND e.userId =:#{#userId.id}", nativeQuery = true)
    Optional<WorkEntry> findByStart_timeAndUser_id(@Param("date") LocalDate date,@Param("userId") User userId);

    @Query("SELECT w FROM WorkEntry w WHERE DATE(w.startTime) = :date AND DATE(w.endTime) = :date")
    List<WorkEntry> findByStart_timeAndExit_time(@Param("date") LocalDate date);

}
