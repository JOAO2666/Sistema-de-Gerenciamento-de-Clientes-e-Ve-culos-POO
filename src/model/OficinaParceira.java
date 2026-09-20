package model;

/**
 * Representa uma oficina conveniada para reparos em caso de sinistros de veículos.
 */
public class OficinaParceira implements Identificavel {
    private int id;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String responsavel;
    private double descontoConvenio; // Percentual de desconto (ex: 15.0 para 15%)

    public OficinaParceira() {}

    public OficinaParceira(int id, String nomeFantasia, String cnpj, String endereco,
                           String telefone, String responsavel, double descontoConvenio) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.responsavel = responsavel;
        this.descontoConvenio = descontoConvenio;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public double getDescontoConvenio() {
        return descontoConvenio;
    }

    public void setDescontoConvenio(double descontoConvenio) {
        this.descontoConvenio = descontoConvenio;
    }

    @Override
    public String toString() {
        return String.format("Oficina [ID: %d] %s | CNPJ: %s | Tel: %s | Desconto: %.1f%%",
                id, nomeFantasia, cnpj, telefone, descontoConvenio);
    }
}
