package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.TimesheetResponseDTO;
import io.github.samuelsantos20.time_sheet.mapper.TimesheetMapper;
import io.github.samuelsantos20.time_sheet.service.TimesheetService;
import io.github.samuelsantos20.time_sheet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private final UserService userService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('Gerente')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do TimeSheet pelo ID do Usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TimeSheet encontrado."),
            @ApiResponse(responseCode = "404", description = "TimeSheet não encontrado."),
    })
    public ResponseEntity<Object> timesheetList(@PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);


        Stream<TimesheetResponseDTO> timesheetStream = timesheetService.TimesheetList(uuid).stream().map(timesheets -> {

            TimesheetResponseDTO timesheet = new TimesheetResponseDTO(timesheets.getUserId().getRegistration(),
                    timesheets.getDayInMonth(),
                    timesheets.getMonth(),
                    timesheets.getYear(),
                    timesheets.getTimeSheetUpdate(),
                    timesheets.getTimeSheetCreated(),
                    timesheets.getTotalHours().toHours());


            return timesheet;


        });

        List<TimesheetResponseDTO> timesheetDTOS = timesheetStream.toList();

        log.info("Lista de objetos sendo enviados : {}", timesheetDTOS);


        if (!timesheetDTOS.isEmpty()) {

            return ResponseEntity.ok(timesheetDTOS);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TimeSheet não localizado a partir do id do usuario!");

        }

    }

   /* @GetMapping(value = "/{registration}/month/{month}/year/{year}")
    public ResponseEntity<Object> objectTimesheetEntity(@PathVariable(value = "id") String registration,
                                                        @PathVariable(value = "month") String month,
                                                        @PathVariable(value = "year") String year) {

        Optional<User> byRegistration = userService.findByRegistration(registration);
        if (byRegistration.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = byRegistration.get();


        UUID uuid = UUID.fromString(user.getId().toString());

        Stream<Object> timesheetStream = timesheetService.TimesheetList(uuid).stream().map(timesheets -> {

            TimesheetResponseDTO timesheet = new TimesheetResponseDTO(timesheets.getUserId().getRegistration(),
                    timesheets.getDayInMonth(),
                    timesheets.getMonth(),
                    timesheets.getYear(),
                    timesheets.getTimeSheetUpdate(),
                    timesheets.getTimeSheetCreated(),
                    timesheets.getTotalHours().toHours());

            return timesheet;
        });

        return ResponseEntity.status(HttpStatus.OK).body(timesheetStream);



    }*/
}