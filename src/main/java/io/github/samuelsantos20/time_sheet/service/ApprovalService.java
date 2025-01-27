package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.ApprovalData;
import io.github.samuelsantos20.time_sheet.model.Approval;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
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


    public void approvalSave(Approval approval) {

        approvalData.save(approval);

    }

    @SneakyThrows
    public Optional<Approval> ApprovalResearch(UUID id) {

        if (Objects.isNull(id)) {

            throw new IllegalArgumentException("O valor enviado é nulo!");

        }

        return Optional.ofNullable(approvalData.findById(id).orElseThrow(() -> new BadRequestException("Approval não localizado!")));

    }


    public List<Approval> approvalList() {

        return approvalData.findAll();

    }

    public void approvalUpdate(Approval approval) {

        approvalData.save(approval);

    }

    public void approvalDelete(UUID id) {

        approvalData.deleteById(id);
    }

}
