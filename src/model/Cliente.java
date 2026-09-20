package model;

/**
 * Classe Abstrata base que representa um Cliente do sistema de seguros.
 * Implementa a interface Identificavel e define métodos abstratos para especializações.
 */
public abstract class Cliente implements Identificavel {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;

    public Cliente() {}

    public Cliente(int id, String nome, String email, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
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
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna o documento principal do cliente (CPF para Pessoa Física, CNPJ para Pessoa Jurídica).
     */
    public abstract String getDocumentoPrincipal();

    /**
     * Retorna a descrição do tipo de cliente.
     */
    public abstract String getTipoCliente();

    /**
     * Exibe o resumo formatado dos dados do cliente.
     */
    public abstract String exibirResumo();

    @Override
    public String toString() {
        return String.format("[%s] ID: %d | Nome: %s | Doc: %s | Tel: %s | Email: %s",
                getTipoCliente(), id, nome, getDocumentoPrincipal(), telefone, email);
    }
}