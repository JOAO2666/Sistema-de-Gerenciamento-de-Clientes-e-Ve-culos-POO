package model;

import model.enums.TipoSinistro;
import java.time.LocalDate;

/**
 * Representa um sinistro reportado pelo segurado.
 * Implementa a interface Identificavel.
 */
public class Sinistro implements Identificavel {
    private int id;
    private String numeroProtocolo;
    private int apoliceId;
    private LocalDate dataOcorrencia;
    private LocalDate dataRegistro;
    private TipoSinistro tipoSinistro;
    private String descricao;
    private double valorEstimadoPrejuizo;
    private double valorIndenizado;
    private Perito perito;
    private OficinaParceira oficinaParceira;
    private String statusSinistro; // REGISTRADO, EM_ANALISE, APROVADO, PAGO, RECUSADO
    private String laudoPericial;

    public Sinistro() {
        this.dataRegistro = LocalDate.now();
        this.statusSinistro = "REGISTRADO";
    }

    public Sinistro(int id, String numeroProtocolo, int apoliceId, LocalDate dataOcorrencia,
                    TipoSinistro tipoSinistro, String descricao, double valorEstimadoPrejuizo) {
        this.id = id;
        this.numeroProtocolo = numeroProtocolo;
        this.apoliceId = apoliceId;
        this.dataOcorrencia = dataOcorrencia;
        this.dataRegistro = LocalDate.now();
        this.tipoSinistro = tipoSinistro != null ? tipoSinistro : TipoSinistro.COLISAO;
        this.descricao = descricao;
        this.valorEstimadoPrejuizo = valorEstimadoPrejuizo;
        this.valorIndenizado = 0.0;
        this.statusSinistro = "REGISTRADO";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public int getApoliceId() {
        return apoliceId;
    }

    public void setApoliceId(int apoliceId) {
        this.apoliceId = apoliceId;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public TipoSinistro getTipoSinistro() {
        return tipoSinistro;
    }

    public void setTipoSinistro(TipoSinistro tipoSinistro) {
        this.tipoSinistro = tipoSinistro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorEstimadoPrejuizo() {
        return valorEstimadoPrejuizo;
    }

    public void setValorEstimadoPrejuizo(double valorEstimadoPrejuizo) {
        this.valorEstimadoPrejuizo = valorEstimadoPrejuizo;
    }

    public double getValorIndenizado() {
        return valorIndenizado;
    }

    public void setValorIndenizado(double valorIndenizado) {
        this.valorIndenizado = valorIndenizado;
    }

    public Perito getPerito() {
        return perito;
    }

    public void setPerito(Perito perito) {
        this.perito = perito;
    }

    public OficinaParceira getOficinaParceira() {
        return oficinaParceira;
    }

    public void setOficinaParceira(OficinaParceira oficinaParceira) {
        this.oficinaParceira = oficinaParceira;
    }

    public String getStatusSinistro() {
        return statusSinistro;
    }

    public void setStatusSinistro(String statusSinistro) {
        this.statusSinistro = statusSinistro;
    }

    public String getLaudoPericial() {
        return laudoPericial;
    }

    public void setLaudoPericial(String laudoPericial) {
        this.laudoPericial = laudoPericial;
    }

    @Override
    public String toString() {
        return String.format("Sinistro [%s] ID: %d | Apólice: %d | Tipo: %s | Prejuízo: R$ %.2f | Status: %s | Indenização: R$ %.2f",
                numeroProtocolo, id, apoliceId, tipoSinistro.getDescricao(),
                valorEstimadoPrejuizo, statusSinistro, valorIndenizado);
    }
}
