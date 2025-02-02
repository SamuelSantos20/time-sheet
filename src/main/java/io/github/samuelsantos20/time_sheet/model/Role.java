package io.github.samuelsantos20.time_sheet.model;

public enum Role {
    EMPLOYEE("Funcionário"),
    MANAGER("Gerente");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
