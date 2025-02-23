package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.ManagerData;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.validation.ManagerValidation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ManagerService {

    private final ManagerData managerData;

    private final ManagerValidation managerValidation;

    public Manager managerSave(Manager manager) {

       // managerValidation.validation(manager);

        return managerData.save(manager);

    }

    @SneakyThrows //O uso dessa anotação exclui a necessidade de anotar o método com Throw
    @Transactional(readOnly = true)
    public Optional<Manager> ManagerUniqueResearch(UUID id) {

        return Optional.ofNullable(managerData.findById(id).orElseThrow(() -> new BadRequestException("Manager não localizado!")));
    }

    @Transactional(readOnly = true)
    public List<Manager> ManagerList() {

        return managerData.findAll();
    }

    public void managerUpdate(Manager manager) {

        managerValidation.validation(manager);

        managerData.save(manager);


    }


    public void managerDelete(Manager manager) {

        managerData.delete(manager);

    }


}
