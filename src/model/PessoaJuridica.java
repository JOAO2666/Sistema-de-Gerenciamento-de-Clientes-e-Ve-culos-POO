package model;

/**
 * Representa um cliente Pessoa Jurídica. Herda de Cliente.
 */
public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String razaoSocial;
    private String inscricaoEstadual;
    private String nomeFantasia;
    private String representanteLegal;

    public PessoaJuridica() {
        super();
    }

    public PessoaJuridica(int id, String nome, String email, String telefone, String endereco,
                          String cnpj, String razaoSocial, String inscricaoEstadual,
                          String nomeFantasia, String representanteLegal) {
        super(id, nome, email, telefone, endereco);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.inscricaoEstadual = inscricaoEstadual;
        this.nomeFantasia = nomeFantasia;
        this.representanteLegal = representanteLegal;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    @Override
    public String getDocumentoPrincipal() {
        return cnpj;
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Jurídica";
    }

    @Override
    public String exibirResumo() {
        return String.format("PJ: %s (%s) | CNPJ: %s | IE: %s | Rep. Legal: %s | Contato: %s / %s",
                getNome(), razaoSocial, cnpj, inscricaoEstadual, representanteLegal, getTelefone(), getEmail());
    }
}
