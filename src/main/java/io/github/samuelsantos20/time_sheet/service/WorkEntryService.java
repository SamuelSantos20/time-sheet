package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.WorkEntryData;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class WorkEntryService {

    private final WorkEntryData workEntryData;



    public WorkEntry workEntrySave(WorkEntry workEntry) {

        return workEntryData.save(workEntry);

    }

    @Transactional(readOnly = true)
    public Optional<WorkEntry> searchByCurrentDayAndUserID(LocalDate localDate,
                                                           Employee employee_id) {
        return workEntryData.findByStart_timeAndEmployee_id(localDate, employee_id);
    }

    @Transactional(readOnly = true)
    public List<WorkEntry> findByStart_timeAndExit_time(LocalDate localDate) {

        return workEntryData.findByStart_timeAndExit_time(localDate);

    }


    public WorkEntry workEntryUpdate(WorkEntry workEntry) {

        return workEntryData.save(workEntry);

    }





}
