package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class TimesheetProcess {

    private final TimesheetService timesheetService;


    public void process(Timesheet timesheet) {
        log.info("Processando Timesheet para o funcionário: {}", timesheet.getUserId());
        log.info("Mês/Ano do Timesheet: {}/{}", timesheet.getMonth(), timesheet.getYear());

        LocalDateTime localDateNow = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.of(timesheet.getYear(), timesheet.getMonth());
        YearMonth newer = YearMonth.from(localDateNow);

        if (yearMonth.isBefore(newer)) {
            log.warn("Operação não permitida: Timesheet para mês/ano anterior.");
            throw new OperationNotPermitted("Não é possível realizar esta operação!");
        }

        Optional<Timesheet> optionalTimesheet = timesheetService.timesheetSearchMonthAndEmployee_id(
                timesheet.getMonth(),
                timesheet.getYear(),
                timesheet.getUserId());

        optionalTimesheet.ifPresentOrElse(timesheet1 -> {
            try {
                Timesheet existingTimesheet = optionalTimesheet.get();
                Duration totalWorkedTime = existingTimesheet.getTotalHours().plus(timesheet.getTotalHours());
                existingTimesheet.setTotalHours(totalWorkedTime);

                timesheetService.timesheetSave(existingTimesheet);
                log.info("Timesheet atualizado com sucesso: {}", existingTimesheet);
            } catch (Exception e) {
                log.error("Erro ao atualizar Timesheet: {}", e.getMessage(), e);
            }
        }, () -> {
            try {
                timesheetService.timesheetSave(timesheet);
                log.info("Novo Timesheet salvo com sucesso: {}", timesheet);
            } catch (Exception e) {
                log.error("Erro ao salvar novo Timesheet: {}", e.getMessage(), e);
            }
        });
    }
}
