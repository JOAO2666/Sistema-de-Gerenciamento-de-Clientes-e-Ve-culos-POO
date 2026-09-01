# Especificação de Requisitos do Sistema

**Projeto:** Sistema de Gerenciamento de Clientes e Veículos  
**Disciplina:** Programação Orientada a Objetos (POO)  
**Docente:** Prof.ª Ana Emília  

---

## 1. Visão Geral do Sistema

O **Sistema de Gerenciamento de Clientes e Veículos** é uma aplicação em Java desenvolvida com arquitetura modular em camadas (MVC) e persistência em arquivos de texto plano. O sistema tem como finalidade gerenciar cadastros de clientes e seus respectivos veículos automotores (Carros e Motos), demonstrando na prática os princípios fundamentais da Programação Orientada a Objetos: **Encapsulamento**, **Herança**, **Abstração** e **Polimorfismo**.

---

## 2. Requisitos Funcionais (RF)

### Módulo de Clientes
- **[RF01] Cadastrar Cliente:** O sistema deve permitir o cadastro de novos clientes com ID gerado automaticamente, Nome, CPF, Telefone, Sexo e E-mail.
- **[RF02] Listar Clientes:** O sistema deve exibir todos os clientes cadastrados de forma clara e resumida.
- **[RF03] Buscar Cliente por ID:** O sistema deve permitir consultar um cliente pelo seu identificador único, exibindo todos os seus dados e a contagem de veículos vinculados.
- **[RF04] Atualizar Cliente:** O sistema deve permitir a alteração dos dados cadastrais de um cliente existente.
- **[RF05] Remover Cliente:** O sistema deve permitir a remoção de um cliente, com proteção contra órfãos e opção de exclusão em cascata dos veículos pertencentes a ele.

### Módulo de Veículos (Carros e Motos)
- **[RF06] Cadastrar Carro:** O sistema deve permitir cadastrar um Carro vinculado a um cliente válido, contendo Marca, Modelo, Ano, Placa, Valor Estimado, Quantidade de Portas e Tipo de Combustível.
- **[RF07] Cadastrar Moto:** O sistema deve permitir cadastrar uma Moto vinculada a um cliente válido, contendo Marca, Modelo, Ano, Placa, Valor Estimado, Cilindradas (cc) e Indicação de Partida Elétrica.
- **[RF08] Listar Veículos por Cliente:** O sistema deve listar todos os veículos vinculados a um determinado cliente, exibindo detalhes específicos de cada tipo polimorficamente.
- **[RF09] Listar Todos os Veículos:** O sistema deve listar todos os veículos registrados no sistema.
- **[RF10] Remover Veículo:** O sistema deve permitir a remoção individual de um veículo pelo seu ID.

### Módulo de Relatórios e Polimorfismo
- **[RF11] Demonstração Polimórfica:** O sistema deve iterar sobre coleções genéricas `List<Veiculo>` e acionar dinamicamente métodos sobrescritos (`exibirDetalhes()` e `getTipo()`), comprovando polimorfismo de subtipo em tempo de execução.
- **[RF12] Relatório de Patrimônio:** O sistema deve totalizar a quantidade de clientes, veículos e o valor monetário total do patrimônio cadastrado.

---

## 3. Requisitos Não-Funcionais (RNF)

- **[RNF01] Linguagem:** Desenvolvido em Java 17+ (compatível com Java 21 LTS).
- **[RNF02] Arquitetura:** Padrão arquitetural em camadas separadas (`model`, `repository`, `view`, `controller`).
- **[RNF03] Paradigma:** 100% Orientado a Objetos (sem código estruturado monolítico no `main`).
- **[RNF04] Persistência:** Armazenamento em arquivos de texto plano (.txt em formato CSV) na pasta `data/`.
- **[RNF05] Robustez de Entrada:** Tratamento defensivo de entradas do usuário para evitar encerramento abrupto por exceções (`NumberFormatException`, `InputMismatchException`).
- **[RNF06] Integridade Relacional:** Validação estrita de existência de cliente ao cadastrar veículo (evitar veículos órfãos).
- **[RNF07] Controle de Versão:** Versionamento via Git com histórico de commits semânticos e branches temáticas.

---

## 4. Regras de Negócio (RN)

- **[RN01] Unicidade de ID:** IDs de clientes e veículos são sequenciais, únicos e gerenciados de forma persistente.
- **[RN02] Existência Prévia:** Nenhum veículo pode ser cadastrado para um `clienteId` que não exista no sistema.
- **[RN03] Integridade na Exclusão:** Ao excluir um cliente que possua veículos, o usuário deve ser advertido e ter a opção de exclusão em cascata.
