package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.data.*;
import io.github.samuelsantos20.time_sheet.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired private WorkEntryData workEntryRepository;
    @Autowired
    private ApprovalData approvalRepository;
    @Autowired private TimesheetData timesheetRepository;
    @Autowired private EmployeeData employeeRepository;
    @Autowired private ManagerData managerRepository;
    @Autowired private UserData userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seed();
    }

    @Transactional
    public void seed() {
        // Limpa as tabelas na ordem correta
        workEntryRepository.deleteAll();
        approvalRepository.deleteAll();
        timesheetRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        userRepository.deleteAll();

        System.out.println("🚮 Tabelas limpas.");

        for (int i = 1; i <= 20; i++) {
            String registration = String.format("USR%04d", i);
            UUID userId = UUID.randomUUID();

            // Usuário
            User user = new User();
            user.setId(userId);
            user.setRegistration(registration);
            user.setPassword(passwordEncoder.encode("password123"));
            boolean isManager = i > 15;
            user.setRoles(List.of(new String[]{isManager ? "Gerente" : "Funcionário"}));
            User merge = entityManager.merge(userRepository.save(user));

            // Manager ou Employee
            if (isManager) {
                Manager manager = new Manager();
                manager.setManagerId(UUID.randomUUID());
                manager.setUser(merge);
                manager.setFirstName("Gerente" + i);
                manager.setLastName("da Silva");
                manager.setDepartment("TI");
                manager.setEmail("gerente" + i + "@empresa.com");
                managerRepository.save(manager);
            } else {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setUser(merge);
                employee.setFirstName("Funcionario" + i);
                employee.setLastName("Souza");
                employee.setDepartment("TI");
                employee.setEmail("funcionario" + i + "@empresa.com");
                employee.setPosition("Desenvolvedor");
                employeeRepository.save(employee);
            }

            // Timesheet
            UUID timesheetId = UUID.randomUUID();
            Timesheet timesheet = new Timesheet();
            timesheet.setTimesheetId(timesheetId);
            timesheet.setUserId(merge);
            timesheet.setDayInMonth(15 + (i % 10)); // de 15 a 24
            timesheet.setDayInMonth(5);
            timesheet.setYear(2025);
            timesheet.setTotalHours(Duration.ofHours(Long.parseLong(BigDecimal.valueOf(160).toString())));
            timesheet.setTimeSheetCreated(LocalDateTime.now());
            timesheet.setTimeSheetUpdate(LocalDateTime.now());
            Timesheet merge1 = entityManager.merge(timesheetRepository.save(timesheet));

            // Approval (para todos, mesmo que o usuário não seja gerente, por simplicidade)
            Approval approval = new Approval();
            approval.setApprovalId(UUID.randomUUID());
            approval.setTimesheet(merge1);
            approval.setUser(merge); // Em um cenário real seria outro usuário
            approval.setComments("Aprovado por script.");
            approval.setApprovalStatus(ApprovalStatus.APPROVED); // 1 = aprovado
            approval.setApprovalDateCreated(LocalDateTime.now());
            approval.setApprovalDateUpdate(LocalDateTime.now());
            approvalRepository.save(approval);

            // WorkEntry
            WorkEntry workEntry = new WorkEntry();
            workEntry.setWorkEntryId(UUID.randomUUID());
            workEntry.setTimesheetId(merge1);
            workEntry.setUserId(merge);
            workEntry.setStartTime(LocalDateTime.of(2025, 5, 10, 9, 0));
            workEntry.setEndTime(LocalDateTime.of(2025, 5, 10, 18, 0));
            workEntry.setDateCreated(LocalDateTime.now());
            workEntry.setDateUpdate(LocalDateTime.now());
            workEntryRepository.save(workEntry);
        }

        System.out.println("✅ Banco de dados populado com 20 usuários e dados relacionados.");
    }
}
