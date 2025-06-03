package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.data.ManagerData;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.model.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ManagerValidation {

    private final ManagerData managerData;

    public void validate(Manager manager) {
        if (Objects.isNull(manager)) {
            throw new IllegalArgumentException("O objeto Manager fornecido é nulo.");
        }

        if (existsManager(manager)) {
            throw new DuplicateRecord("Esse Manager já está cadastrado no banco de dados.");
        }
    }

    private boolean existsManager(Manager manager) {
        Optional<Manager> optionalManager = managerData.findByEmailAndFirstNameAndLastNameAndDepartment(
                manager.getEmail(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getDepartment()
        );

        if (manager.getManagerId() == null) {
            return optionalManager.isPresent();
        }

        return optionalManager
                .map(existing -> !existing.getManagerId().equals(manager.getManagerId()))
                .orElse(false);
    }
}
