package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.data.WorkEntryData;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.validation.TimesheetValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class TimesheetService {

    private final TimesheetData timesheetData;

    private final WorkEntryData workEntryData;

    private final EntityManager entityManager;

    @CacheEvict(value = "timesheetCache", allEntries = true)
    public  void timesheetSave(Timesheet timesheet) {

        LocalDate localDate = LocalDate.now();

        Timesheet timesheet1 = timesheetData.save(timesheet);

        Timesheet merge = entityManager.merge(timesheet1);

        Optional<WorkEntry> byUserIdIdAndTimesheetIdMonthAndTimesheetIdYear = Optional.ofNullable(workEntryData.findByEndTime(
                localDate,
                merge.getUserId().getId())
                .orElseThrow(() -> new EntityNotFoundException("WorkEntry não Localizado!")));



        WorkEntry entry = byUserIdIdAndTimesheetIdMonthAndTimesheetIdYear.get();

        entry.setTimesheetId(merge);

        workEntryData.save(entry);
    }

    @Cacheable(value = "timesheetCache")
    public Optional<Timesheet>timesheetSearch(UUID id) {

        return Optional.ofNullable(timesheetData.findById(id).orElseThrow(() -> new IllegalArgumentException("O valor digitado não foi localizado!")));
    }

    @Cacheable(value = "timesheetCache")
    public List<Timesheet> TimesheetList(UUID id) {

        return timesheetData.findByUserId_Id(id);

    }

    @Cacheable(value = "timesheetCache")
    public Optional<Timesheet> timesheetSearchMonthAndEmployee_id(int month,
                                                                  int year,
                                                                  User userId) {
        return timesheetData.findByMonthAndYearAndUserId(month, year, userId);
    }
    @CacheEvict(value = "timesheetCache", allEntries = true)
    public void timesheetUpdate(Timesheet timesheet) {

        timesheetData.save(timesheet);
    }

    @CacheEvict(value = "timesheetCache", allEntries = true)
    public  void timesheetDelete(UUID id) {
        if (id == null){

            throw new IllegalArgumentException("O valor digitado é nulo");
        }

        timesheetData.deleteById(id);
    }

}
