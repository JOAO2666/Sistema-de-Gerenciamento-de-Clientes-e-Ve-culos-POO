package model;

public class Perito extends Pessoa implements Identificavel {
    private String registroProfissional;
    private String especialidade;
    private String email;

    public Perito() {
        super(0, "Perito", "000.000.000-00", "0000-0000");
    }

    public Perito(int id, String nome, String registroProfissional,
                  String especialidade, String telefone, String email) {
        super(
                id,
                (nome != null && !nome.isBlank()) ? nome : "Perito",
                (registroProfissional != null && !registroProfissional.isBlank()) ? registroProfissional : "000.000.000-00",
                (telefone != null && !telefone.isBlank()) ? telefone : "0000-0000"
        );
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
        this.email = email;
    }

    public Perito(int id, String nome, String cpf, String telefone, String sexo,
                  String email, String registroProfissional, String especialidade) {
        super(id, nome, cpf, telefone);
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String exibirResumo() {
        return String.format("Perito %s (Reg: %s) - %s | Tel: %s",
                getNome(), registroProfissional, especialidade, getTelefone());
    }

    @Override
    public String toString() {
        return exibirResumo();
    }
}
