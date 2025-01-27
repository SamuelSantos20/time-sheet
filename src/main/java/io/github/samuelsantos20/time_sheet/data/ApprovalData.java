package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApprovalData extends JpaRepository<Approval, UUID> {
}
