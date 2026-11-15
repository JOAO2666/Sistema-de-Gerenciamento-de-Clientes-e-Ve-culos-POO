package model;

import model.enums.StatusApolice;
import model.enums.TipoCobertura;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Apolice implements Identificavel {
    private int id;
    private String numeroApolice;
    private Cliente cliente;
    private double valorPremio;
    private double valorFranquia;
    private LocalDate dataInicioVigencia;
    private LocalDate dataFimVigencia;
    private StatusApolice status;
    private List<Cobertura> coberturas;
    private List<Pagamento> pagamentos;
    private List<Sinistro> sinistros;

    public Apolice() {
        this.coberturas = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
        this.sinistros = new ArrayList<>();
        this.status = StatusApolice.PROPOSTA;
    }

    public Apolice(int id, String numeroApolice, Cliente cliente, double valorPremio,
                   double valorFranquia, LocalDate dataInicioVigencia, LocalDate dataFimVigencia) {
        this.id = id;
        this.numeroApolice = numeroApolice;
        this.cliente = cliente;
        this.valorPremio = valorPremio;
        this.valorFranquia = valorFranquia;
        this.dataInicioVigencia = dataInicioVigencia;
        this.dataFimVigencia = dataFimVigencia;
        this.status = StatusApolice.PROPOSTA;
        this.coberturas = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
        this.sinistros = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValorPremio() {
        return valorPremio;
    }

    public void setValorPremio(double valorPremio) {
        this.valorPremio = valorPremio;
    }

    public double getValorFranquia() {
        return valorFranquia;
    }

    public void setValorFranquia(double valorFranquia) {
        this.valorFranquia = valorFranquia;
    }

    public LocalDate getDataInicioVigencia() {
        return dataInicioVigencia;
    }

    public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }

    public LocalDate getDataFimVigencia() {
        return dataFimVigencia;
    }

    public void setDataFimVigencia(LocalDate dataFimVigencia) {
        this.dataFimVigencia = dataFimVigencia;
    }

    public StatusApolice getStatus() {
        return status;
    }

    public void setStatus(StatusApolice status) {
        this.status = status;
    }

    public List<Cobertura> getCoberturas() {
        return coberturas;
    }

    public void setCoberturas(List<Cobertura> coberturas) {
        this.coberturas = coberturas != null ? coberturas : new ArrayList<>();
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos != null ? pagamentos : new ArrayList<>();
    }

    public List<Sinistro> getSinistros() {
        return sinistros;
    }

    public void setSinistros(List<Sinistro> sinistros) {
        this.sinistros = sinistros != null ? sinistros : new ArrayList<>();
    }

    public void adicionarCobertura(Cobertura cobertura) {
        if (cobertura != null) {
            this.coberturas.add(cobertura);
        }
    }

    public void adicionarCobertura(String nome, double valorLimite, double valorFranquia, TipoCobertura tipo) {
        int novoId = this.coberturas.size() + 1;
        this.coberturas.add(new Cobertura(novoId, nome, nome, valorLimite, valorFranquia, tipo));
    }

    public void adicionarPagamento(Pagamento pagamento) {
        if (pagamento != null) {
            this.pagamentos.add(pagamento);
        }
    }

    public void adicionarSinistro(Sinistro sinistro) {
        if (sinistro != null) {
            this.sinistros.add(sinistro);
        }
    }

    public boolean isVencida() {
        return dataFimVigencia != null && LocalDate.now().isAfter(dataFimVigencia);
    }

    public abstract double calcularPremioBase();

    public abstract String getTipoApolice();

    public abstract void exibirDetalhesApolice();

    @Override
    public String toString() {
        String clienteNome = (cliente != null) ? cliente.getNome() : "Não informado";
        return String.format("ID %d - [%s] N° %s | Segurado: %s | Prêmio: R$ %.2f | Vigência: %s a %s | %s",
                id, getTipoApolice(), numeroApolice, clienteNome, valorPremio,
                dataInicioVigencia, dataFimVigencia, status.getDescricao());
    }
}
