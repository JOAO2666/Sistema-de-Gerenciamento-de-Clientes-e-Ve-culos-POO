/*
 * Projeto: Sistema de Gerenciamento de Clientes e Veículos
 * Disciplina: Programação Orientada a Objetos
 * Arquivo: Cliente.java
 * Autor: Ícaro Rafael
 * Professor: Ana Emília de Melo Queiroz
 * Descrição: Classe que representa um cliente.
 */

package model;

// ----- Classe cliente herdando pessoa -----
public class Cliente extends Pessoa {
    // ----- Atributos -----
    private String sexo;
    private String email;

    // ----- Construtores -----
    public Cliente(int id, String nome, String cpf, String telefone,
                   String sexo, String email) {

        super(id, nome, cpf, telefone);

        setSexo(sexo);
        setEmail(email);
    }

    // ----- Getters e Setters -----
    public String getSexo() {
        return this.sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ----- Exibe o resumo do cliente -----
    @Override
    public String exibirResumo() {
        return "Cliente: " + getNome() +
                " | CPF: " + getCpf() +
                " | Telefone: " + getTelefone() +
                " | Sexo: " + sexo +
                " | Email: " + email;
    }

    // ----- Transforma o cliente em CSV -----
    public String toCsv() {
        return getId() + ";" +
                getNome() + ";" +
                getCpf() + ";" +
                getTelefone() + ";" +
                sexo + ";" +
                email;
    }

    // ----- Cria um cliente através de um CSV -----
    public Cliente fromCsv(String csvLine) {
        String[] dados = csvLine.split(";");

        return new Cliente(
                Integer.parseInt(dados[0]),
                dados[1],
                dados[2],
                dados[3],
                dados[4],
                dados[5]
        );
    }

    // ----- Representação textual do cliente -----
    @Override
    public String toString() {
        return "Cliente: " +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", telefone='" + getTelefone() + '\'' +
                ", sexo='" + sexo + '\'' +
                ", email='" + email + '\'';
    }
}