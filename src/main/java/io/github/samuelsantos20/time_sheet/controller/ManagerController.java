package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.ManagerDTO;
import io.github.samuelsantos20.time_sheet.mapper.ManagerMapper;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/manager")
@Slf4j
public class ManagerController implements GenericController {

    private final ManagerService managerService;

    private final ManagerMapper managerMapper;

    @PostMapping
    public ResponseEntity<Object> saveManage(@RequestBody @Valid ManagerDTO managerDTO) {

        log.info("Valores do novo manager : {}", managerDTO);

        Manager entity = managerMapper.toEntity(managerDTO);

        Manager manager = managerService.managerSave(entity);

        URI uri = gerarHaderLoccation(manager.getManagerId());

        return ResponseEntity.created(uri).build();


    }

    @GetMapping
    public ResponseEntity<Object> findAllManager() {

        List<ManagerDTO> managerList = managerMapper.toManagerList(managerService.ManagerList());

        return ResponseEntity.ok(managerList);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ManagerDTO> finByIdManger(@PathVariable("id") String id) {

        UUID uuid = UUID.fromString(id);

        return managerService.ManagerUniqueResearch(uuid).map(manager -> {

            ManagerDTO dto = managerMapper.toDto(manager);

            return ResponseEntity.ok(dto);

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> Update(@RequestBody ManagerDTO managerDTO,
                                         @PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);

        return managerService.ManagerUniqueResearch(uuid).map(manager -> {

            log.info("Metodo de update e Manager que sofrendo atualização: {} ", manager);

            manager.setDepartment(managerDTO.department());
            manager.setEmail(managerDTO.email());
            manager.setFirstName(managerDTO.firstName());
            manager.setLastName(managerDTO.lastName());


            managerService.managerUpdate(manager);

            return ResponseEntity.noContent().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());


    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> DeleteManager(@PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);

        return managerService.ManagerUniqueResearch(uuid).map(manager -> {

            log.info("Manager Deletado: {} {} , departamento: {} , e-mail: {} ", manager.getFirstName(), manager.getLastName(),
                    manager.getDepartment(), manager.getEmail());

            managerService.managerDelete(manager);

            return ResponseEntity.noContent().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }





}
