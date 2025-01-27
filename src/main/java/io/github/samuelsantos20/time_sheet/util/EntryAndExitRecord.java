package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.service.WorkEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class EntryAndExitRecord {

    private final WorkEntryService workEntryService;

    private final TimesheetProcess timesheetTimesheetProcess;

    private final TimesheetData timesheetData;

    public void Entry(Employee id) {

        LocalDate dayNow = LocalDate.now();

        Optional<WorkEntry> workEntry = workEntryService.searchByCurrentDayAndUserID(dayNow, id);

        workEntry.ifPresentOrElse(workEntry1 -> {

            throw new OperationNotPermitted("Já consta entrada reistrada no dia de Hoje!");

        }, () -> {

            LocalDateTime dayTimeNow = LocalDateTime.now();

            workEntry.get().setStartTime(dayTimeNow);
            workEntry.get().setEmployeeId(id);
            workEntryService.workEntrySave(workEntry.get());

        });

    }


    public void Exit(Employee employee_id) {

        LocalDate exitNow = LocalDate.now();

        Optional<WorkEntry> exitOptional = workEntryService.searchByCurrentDayAndUserID(exitNow, employee_id);

        exitOptional.ifPresentOrElse(workEntry -> {

            LocalDateTime exitTimeNow = LocalDateTime.now();

            workEntry.setEndTime(exitTimeNow);

            WorkEntry exit = workEntryService.workEntryUpdate(workEntry);


            Duration duration = Duration.between(exit.getStartTime(), exit.getEndTime());

            int totHour = (int) duration.toHours();

            int totMinute = (int) (duration.toMinutes() % 60);

            if (totHour > 10) {

                totHour = -totHour;

                totMinute = -totMinute;

            }

            Timesheet timesheet = new Timesheet();
            LocalTime totalWorkedTime = LocalTime.of(totHour, totMinute);

            timesheet.setTotalHours(totalWorkedTime);
            timesheet.setEmployee(exit.getEmployeeId());

            timesheetTimesheetProcess.process(timesheet);

        }, () -> {
            log.info("Entrada ainda não marcada na data atual.");
            Entry(employee_id);

        });


    }

    /**
     * 0: Segundo (0 segundo).
     * 0: Minuto (0 minuto).
     * 23: Hora (23 horas, ou seja, 11 PM).
     * *: Todos os dias do mês.
     * *: Todos os meses.
     */
    @Scheduled(cron = "0 0 23 * * ?")// Executa todos os dias às 23:00 (11 PM)
    private void Teste() {


        log.info("Tarefa agendada executada às 23:00 para ajustar horas trabalhadas.");


        LocalDate localDate = LocalDate.now();

        log.info("Executando tarefa agenda para:  {}", localDate);

        List<WorkEntry> workEntries = workEntryService.findByStart_timeAndExit_time(localDate);

        log.info("Total de registros para serem analisados: {}", workEntries.size());

        workEntries.forEach(workEntry -> {

            if (Objects.isNull(workEntry.getStartTime()) || Objects.isNull(workEntry.getEndTime())) {

                LocalDateTime now = LocalDateTime.now();

                timesheetData.ListFindByMonthAndEmployee(now.getMonth().getValue(), now.getYear(), workEntry.getEmployeeId()).forEach(timesheet -> {

                    LocalTime time = timesheet.getTotalHours().minusHours(9).plusMinutes(0);


                    timesheet.setTotalHours(time);

                    timesheetData.save(timesheet);
                });

            }

        });

    }

}
