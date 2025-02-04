package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.mapper.WorkEntryMapper;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.util.EntryAndExitRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/workEntry")
@RequiredArgsConstructor
@Slf4j
public class WorkEntryController {

    private final EntryAndExitRecord entryAndExitRecord;

    private final WorkEntryMapper workEntryMapper;

    @PostMapping(value = "/{userId}")
    public ResponseEntity<Object> Entry(@PathVariable(value = "userId")String userId) {

        System.out.println(userId);

        UUID id = UUID.fromString(userId);

        log.info("Id do usuario: {}", id);

        User user = new User();

        user.setId(id);


        entryAndExitRecord.Entry(user.getId());

        return ResponseEntity.ok().build();

    }

    @PostMapping
    public ResponseEntity<Object> Exit(@RequestParam(value = "userId")String userId) {

        System.out.println(userId);

        UUID uuidd = UUID.fromString(userId);

        //log.info("Id do usuario: {}", uuidd);

        User user = new User();

        user.setId(uuidd);


        entryAndExitRecord.Exit(user.getId());

        return ResponseEntity.ok().build();

    }




}
