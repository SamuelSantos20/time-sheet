package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.EmployeeDTO;
import io.github.samuelsantos20.time_sheet.mapper.EmployeeMapper;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.service.EmployeeService;
import io.github.samuelsantos20.time_sheet.service.UserService;
import io.github.samuelsantos20.time_sheet.util.PasswordGenerator;
import io.github.samuelsantos20.time_sheet.util.RegistrationGenerator;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
@Slf4j
public class EmployeeController implements GenericController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    private final UserService userService;

    private final EntityManager  entityManager;


    @PostMapping
    @Transactional
    public ResponseEntity<Object> saveEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {


        log.info("Valores do metodo de save do EmployeeDTO: {}", employeeDTO);

        Employee entity = employeeMapper.toEntity(employeeDTO);

        User user = entityManager.merge(UserCreate());

        entity.setUser(user);


        log.info("Valores do metodo de save do Employee. Department: {} , Position: {}, FirstName: {}, LastName : {}, Email: {},User: Matricula: {} e Senha: {} ",
                entity.getDepartment(),
                entity.getPosition(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getUser().getRegistration(),
                entity.getUser().getPassword());


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


    private User UserCreate() {

        User user = new User();

        RegistrationGenerator registrationGenerator = new RegistrationGenerator();

        PasswordGenerator passwordGenerator = new PasswordGenerator();

        String generator = registrationGenerator.Generator();

        String generator1 = passwordGenerator.Generator();

        log.info("Senha : {}  e  Matricula : {}  gerada pelo meto de save de " +
                "usuario na classe EmployeeController", generator1, generator);

        user.setRegistration(generator);
        user.setPassword(generator1);

        return userService.saveUser(user);


    }


}
