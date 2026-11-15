package model;

import java.time.LocalDate;

public class Pagamento implements Identificavel {
    private int id;
    private int apoliceId;
    private int numeroParcela;
    private int totalParcelas;
    private double valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private boolean pago;
    private String formaPagamento;

    public Pagamento() {}

    public Pagamento(int id, int apoliceId, int numeroParcela, int totalParcelas,
                     double valor, LocalDate dataVencimento, String formaPagamento) {
        this.id = id;
        this.apoliceId = apoliceId;
        this.numeroParcela = numeroParcela;
        this.totalParcelas = totalParcelas;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.pago = false;
        this.formaPagamento = formaPagamento;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getApoliceId() {
        return apoliceId;
    }

    public void setApoliceId(int apoliceId) {
        this.apoliceId = apoliceId;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public int getTotalParcelas() {
        return totalParcelas;
    }

    public void setTotalParcelas(int totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void registrarQuitacao() {
        this.pago = true;
        this.dataPagamento = LocalDate.now();
    }

    public void registrarQuitacao(String forma) {
        registrarQuitacao();
        if (forma != null && !forma.isBlank()) {
            this.formaPagamento = forma.trim().toUpperCase();
        }
    }

    @Override
    public String toString() {
        String forma = (formaPagamento != null && !formaPagamento.isBlank()) ? formaPagamento : "-";
        return String.format("ID %d | Apólice %d | Parcela %d/%d | R$ %.2f | Venc: %s | %s (%s)",
                id, apoliceId, numeroParcela, totalParcelas, valor,
                dataVencimento, (pago ? "Pago em " + dataPagamento : "Pendente"), forma);
    }
}
