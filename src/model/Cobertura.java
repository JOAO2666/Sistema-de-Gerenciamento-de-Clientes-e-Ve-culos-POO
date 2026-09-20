package model;

import model.enums.TipoCobertura;

/**
 * Representa uma cobertura contratada ou disponível para uma apólice.
 */
public class Cobertura implements Identificavel {
    private int id;
    private String nome;
    private String descricao;
    private double valorLimite;
    private double valorFranquia;
    private TipoCobertura tipo;

    public Cobertura() {}

    public Cobertura(int id, String nome, String descricao, double valorLimite,
                     double valorFranquia, TipoCobertura tipo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorLimite = valorLimite;
        this.valorFranquia = valorFranquia;
        this.tipo = tipo != null ? tipo : TipoCobertura.BASICA;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorLimite() {
        return valorLimite;
    }

    public void setValorLimite(double valorLimite) {
        this.valorLimite = valorLimite;
    }

    public double getValorFranquia() {
        return valorFranquia;
    }

    public void setValorFranquia(double valorFranquia) {
        this.valorFranquia = valorFranquia;
    }

    public TipoCobertura getTipo() {
        return tipo;
    }

    public void setTipo(TipoCobertura tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return String.format("Cobertura: %s (%s) | Limite: R$ %.2f | Franquia: R$ %.2f",
                nome, tipo.getDescricao(), valorLimite, valorFranquia);
    }
}
