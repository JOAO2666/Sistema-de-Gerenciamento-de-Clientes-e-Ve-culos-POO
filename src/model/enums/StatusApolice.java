package model.enums;

/**
 * Representa os possíveis status de uma apólice no sistema.
 */
public enum StatusApolice {
    PROPOSTA("Proposta em Análise"),
    ATIVA("Apólice Ativa"),
    SUSPENSA("Apólice Suspensa"),
    CANCELADA("Apólice Cancelada"),
    EXPIRADA("Apólice Expirada / Vencida");

    private final String descricao;

    StatusApolice(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
