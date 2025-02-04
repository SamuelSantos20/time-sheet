package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.util.TimesheetProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "timesheet")
@Slf4j
public class TimesheetController {

    private final TimesheetProcess timesheetProcess;


    @PostMapping
    public ResponseEntity<Object> saveTimesheet() {


    }



}
