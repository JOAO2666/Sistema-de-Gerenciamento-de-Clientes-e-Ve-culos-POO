/*
 * Projeto: Sistema de Gerenciamento de Clientes e Veículos
 * Disciplina: Programação Orientada a Objetos
 * Arquivo: Pessoa.java
 * Autor: Ícaro Rafael
 * Professor: Ana Emília de Melo Queiroz
 * Descrição: Classe abstrata que representa uma pessoa.
 */

package model;

// ----- Classe abstrata de pessoa -----
public abstract class Pessoa {
    // ----- Atributos -----
    private int id;
    private String nome;
    private String cpf;
    private String telefone;

    // ----- Construtores -----
    public Pessoa(int id, String nome, String cpf, String telefone) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
    }

    // ----- Getters e Setters -----
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        // atenção: cpfs necessitam de verificações mais complexas. Irei implementar depois.
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF inválido");
        }

        this.cpf = cpf;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        // Atenção: telefones necessitam de verificações mais complexas. Irei implementar depois.
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        this.telefone = telefone;
    }

    // ----- Método abstrato para a exibição do resumo da pessoa -----
    public abstract String exibirResumo();

    // ----- Representação textual da pessoa -----
    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}