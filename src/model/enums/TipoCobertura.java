package model.enums;

/**
 * Representa os tipos de cobertura oferecidos nas apólices.
 */
public enum TipoCobertura {
    BASICA("Cobertura Básica"),
    COMPREENSIVA("Cobertura Compreensiva (Colisão, Incêndio e Roubo)"),
    DANOS_A_TERCEIROS("Responsabilidade Civil Facultativa (Danos a Terceiros)"),
    ROUBO_FURTO("Roubo e Furto"),
    INCENDIO("Incêndio e Raio"),
    ALAGAMENTO_ENCHENTE("Alagamento e Desastres Naturais"),
    VIDROS("Proteção de Vidros, Faróis e Retrovisores"),
    MORTE_NATURAL("Morte Natural"),
    MORTE_ACIDENTAL("Morte Acidental"),
    INVALIDEZ_PERMANENTE("Invalidez Permanente Total ou Parcial"),
    ASSISTENCIA_24H("Assistência 24 Horas");

    private final String descricao;

    TipoCobertura(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
