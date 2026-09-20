package model;

/**
 * Representa um veículo no sistema, que pode ser segurado por uma apólice de auto.
 * Implementa Identificavel e Seguravel.
 */
public class Veiculo implements Identificavel, Seguravel {
    private int id;
    private int clienteId;
    private String marca;
    private String modelo;
    private int ano;
    private String placa;
    private String renavam;
    private String chassi;
    private double valorFipe;

    public Veiculo() {}

    public Veiculo(int id, int clienteId, String marca, String modelo, int ano,
                   String placa, String renavam, String chassi, double valorFipe) {
        this.id = id;
        this.clienteId = clienteId;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.placa = placa;
        this.renavam = renavam;
        this.chassi = chassi;
        this.valorFipe = valorFipe;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public double getValorFipe() {
        return valorFipe;
    }

    public void setValorFipe(double valorFipe) {
        this.valorFipe = valorFipe;
    }

    @Override
    public double getValorAvaliado() {
        return valorFipe;
    }

    @Override
    public String getIdentificador() {
        return placa;
    }

    @Override
    public String getDescricaoCompleta() {
        return String.format("Veículo [ID: %d] %s %s (%d) | Placa: %s | FIPE: R$ %.2f",
                id, marca, modelo, ano, placa, valorFipe);
    }

    @Override
    public String toString() {
        return getDescricaoCompleta();
    }
}
