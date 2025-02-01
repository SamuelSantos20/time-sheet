package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.data.EmployeeData;
import io.github.samuelsantos20.time_sheet.dto.EmployeeDTO;
import io.github.samuelsantos20.time_sheet.mapper.EmployeeMapper;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
@Slf4j
public class EmployeeController implements GenericController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    @PostMapping
    public ResponseEntity<Object> saveEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {

        log.info("Valores do metodo de save do Employee: {}", employeeDTO);

        Employee entity = employeeMapper.toEntity(employeeDTO);

        Employee employee = employeeService.employeeSave(entity);


        URI uri = gerarHaderLoccation(employee.getId());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {


        List<Employee> employees = employeeService.employeeListString();

        List<EmployeeDTO> entityList = employeeMapper.toEntityList(employees);

        return ResponseEntity.ok(entityList);


    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> Update(@RequestBody @Valid EmployeeDTO employeeDTO,
                                         @PathVariable(value = "id") String id) {

        log.info("Valores do metodo de update de Employee: id = {} ,  employee: {}", id, employeeDTO);

        UUID uuid = UUID.fromString(id);

        return employeeService.employeeSearch(uuid).map(employee2 -> {


            employee2.setLastName(employeeDTO.lastName());
            employee2.setFirstName(employeeDTO.firstName());
            employee2.setEmail(employeeDTO.email());
            employee2.setPosition(employeeDTO.position());
            employee2.setDepartment(employeeDTO.department());

            employeeService.employeeUpdate(employee2);

            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());


    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findByIdEmployee(@PathVariable("id") String id) {

        log.info("Busca do employee com id de : {}", id);

        UUID uuid = UUID.fromString(id);

        return employeeService.employeeSearch(uuid).map(employee -> {

            EmployeeDTO dto = employeeMapper.toDto(employee);

            log.info("Valores retornados: {} ", dto);

            return ResponseEntity.ok(dto);

        }).orElseGet(() -> ResponseEntity.notFound().build());


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete(@PathVariable("id") String id) {

        UUID uuid = UUID.fromString(id);

        return employeeService.employeeSearch(uuid).map(employee -> {

            log.info("Usuario excluido: {}", employee);
            employeeService.employeeDelete(employee);

            return ResponseEntity.ok().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }


}
