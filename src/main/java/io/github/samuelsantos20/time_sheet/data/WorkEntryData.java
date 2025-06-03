package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkEntryData extends JpaRepository<WorkEntry, UUID> {

    @Query(value = "SELECT e.* FROM work_entry e WHERE DATE(e.start_time) = :date AND e.user_id = :userId", nativeQuery = true)
    Optional<WorkEntry> findByStartTimeAndUserId(@Param("date") LocalDate date, @Param("userId") UUID userId);

    @Query(value = "SELECT * FROM work_entry WHERE DATE(end_time) = :date AND  user_id = :userId", nativeQuery = true)
    Optional<WorkEntry>  findByEndTimeANDUserId (@Param("date") LocalDate date, @Param("userId") UUID userId);

    @Query(value = "SELECT * FROM work_entry WHERE DATE(start_time) = :date OR DATE(end_time) = :date", nativeQuery = true)
    List<WorkEntry> findByStartTimeAndEndTime(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM work_entry WHERE user_id = :userId", nativeQuery = true)
    List<WorkEntry> findByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * FROM work_entry WHERE start_time BETWEEN :startTime AND :endTime AND user_id = :userId", nativeQuery = true)
    Optional<WorkEntry> findByStartTimeAndEndTimeAndUserIdBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("userId") UUID userId);

    @Query(value = "SELECT e.* FROM work_entry e WHERE DATE(e.start_time) = :date AND e.user_id = :userId", nativeQuery = true)
    Optional<WorkEntry> findByEndTime(@Param("date") LocalDate date, @Param("userId") UUID userId);

    @Query("select w from WorkEntry w where w.timesheetId.userId.id = ?1")
    Optional<WorkEntry> findByTimesheetId_UserId_Id(UUID id);

    @Query(value = "SELECT e.* FROM work_entry e WHERE e.user_id = :userId AND EXTRACT(MONTH FROM e.start_time) = :month AND EXTRACT(YEAR FROM e.start_time) = :year", nativeQuery = true)
    List<WorkEntry> findByUserIdAndMonthAndYear(UUID userId, int month, int year);
    
    

}