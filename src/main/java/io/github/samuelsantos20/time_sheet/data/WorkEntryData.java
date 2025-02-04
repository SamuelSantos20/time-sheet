package io.github.samuelsantos20.time_sheet.data;

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

    @Query(value = "SELECT e.* FROM work_entry e WHERE CAST(e.start_time AS DATE) = :date AND e.user_id = :userId", nativeQuery = true)
    Optional<WorkEntry> findByStartTimeAndUserId(@Param("date") LocalDate date, @Param("userId") UUID userId);

    @Query("SELECT w FROM WorkEntry w WHERE CAST(w.startTime AS DATE) = :date AND CAST(w.endTime AS DATE) = :date")
    List<WorkEntry> findByStartTimeAndEndTime(@Param("date") LocalDate date);

}