package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.WorkEntryData;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class WorkEntryService {

    private final WorkEntryData workEntryData;



    public void workEntrySave(WorkEntry workEntry) {

         workEntryData.save(workEntry);

    }

    @Transactional(readOnly = true)
    public Optional<WorkEntry> searchByCurrentDayAndUserID(LocalDate localDate,
                                                           UUID userId) {
        return workEntryData.findByStartTimeAndUserId(localDate, userId);
    }

    @Transactional(readOnly = true)
    public List<WorkEntry> findByStart_timeAndExit_time(LocalDate localDate) {

        return workEntryData.findByStartTimeAndEndTime(localDate);

    }


    public WorkEntry workEntryUpdate(WorkEntry workEntry) {

        return workEntryData.save(workEntry);

    }

    @Transactional(readOnly = true)
    public List<WorkEntry> workEntryList() {

        return workEntryData.findAll();

    }

    @Transactional(readOnly = true)
    public Optional<WorkEntry> findByEndTime(LocalDate date, UUID userId) {

       return workEntryData.findByEndTime(date, userId);

    }

}
