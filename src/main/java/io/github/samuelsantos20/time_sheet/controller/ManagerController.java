package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.ManagerDTO;
import io.github.samuelsantos20.time_sheet.mapper.ManagerMapper;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.service.ManagerService;
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
@RequestMapping(value = "/manager")
@Slf4j
public class ManagerController implements GenericController {

    private final ManagerService managerService;

    private final ManagerMapper managerMapper;

    private final UserService userService;

    private final EntityManager entityManager;

    @PostMapping
    @Transactional
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Cadastrar", description = "Cadastrar novo Manager")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro  de validação."),
            @ApiResponse(responseCode = "409", description = "Manager já cadastrado.")
    })
    public ResponseEntity<Object> saveManager(@RequestBody @Valid ManagerDTO managerDTO) {

        log.info("Valores do novo manager : {}", managerDTO);

        Manager entity = managerMapper.toEntity(managerDTO);

        User userReturn = entityManager.merge(UserCreate());

        entity.setUser(userReturn);

        log.info("Valores do metodo de save do Manager. Department: {}, FirstName: {}, LastName : {}, Email: {},User: Matricula: {} e Senha: {} ",
                entity.getDepartment(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getUser().getRegistration(),
                entity.getUser().getPassword());


        Manager manager = managerService.managerSave(entity);



        URI uri = gerarHaderLoccation(manager.getManagerId());

        return ResponseEntity.created(uri).build();


    }


    @GetMapping
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Lista de Managers", description = "Realiza pesquisa de todos Managers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso."),
    })
    public ResponseEntity<Object> findAllManager() {

        List<ManagerDTO> managerList = managerMapper.toManagerList(managerService.ManagerList());

        return ResponseEntity.ok(managerList);

    }

    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do Manager pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager encontrado."),
            @ApiResponse(responseCode = "404", description = "Manager não encontrado."),
    })
    public ResponseEntity<ManagerDTO> finByIdManger(@PathVariable("id") String id) {

        UUID uuid = UUID.fromString(id);

        return managerService.ManagerUniqueResearch(uuid).map(manager -> {

            ManagerDTO dto = managerMapper.toDto(manager);

            return ResponseEntity.ok(dto);

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/{id}")
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Atualizar", description = "Atualiza um Manager existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Manager não encontrado."),
            @ApiResponse(responseCode = "409", description = "Manager já cadastrado.")
    })
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
    @PreAuthorize(value = "hasRole('Gerente')")
    @Operation(summary = "Deletar", description = "Deleta um Manager existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Manager não encontardo."),
            @ApiResponse(responseCode = "400", description = "Manager possui livro cadastrado.")
    })
    public ResponseEntity<Object> DeleteManager(@PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);

        return managerService.ManagerUniqueResearch(uuid).map(manager -> {

            log.info("Manager Deletado: {} {} , departamento: {} , e-mail: {} ", manager.getFirstName(), manager.getLastName(),
                    manager.getDepartment(), manager.getEmail());

            managerService.managerDelete(manager);

            return ResponseEntity.noContent().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }


    private User UserCreate(){

        User user = new User();
        RegistrationGenerator registrationGenerator = new RegistrationGenerator();
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        String registration = registrationGenerator.Generator();

        String password = passwordGenerator.Generator();

        String manager = Role.MANAGER.getDescricao();

        user.setPassword(password);
        user.setRegistration(registration);
        user.getRoles().add(manager);

        log.info("Valores gerados para User: Matricula: {}, Senha: {}, Role: {}",user.getRegistration(), user.getPassword(), Role.MANAGER );


        return userService.saveUser(user);


    }





}
