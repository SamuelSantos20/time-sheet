package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.WorkEntryDTO;
import io.github.samuelsantos20.time_sheet.mapper.WorkEntryMapper;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.service.WorkEntryService;
import io.github.samuelsantos20.time_sheet.util.EntryAndExitRecord;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/workEntry")
@RequiredArgsConstructor
@Slf4j
public class WorkEntryController {

    private final EntryAndExitRecord entryAndExitRecord;

    private final WorkEntryMapper workEntryMapper;

    private final WorkEntryService workEntryService;

    @PostMapping(value = "/{userId}/entry")
    public ResponseEntity<Object> Entry(@PathVariable(value = "userId")String userId) {

        System.out.println(userId);

        UUID id = UUID.fromString(userId);

        log.info("Id do usuario: {}", id);

        User user = new User();

        user.setId(id);


        entryAndExitRecord.Entry(user.getId());

        return ResponseEntity.ok().build();

    }

    @PostMapping("/{userId}/exit")
    public ResponseEntity<Object> Exit(@PathVariable(value = "userId")String userId) {

        System.out.println(userId);

        UUID uuidd = UUID.fromString(userId);

//        log.info("Id do usuario: {}", uuidd);

        User user = new User();

        user.setId(uuidd);


        entryAndExitRecord.Exit(user.getId());

        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<Object> WorkEntryList() {

        List<WorkEntryDTO> list = workEntryService.workEntryList().stream().map(workEntry -> new WorkEntryDTO(
                workEntry.getTimesheetId().getTimesheetId(),
                workEntry.getUserId().getRegistration(),
                workEntry.getStartTime(),
                workEntry.getEndTime(),
                workEntry.getDateCreated(),
                workEntry.getDateUpdate())).toList();

        return ResponseEntity.ok(list);


    }




}
