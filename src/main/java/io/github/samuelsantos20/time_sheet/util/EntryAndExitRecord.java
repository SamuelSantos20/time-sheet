package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
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
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class EntryAndExitRecord {
    private final WorkEntryService workEntryService;

    private final TimesheetData timesheetData;


    /**
     * 0: Segundo (0 segundo).
     * 0: Minuto (0 minuto).
     * 23: Hora (23 horas, ou seja, 11 PM).
     * *: Todos os dias do mês.
     * *: Todos os meses.
     */
    @Scheduled(cron = "0 0 23 * * ?") // Executa todos os dias às 23:00 (11 PM)
    public void ajustarHorasTrabalhadas() {
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
                            Duration newTotalHours = timesheet.getTotalHours().minusHours(9);
                            timesheet.setTotalHours(newTotalHours);

                            Timesheet save = timesheetData.save(timesheet);
                            log.info("Resultado da função de acompanhamento do work_entry: {}", save);

                    } catch (Exception e) {
                        log.error("Erro ao processar o Timesheet: {}", e.getMessage(), e);
                    }
                });
            }
        });
    }
}
