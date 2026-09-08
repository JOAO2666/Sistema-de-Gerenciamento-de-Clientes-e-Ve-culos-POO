# Alterações incorporadas do Pull Request #1

Este documento registra exatamente o que foi feito no commit do colaborador
Ícaro Rafael (`fd138cc`, `feat: implementa Pessoa e Cliente`).

## Arquivos alterados pelo colaborador

O commit alterou somente estes dois arquivos:

- `src/model/Pessoa.java`
- `src/model/Cliente.java`

O Pull Request mostra também alterações no `README.md` porque ele foi aberto
contra a branch `develop` e carregou um commit anterior do repositório
(`76ebea7`, feito por João Emanuel). O `README.md` não faz parte do commit do
Ícaro.

## `Pessoa.java`

- Criou a classe abstrata `Pessoa` no pacote `model`.
- Adicionou os atributos privados `id`, `nome`, `cpf` e `telefone`.
- Criou o construtor que recebe os quatro dados.
- Criou getters e setters para todos os atributos.
- Validou `nome`, `cpf` e `telefone` contra valores nulos ou vazios, lançando
  `IllegalArgumentException`.
- Declarou o método abstrato `exibirResumo()`.
- Implementou `toString()` para representar uma pessoa.

## `Cliente.java`

- Criou `Cliente` herdando de `Pessoa` com `extends`.
- Adicionou os atributos privados `sexo` e `email`.
- Criou o construtor completo, reutilizando o construtor da classe base com
  `super(...)`.
- Criou getters e setters para `sexo` e `email`.
- Implementou `exibirResumo()` com nome, CPF, telefone, sexo e e-mail.
- Implementou `toCsv()`, usando `;` como separador e seis campos na ordem:
  `id;nome;cpf;telefone;sexo;email`.
- Implementou `fromCsv(String csvLine)`, que separa a linha por `;` e cria um
  novo `Cliente` a partir dos seis campos.
- Implementou `toString()` com todos os dados do cliente.

## Observações do código original

Estas observações não alteram o que foi feito pelo colaborador; servem para
orientar as próximas etapas:

- A validação de formato de CPF e telefone ficou indicada nos comentários para
  uma implementação futura.
- `sexo` e `email` são armazenados sem validação de conteúdo.
- `fromCsv` foi implementado como método de instância; o UML do projeto o
  descreve como estático. Essa decisão pode ser ajustada posteriormente, caso
  o grupo queira alinhar código e documentação.
- O parser CSV assume exatamente seis campos separados por `;` e não trata
  separadores dentro dos valores.
