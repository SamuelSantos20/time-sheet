package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.dto.UserDTO;
import io.github.samuelsantos20.time_sheet.mapper.UserMapper;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PutMapping("/{id}")
    @SneakyThrows
    @PreAuthorize("hasAnyRole('Gerente', 'Funcionário')")
    public ResponseEntity<Object> UpdatePassword(@RequestBody @Valid UserDTO userDTO, @PathVariable(value = "id") String id) {

        UUID uuid = UUID.fromString(id);

        userService.findByUserId(uuid).ifPresentOrElse(user1 -> {

            User entity = userMapper.toEntity(userDTO);


            user1.setPassword(entity.getPassword());

            userService.Update(user1);

        }, () -> {

            try {
                throw new BadRequestException("User não encontrado!");

            } catch (BadRequestException e) {

                throw new RuntimeException(e);
            }

        });

        return ResponseEntity.noContent().build();

    }

}
