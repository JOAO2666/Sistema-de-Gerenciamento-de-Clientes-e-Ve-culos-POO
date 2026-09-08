# Sistema de Gerenciamento de Clientes e Veículos

Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos**, com o objetivo de aplicar na prática os principais conceitos estudados em sala de aula utilizando a linguagem Java.

**Linguagem:** Java  
**Disciplina:** Programação Orientada a Objetos  
**Professora:** Ana Emília  

---

## Sobre o projeto

O projeto consiste em um sistema de gerenciamento de clientes e veículos.

A proposta inicial é permitir o cadastro, consulta, alteração e exclusão de clientes, além do cadastro e associação de veículos a esses clientes.

Durante o desenvolvimento, o projeto também será utilizado para aplicar conceitos de Programação Orientada a Objetos, como:

- encapsulamento;
- herança;
- abstração;
- polimorfismo;
- associação entre objetos;
- organização em classes;
- separação de responsabilidades.

A estrutura do projeto foi criada antes da implementação completa para que cada integrante do grupo possa desenvolver sua parte separadamente.

---

## Funcionalidades previstas

As principais funcionalidades previstas para o sistema são:

- cadastrar clientes;
- listar clientes;
- alterar dados de clientes;
- remover clientes;
- cadastrar veículos;
- associar veículos a clientes;
- listar veículos de um cliente;
- armazenar os dados em arquivos;
- validar dados informados pelo usuário.

Outras funcionalidades poderão ser adicionadas conforme os conteúdos forem avançando durante a disciplina.

---

## Conceitos de POO

### Encapsulamento

Os atributos das classes serão mantidos com acesso controlado, utilizando principalmente atributos privados e métodos de acesso quando necessário.

Exemplo:

```java
private String nome;

public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}

## Alterações incorporadas do Pull Request #1

As classes `Pessoa` e `Cliente` foram implementadas a partir do commit do
colaborador Ícaro Rafael. O detalhamento completo das alterações está em
[`docs/alteracoes-pr-1.md`](docs/alteracoes-pr-1.md).
