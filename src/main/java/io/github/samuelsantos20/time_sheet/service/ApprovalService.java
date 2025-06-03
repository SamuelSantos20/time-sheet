package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.ApprovalData;
import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ApprovalService {

    private final ApprovalData approvalData;

    @CacheEvict(value = "approvalCache", allEntries = true)
    public Approval approvalSave(Approval approval) {

        return approvalData.save(approval);


    }

    @SneakyThrows
    @Cacheable(value = "approvalCache")
    public Optional<Approval> ApprovalResearch(UUID id) {

        if (Objects.isNull(id)) {

            throw new IllegalArgumentException("O valor enviado é nulo!");

        }

        return Optional.ofNullable(approvalData.findById(id).orElseThrow(() -> new BadRequestException("Approval não localizado!")));

    }


    @Transactional(readOnly = true)
    @Cacheable(value = "approvalCache")
    public Optional<Approval> Approval_TimesheetSearch(UUID id, UUID timesheet) {

        return approvalData.findByUser_IdAndTimesheet_TimesheetId(id, timesheet);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "approvalCache")
    public List<Approval> approvalList() {

        return approvalData.findAll();

    }

    @CacheEvict(value = "approvalCache", allEntries = true)
    public void approvalUpdate(Approval approval) {

        approvalData.save(approval);

    }

    @CacheEvict(value = "approvalCache", allEntries = true)
    public void approvalDelete(UUID id) {

        approvalData.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "approvalCache")
    public Optional<Approval> ApprovalUserId(User user) {

        return approvalData.findByUser_Id(user.getId());
    }

}
