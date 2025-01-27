package io.github.samuelsantos20.time_sheet.model;

public enum ApprovalStatus {



    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String approvalStatus;

    ApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
