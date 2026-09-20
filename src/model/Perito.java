package model;

/**
 * Representa um perito avaliador de sinistros credenciado.
 */
public class Perito implements Identificavel {
    private int id;
    private String nome;
    private String registroProfissional;
    private String especialidade;
    private String telefone;
    private String email;

    public Perito() {}

    public Perito(int id, String nome, String registroProfissional,
                  String especialidade, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
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

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Perito [ID: %d] %s | Reg: %s | Esp: %s | Contato: %s",
                id, nome, registroProfissional, especialidade, telefone);
    }
}
