package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.ApprovalDTO;
import io.github.samuelsantos20.time_sheet.dto.ApprovalResponse;
import io.github.samuelsantos20.time_sheet.dto.ApprovalUserDTO;
import io.github.samuelsantos20.time_sheet.dto.TimesheetResponseDTO;
import io.github.samuelsantos20.time_sheet.mapper.ApprovalMapper;
import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.service.ApprovalService;
import io.github.samuelsantos20.time_sheet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/approval")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Approval")
public class ApprovalController implements GenericController {

    private final ApprovalService approvalService;

    private final ApprovalMapper approvalMapper;

    private final UserService userService;


    @PostMapping
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

    @GetMapping
    @PreAuthorize("hasAnyRole('Gerente', 'Funcionário')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados da aprovação pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aprovação encontrada."),
            @ApiResponse(responseCode = "404", description = "Aprovação não encontrada."),
    })
    public ResponseEntity<ApprovalResponse> Approval_TimesheetSearch(@RequestParam(value = "id_manager") String id_manager,
                                                                     @RequestParam(value = "id_timesheet") String id_timesheet) {

        UUID manager = UUID.fromString(id_manager);
        UUID timesheet = UUID.fromString(id_timesheet);

        Optional<Approval> approvalOptional = approvalService.Approval_TimesheetSearch(manager, timesheet);


        return approvalOptional.map(approval -> {

            Optional<Manager> first_manager = userService.findByManagers_User(approval.getUser()).get()
                    .getManagers()
                    .stream()
                    .filter(manager1 -> manager1.getUser().equals(approval.getUser()))
                    .findFirst();


            TimesheetResponseDTO timesheetResponseDTO = first_manager.map(manager1 -> new TimesheetResponseDTO(
                    approval.getTimesheet().getUserId().getRegistration(),
                    approval.getTimesheet().getMonth(),
                    approval.getTimesheet().getYear(),
                    approval.getTimesheet().getTimeSheetUpdate(),
                    approval.getTimesheet().getTimeSheetCreated(),
                    approval.getTimesheet().getTotalHours().toHours())
            ).orElse(null);

            ApprovalUserDTO approvalUserDTO1 = new ApprovalUserDTO(first_manager.map(Manager::getFirstName).orElse(""), approval.getUser().getRegistration());


            ApprovalResponse dto  = approvalMapper.toDtoResponse(approvalUserDTO1, approval, timesheetResponseDTO);

            return Optional.ofNullable(dto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());



        }).orElseGet(() -> ResponseEntity.notFound().build());


    }


}
