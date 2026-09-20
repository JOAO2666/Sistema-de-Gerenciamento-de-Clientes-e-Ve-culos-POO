package model;

import model.enums.EstadoCivil;

/**
 * Representa um cliente Pessoa Física. Herda de Cliente.
 */
public class PessoaFisica extends Cliente {
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String sexo;
    private EstadoCivil estadoCivil;

    public PessoaFisica() {
        super();
    }

    public PessoaFisica(int id, String nome, String email, String telefone, String endereco,
                        String cpf, String rg, String dataNascimento, String sexo, EstadoCivil estadoCivil) {
        super(id, nome, email, telefone, endereco);
        this.cpf = cpf;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil != null ? estadoCivil : EstadoCivil.SOLTEIRO;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    @Override
    public String getDocumentoPrincipal() {
        return cpf;
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Física";
    }

    @Override
    public String exibirResumo() {
        return String.format("PF: %s (CPF: %s) | Nasc: %s | Sexo: %s | Estado Civil: %s | Contato: %s / %s",
                getNome(), cpf, dataNascimento, sexo, estadoCivil.getDescricao(), getTelefone(), getEmail());
    }
}