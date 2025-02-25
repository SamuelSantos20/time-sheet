package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.TimesheetAlternativeDTO;
import io.github.samuelsantos20.time_sheet.mapper.TimesheetMapper;
import io.github.samuelsantos20.time_sheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/timesheet")
@Slf4j
public class TimesheetController {

    private final TimesheetService timesheetService;

    private final TimesheetMapper timesheetMapper;




    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('Gerente')")
    public ResponseEntity<Object> timesheetList(@PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);


        Stream<TimesheetAlternativeDTO> timesheetStream = timesheetService.TimesheetList(uuid).stream().map(timesheets -> {

            TimesheetAlternativeDTO timesheet = new TimesheetAlternativeDTO(timesheets.getUserId().getRegistration(),
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
