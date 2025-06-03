package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.WorkEntryData;
import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.util.TimesheetProcess;
import io.github.samuelsantos20.time_sheet.validation.WorEntryValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
@Slf4j
public class WorkEntryService {

    private final WorkEntryData workEntryData;

    private final TimesheetProcess timesheetTimesheetProcess;

    private final UserService userService;

    private final WorEntryValidation worEntryValidation;

    @CacheEvict(value = "workEntryCache", allEntries = true)
    public void workEntrySaveEntry(WorkEntry workEntry) {
        LocalDate dayNow = LocalDate.now();

        Optional<WorkEntry> workEntry2 = workEntryData.findByStartTimeAndUserId(dayNow, workEntry.getUserId().getId());

        workEntry2.ifPresentOrElse(existingEntry -> {
            log.error("Já consta registro de entrada para o dia de hoje! Usuario: {}", existingEntry.getUserId().getId());
            throw new OperationNotPermitted("Já consta registro de entrada para o dia de hoje!");
        }, () -> {
            LocalDateTime dayTimeNow = LocalDateTime.now();


            Optional<User> userOptional = userService.findByUserId(workEntry.getUserId().getId());

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                WorkEntry entry = new WorkEntry();
                entry.setStartTime(dayTimeNow);
                entry.setUserId(user);

                log.info("Salvando entrada de trabalho para usuário: {}", user.getId());

                worEntryValidation.validateEntry(entry);
                workEntryData.save(entry);
            } else {
                log.error("Usuário não encontrado para ID: {}", workEntry.getUserId().getId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
            }
        });
    }

    @CacheEvict(value = "workEntryCache", allEntries = true)
    public void workEntrySaveExit(WorkEntry workEntry1) {
        log.info("Registrando saída para o funcionário: {}", workEntry1.getUserId().getId());

        LocalDate exitNow = LocalDate.now();
        Optional<WorkEntry> exitOptional = workEntryData.findByStartTimeAndUserId(exitNow, workEntry1.getUserId().getId());

        if (exitOptional.isEmpty()) {
            log.info("Entrada ainda não marcada na data atual para usuário ID: {}", workEntry1.getUserId().getId());
            throw new OperationNotPermitted("Entrada ainda não marcada na data atual.");
        }

        WorkEntry workEntry = exitOptional.get();
        if (workEntry.getEndTime() != null) {
            log.error("Já consta saída marcada na presente data para usuário ID: {}", workEntry.getUserId().getId());
            throw new OperationNotPermitted("Já consta saída marcada na presente data.");
        }

        try {
            LocalDateTime exitTimeNow = LocalDateTime.now();
            workEntry.setEndTime(exitTimeNow);
            WorkEntry updatedEntry = workEntryData.save(workEntry);

            Duration duration = Duration.between(updatedEntry.getStartTime(), updatedEntry.getEndTime());
            if (duration.isNegative()) {
                log.error("Horário de saída anterior ao horário de entrada para usuário ID: {}", workEntry.getUserId().getId());
                throw new OperationNotPermitted("Horário de saída não pode ser anterior ao de entrada.");
            }

            int totHour = (int) duration.toHours();
            int totMinute = (int) (duration.toMinutes() % 60);
            log.info("Tempo trabalhado: {} horas e {} minutos", totHour, totMinute);

            if (totHour > 10) {
                log.warn("Horas extras detectadas: {} horas e {} minutos", totHour, totMinute);
            }

            Timesheet timesheet = new Timesheet();
            LocalDate localDate = LocalDate.now();
            Duration totalWorkedTime = Duration.ofHours(totHour).plusMinutes(totMinute);
            timesheet.setTotalHours(totalWorkedTime);
            timesheet.setUserId(updatedEntry.getUserId());
            timesheet.setYear(localDate.getYear());
            timesheet.setMonth(localDate.getMonth().getValue());

            timesheetTimesheetProcess.process(timesheet);
        } catch (Exception e) {
            log.error("Erro ao processar saída para usuário ID: {}: {}", workEntry.getUserId().getId(), e.getMessage(), e);
            throw new RuntimeException("Erro ao processar saída: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public Optional<WorkEntry> searchByCurrentDayAndUserID(LocalDate localDate,
                                                           UUID userId) {
        return workEntryData.findByStartTimeAndUserId(localDate, userId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public List<WorkEntry> findByStart_timeAndExit_time(LocalDate localDate) {

        return workEntryData.findByStartTimeAndEndTime(localDate);

    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public List<WorkEntry> workEntryUserSearch(UUID id) {

        return workEntryData.findByUserId(id);
    }


    @CacheEvict(value = "workEntryCache", allEntries = true)
    public WorkEntry workEntryUpdate(WorkEntry workEntry) {

        return workEntryData.save(workEntry);

    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public List<WorkEntry> workEntryList() {

        return workEntryData.findAll();

    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public Optional<WorkEntry> findByEndTime(LocalDate date, UUID userId) {

       return workEntryData.findByEndTime(date, userId);

    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public Optional<WorkEntry> existsByStartTimeAndEndTime(LocalDateTime startTime,LocalDateTime endTime,UUID userId) {
        return workEntryData.findByStartTimeAndEndTimeAndUserIdBetween(startTime, endTime, userId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public Optional<WorkEntry> BuscarFimUsuario( LocalDate date, UUID userId) {
        return workEntryData.findByEndTime(date, userId);
    }

    /**
     * Finds all work entries for a specific user within a given month and year
     *
     * @param userId The ID of the user
     * @param month  The month to search for
     * @param year   The year to search for
     * @return List of work entries matching the criteria
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "workEntryCache")
    public List<WorkEntry> findByUserIdAndMonthAndYear(UUID userId, int month, int year) {
        return workEntryData.findByUserIdAndMonthAndYear(userId, month, year);
    }
}
