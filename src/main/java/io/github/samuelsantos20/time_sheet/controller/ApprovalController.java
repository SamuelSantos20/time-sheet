package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.ApprovalDTO;
import io.github.samuelsantos20.time_sheet.mapper.ApprovalMapper;
import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.service.ApprovalService;
import io.github.samuelsantos20.time_sheet.service.EmployeeService;
import io.github.samuelsantos20.time_sheet.service.UserService;
import io.github.samuelsantos20.time_sheet.service.WorkEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/approval")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Approval")
public class ApprovalController implements GenericController {

    private final ApprovalService approvalService;

    private final ApprovalMapper approvalMapper;

    private final UserService userService;

    private final EmployeeService employeeService;

    private final WorkEntryService workEntryService;

    @PostMapping("/save")
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Salvar", description = "Cadastrar a aprovação de uma folha de ponto")
    @ApiResponses({

            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Aprovação já cadastrada.")


    })
    public ResponseEntity<Object> saveApproval(@RequestBody @Valid ApprovalDTO approvalDTO) {

        Approval entity = approvalMapper.toEntity(approvalDTO);

        Approval approval = approvalService.approvalSave(entity);

        log.info("Dados salvos: {}", List.of(approval));

        URI uri = gerarHaderLoccation(approval.getApprovalId());

        return ResponseEntity.created(uri).build();


    }

    @GetMapping("/listapproval")
    public ResponseEntity<List<Map<String, Object>>> listApproval() {
        try {
            List<WorkEntry> workEntryList = workEntryService.workEntryList();
            log.info("workEntryList size: {}", workEntryList.size());

            List<Map<String, Object>> approvalResponseList = workEntryList.stream()
                    .map(workEntry -> {
                        Map<String, Object> map = new HashMap<>();

                        log.info("-------------------------Inicio-------------------------------------");
                        log.info("WorkEntry userId: {}", workEntry.getUserId().getId());
                        log.info("WorkEntry registration: {}", workEntry.getUserId().getRegistration());
                        log.info("WorkEntry roles: {}", workEntry.getUserId().getRoles());
                        log.info("WorkEntry startTime: {}", workEntry.getStartTime());
                        log.info("WorkEntry endTime: {}", workEntry.getEndTime());
                        log.info("WorkEntry dateCreated: {}", workEntry.getDateCreated());
                        log.info("WorkEntry dateUpdate: {}", workEntry.getDateUpdate());
                        log.info("WorkEntry timesheetId: {}", workEntry.getTimesheetId() != null ? workEntry.getTimesheetId().getTimesheetId() : "null");
                        log.info("-------------------------FIM-------------------------------------");

                        // Check startTime
                        if (workEntry.getStartTime() == null) {
                            log.warn("WorkEntry with ID {} has null startTime for userId {}",
                                    workEntry.getWorkEntryId(), workEntry.getUserId().getId());
                            map.put("error", "Work entry has no start time");
                            return map;
                        }

                        // Fetch employee
                        Optional<Employee> employee = employeeService.employeeSearchByUserId(workEntry.getUserId().getId());
                        log.info("Employee data: {}", employee);
                        if (!employee.isPresent()) {
                            map.put("error", "Employee not found");
                            return map;
                        }

                        // Check timesheetId
                        if (workEntry.getTimesheetId() == null) {
                            log.warn("WorkEntry with ID {} has null timesheetId for userId {}",
                                    workEntry.getWorkEntryId(), workEntry.getUserId().getId());
                            map.put("error", "Work entry has no associated timesheet");
                            return map;
                        }

                        // Fetch approval
                        Optional<Approval> approval = approvalService.Approval_TimesheetSearch(
                                employee.get().getUser().getId(), 
                                workEntry.getTimesheetId().getTimesheetId()
                        );
                        log.info("Approval data: {}", approval);

                        // Populate map with data
                        map.put("id", employee.get().getId());
                        map.put("firstName", employee.get().getFirstName());
                        map.put("data", workEntry.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        map.put("start", workEntry.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                        map.put("end", workEntry.getEndTime() != null ? workEntry.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A");
                        map.put("approvalStatus", approval.isPresent() ? approval.get().getApprovalStatus() : "N/A");
                        map.put("comentario", approval.isPresent() && approval.get().getComments() != null ? approval.get().getComments() : "");
                        map.put("workEntryId", workEntry.getWorkEntryId()); // For approve/reject actions

                        log.info("Populated map: {}", map);
                        return map;
                    })
                    .collect(Collectors.toList());

            log.info("Approval response size: {}", approvalResponseList.size());
            log.info("Approval response: {}", approvalResponseList);
            return ResponseEntity.status(HttpStatus.OK).body(approvalResponseList);
        } catch (Exception e) {
            log.error("Error in listApproval: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Collections.singletonMap("error", "Internal server error: " + e.getMessage())));
        }
    }
}
