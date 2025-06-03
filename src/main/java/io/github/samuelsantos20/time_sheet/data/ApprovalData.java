package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalData extends JpaRepository<Approval, UUID> {


    @Query("SELECT a FROM Approval a " +
            "JOIN FETCH a.user u " +
            "LEFT JOIN FETCH u.managers " +
            "WHERE u.id = ?1 AND a.timesheet.timesheetId = ?2")
    Optional<Approval> findByUser_IdAndTimesheet_TimesheetId(UUID id, UUID timesheetId);

    @Query("SELECT a FROM Approval a " +
            "JOIN FETCH a.user u ")
    Optional<Approval> findByUser_Id(UUID id);
}
