package model;

/**
 * Representa um imóvel segurável no sistema por uma apólice residencial.
 * Implementa Identificavel e Seguravel.
 */
public class Imovel implements Identificavel, Seguravel {
    private int id;
    private int clienteId;
    private String cep;
    private String endereco;
    private String cidade;
    private String estado;
    private double valorMercado;
    private double areaM2;
    private String tipoConstrucao;
    private boolean possuiAlarmeIncendio;

    public Imovel() {}

    public Imovel(int id, int clienteId, String cep, String endereco, String cidade,
                  String estado, double valorMercado, double areaM2, String tipoConstrucao,
                  boolean possuiAlarmeIncendio) {
        this.id = id;
        this.clienteId = clienteId;
        this.cep = cep;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.valorMercado = valorMercado;
        this.areaM2 = areaM2;
        this.tipoConstrucao = tipoConstrucao;
        this.possuiAlarmeIncendio = possuiAlarmeIncendio;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getValorMercado() {
        return valorMercado;
    }

    public void setValorMercado(double valorMercado) {
        this.valorMercado = valorMercado;
    }

    public double getAreaM2() {
        return areaM2;
    }

    public void setAreaM2(double areaM2) {
        this.areaM2 = areaM2;
    }

    public String getTipoConstrucao() {
        return tipoConstrucao;
    }

    public void setTipoConstrucao(String tipoConstrucao) {
        this.tipoConstrucao = tipoConstrucao;
    }

    public boolean isPossuiAlarmeIncendio() {
        return possuiAlarmeIncendio;
    }

    public void setPossuiAlarmeIncendio(boolean possuiAlarmeIncendio) {
        this.possuiAlarmeIncendio = possuiAlarmeIncendio;
    }

    @Override
    public double getValorAvaliado() {
        return valorMercado;
    }

    @Override
    public String getIdentificador() {
        return "CEP: " + cep + " - " + endereco;
    }

    @Override
    public String getDescricaoCompleta() {
        return String.format("Imóvel [ID: %d] %s, %s - %s/%s | Área: %.1fm² | Tipo: %s | Valor: R$ %.2f",
                id, endereco, cep, cidade, estado, areaM2, tipoConstrucao, valorMercado);
    }

    @Override
    public String toString() {
        return getDescricaoCompleta();
    }
}
