package model.enums;

/**
 * Representa as categorias de sinistros que podem ser acionados.
 */
public enum TipoSinistro {
    COLISAO("Colisão de Veículo"),
    ROUBO_FURTO("Roubo ou Furto"),
    INCENDIO("Incêndio"),
    ALAGAMENTO("Alagamento / Enchente"),
    DANOS_ELETRICOS("Danos Elétricos"),
    DANOS_A_TERCEIROS("Danos a Terceiros"),
    MORTE("Óbito do Segurado"),
    INVALIDEZ("Invalidez por Acidente");

    private final String descricao;

    TipoSinistro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
