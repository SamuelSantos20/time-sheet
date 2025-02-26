package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.EmployeeDTO;
import io.github.samuelsantos20.time_sheet.mapper.EmployeeMapper;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.service.EmployeeService;
import io.github.samuelsantos20.time_sheet.service.UserService;
import io.github.samuelsantos20.time_sheet.util.PasswordGenerator;
import io.github.samuelsantos20.time_sheet.util.RegistrationGenerator;
import io.github.samuelsantos20.time_sheet.model.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Cadastrar", description = "Cadastrar novo Employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro  de validação."),
            @ApiResponse(responseCode = "409", description = "Employee já cadastrado.")
    })
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
    @PreAuthorize("hasRole('Gerente')")
    @Operation(summary = "Lista de Employees", description = "Realiza pesquisa de todos Employees")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso."),
    })
    public ResponseEntity<Object> findAll() {


        List<Employee> employees = employeeService.employeeListString();

        List<EmployeeDTO> entityList = employeeMapper.toEntityList(employees);

        return ResponseEntity.ok(entityList);


    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('Gerente')")
    @Operation(summary = "Atualizar", description = "Atualiza um Employee existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Employee não encontrado."),
            @ApiResponse(responseCode = "409", description = "Employee já cadastrado.")
    })
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
    @PreAuthorize("hasAnyRole('Gerente', 'Funcionário')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do Employee pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee encontrado."),
            @ApiResponse(responseCode = "404", description = "Employee não encontrado."),
    })
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
    @PreAuthorize("hasRole('Gerente')")
    @Operation(summary = "Deletar", description = "Deleta um Employee existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Employee não encontardo."),
            @ApiResponse(responseCode = "400", description = "Employee possui livro cadastrado.")
    })
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
        String descricao = Role.EMPLOYEE.getDescricao();

        user.setRegistration(generator);
        user.setPassword(generator1);
        user.getRoles().add(descricao);

        log.info("Valores gerados para User: Matricula: {}, Senha: {}, Role: {}",user.getRegistration(), user.getPassword(), Role.EMPLOYEE );


        return userService.saveUser(user);


    }


}
