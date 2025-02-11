package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.TimesheetAlternativeDTO;
import io.github.samuelsantos20.time_sheet.dto.TimesheetDTO;
import io.github.samuelsantos20.time_sheet.mapper.TimesheetMapper;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/timesheet")
@Slf4j
public class TimesheetController {

    private final TimesheetService timesheetService;

    private final TimesheetMapper timesheetMapper;




    @GetMapping
    public ResponseEntity<Object> timesheetList() {


        Stream<TimesheetAlternativeDTO> timesheetStream = timesheetService.TimesheetList().stream().map(timesheets -> {

            TimesheetAlternativeDTO timesheet = new TimesheetAlternativeDTO(timesheets.getUserId().getRegistration(),
                    timesheets.getApproval(),
                    timesheets.getMonth(),
                    timesheets.getYear(),
                    timesheets.getTimeSheetUpdate(),
                    timesheets.getTimeSheetCreated(),
                    timesheets.getTotalHours().toHours());




            return timesheet;


        });

        List<TimesheetAlternativeDTO> timesheetDTOS = timesheetStream.toList();

        log.info("Lista de objetos sendo enviados : {}", timesheetDTOS);


        return ResponseEntity.ok(timesheetDTOS);


    }





}
