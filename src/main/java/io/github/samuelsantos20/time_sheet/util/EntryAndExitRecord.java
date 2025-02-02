package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
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

    public void Entry(User id) {

        LocalDate dayNow = LocalDate.now();

        Optional<WorkEntry> workEntry = workEntryService.searchByCurrentDayAndUserID(dayNow, id);

        workEntry.ifPresentOrElse(workEntry1 -> {

            throw new OperationNotPermitted("Já consta entrada reistrada no dia de Hoje!");

        }, () -> {

            LocalDateTime dayTimeNow = LocalDateTime.now();

            workEntry.get().setStartTime(dayTimeNow);
            workEntry.get().setUserId(id);
            workEntryService.workEntrySave(workEntry.get());

        });

    }


    public void Exit(User user_id) {
        log.info("Registrando saída para o funcionário: {}", user_id);

        LocalDate exitNow = LocalDate.now();
        Optional<WorkEntry> exitOptional = workEntryService.searchByCurrentDayAndUserID(exitNow, user_id);

        exitOptional.ifPresentOrElse(workEntry -> {
            try {
                LocalDateTime exitTimeNow = LocalDateTime.now();
                workEntry.setEndTime(exitTimeNow);
                WorkEntry exit = workEntryService.workEntryUpdate(workEntry);

                Duration duration = Duration.between(exit.getStartTime(), exit.getEndTime());
                int totHour = (int) duration.toHours();
                int totMinute = (int) (duration.toMinutes() % 60);

                log.info("Tempo trabalhado: {} horas e {} minutos", totHour, totMinute);

                if (totHour > 10) {
                    log.warn("Horas extras detectadas: {} horas e {} minutos", totHour, totMinute);
                    // Considere armazenar horas extras em um campo separado.
                }

                Timesheet timesheet = new Timesheet();
                Duration totalWorkedTime = Duration.ofHours(totHour).plusMinutes(totMinute);
                timesheet.setTotalHours(totalWorkedTime);
                timesheet.setUserId(exit.getUserId());

                timesheetTimesheetProcess.process(timesheet);
            } catch (Exception e) {
                log.error("Erro ao processar saída: {}", e.getMessage(), e);
            }
        }, () -> {
            log.info("Entrada ainda não marcada na data atual.");
            Entry(user_id);
        });
    }


    /**
     * 0: Segundo (0 segundo).
     * 0: Minuto (0 minuto).
     * 23: Hora (23 horas, ou seja, 11 PM).
     * *: Todos os dias do mês.
     * *: Todos os meses.
     */
    @Scheduled(cron = "0 0 23 * * ?") // Executa todos os dias às 23:00 (11 PM)
    private void ajustarHorasTrabalhadas() {
        log.info("Tarefa agendada executada às 23:00 para ajustar horas trabalhadas.");

        LocalDate localDate = LocalDate.now();
        log.info("Executando tarefa agendada para: {}", localDate);

        List<WorkEntry> workEntries = workEntryService.findByStart_timeAndExit_time(localDate);
        log.info("Total de registros para serem analisados: {}", workEntries.size());

        workEntries.forEach(workEntry -> {
            if (Objects.isNull(workEntry.getStartTime()) || Objects.isNull(workEntry.getEndTime())) {
                LocalDateTime now = LocalDateTime.now();

                timesheetData.ListFindByMonthAndUserId(now.getMonth().getValue(), now.getYear(), workEntry.getUserId()).forEach(timesheet -> {
                    try {
                        if (timesheet.getTotalHours().toHours() >= 9) {
                            Duration newTotalHours = timesheet.getTotalHours().minusHours(9);
                            timesheet.setTotalHours(newTotalHours);

                            Timesheet save = timesheetData.save(timesheet);
                            log.info("Resultado da função de acompanhamento do work_entry: {}", save);
                        } else {
                            log.warn("Total de horas menor que 9 para o Timesheet: {}", timesheet);
                        }
                    } catch (Exception e) {
                        log.error("Erro ao processar o Timesheet: {}", e.getMessage(), e);
                    }
                });
            }
        });
    }
}
