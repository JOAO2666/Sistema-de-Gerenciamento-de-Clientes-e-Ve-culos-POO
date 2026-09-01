# Sistema de Gerenciamento de Clientes e Veículos — POO

> **Projeto Acadêmico de Programação Orientada a Objetos**  
> **Linguagem:** Java (v17+)  
> **Docente:** Prof.ª Ana Emília  
> **Repositório:** [JOAO2666/Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO](https://github.com/JOAO2666/Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO)

---

## 📌 Sobre o Projeto

O **Sistema de Gerenciamento de Clientes e Veículos** é uma aplicação completa em Java projetada para demonstrar na prática todos os pilares e boas práticas da **Programação Orientada a Objetos (POO)**. 

O sistema substitui abordagens monolíticas/estruturadas por uma arquitetura em camadas (**MVC**), com persistência em arquivos de texto plano (CSV), integridade relacional, tratamento defensivo de erros e polimorfismo dinâmico.

---

## 🎯 Pilares de POO Demonstrados

1. **Encapsulamento:**
   - Todos os atributos de todas as entidades de domínio possuem visibilidade `private`.
   - Acesso e mutação são intermediados por métodos `get` e `set` com validações de consistência (ex: restrição contra nomes nulos ou valores negativos).

2. **Herança (Relação É-UM):**
   - `Cliente extends Pessoa`: Especialização da entidade Pessoa com reutilização de construtores através da instrução `super(...)`.
   - `Carro extends Veiculo` e `Moto extends Veiculo`: Especialização da entidade genérica de transporte.

3. **Abstração:**
   - Classes base declaradas como `abstract` (`Pessoa` e `Veiculo`), garantindo que apenas especializações concretas sejam instanciadas.
   - Assinatura de métodos abstratos (`exibirResumo()`, `exibirDetalhes()`, `getTipo()`, `toCsv()`).

4. **Polimorfismo (Inclusão / Subtipo):**
   - Métodos abstratos da superclasse são sobrescritos com a anotação `@Override` em `Carro` e `Moto`.
   - O controlador manipula coleções genéricas `List<Veiculo>`, executando dinamicamente em tempo de execução o comportamento específico de cada subclasse.

5. **Associação e Integridade Relacional:**
   - Cada veículo está associado a um cliente via `clienteId`.
   - O sistema impede a criação de veículos para clientes inexistentes e oferece suporte à exclusão em cascata.

---

## 👥 Divisão de Responsabilidades da Equipe

| Integrante | Responsabilidade no Projeto | Módulos Desenvolvidos |
| :--- | :--- | :--- |
| **João** | Integração, Arquitetura MVC, Controller e Git | `Main.java`, `ApplicationController.java`, Git Setup |
| **Ícaro** | Domínio Base, Herança e Encapsulamento | `Pessoa.java`, `Cliente.java` |
| **Alexandre** | Abstração de Veículos e Especialização Carro | `Veiculo.java`, `Carro.java` |
| **Hícaro** | Especialização Moto e Polimorfismo | `Moto.java`, Coleções polimórficas |
| **Natanael** | Persistência em Arquivos, Documentação e Testes | `ClienteRepository.java`, `VeiculoRepository.java`, `IdGenerator.java`, `docs/`, `test/` |

---

## 🗂️ Estrutura de Diretórios

```text
projeto-poo-clientes-veiculos/
├── README.md                  # Documentação principal do projeto
├── .gitignore                 # Arquivos ignorados pelo Git
├── docs/                      # Documentação de Engenharia de Software
│   ├── requisitos.md          # Requisitos Funcionais e Não-Funcionais
│   ├── planejamento.md        # Planejamento da equipe e fluxo de branches
│   └── uml.md                 # Diagramas de Classes e Sequência (Mermaid)
├── src/                       # Código-fonte da aplicação
│   ├── Main.java              # Ponto de entrada (Entrypoint)
│   ├── model/                 # Entidades e regras de domínio (POO)
│   │   ├── Pessoa.java        # Superclasse abstrata
│   │   ├── Cliente.java       # Subclasse concreta
│   │   ├── Veiculo.java       # Superclasse abstrata de veículos
│   │   ├── Carro.java         # Subclasse de veículo
│   │   └── Moto.java          # Subclasse de veículo
│   ├── repository/            # Camada de persistência em arquivos
│   │   ├── IdGenerator.java   # Controle de IDs sequenciais
│   │   ├── ClienteRepository.java # Operações CRUD de clientes
│   │   └── VeiculoRepository.java # Operações CRUD polimórficas
│   ├── view/                  # Camada de apresentação e interface
│   │   ├── Menu.java          # Menus e formatação de texto
│   │   └── Input.java         # Tratamento de entradas do usuário
│   └── controller/            # Camada controladora (Orquestração MVC)
│       └── ApplicationController.java
├── test/                      # Testes automatizados
│   └── TestePOO.java          # Suíte de testes de validação dos pilares
└── data/                      # Diretório de armazenamento dos arquivos .txt
    └── .gitkeep
```

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
- **Java JDK** versão 17 ou superior instalado (ex: OpenJDK / Temurin / Oracle JDK).

### 1. Compilação
No terminal (PowerShell, CMD ou Bash), na raiz do projeto:

```bash
javac -d bin -sourcepath src src/Main.java src/model/*.java src/repository/*.java src/view/*.java src/controller/*.java test/TestePOO.java
```

### 2. Execução da Aplicação Principal
```bash
java -cp bin Main
```

### 3. Execução dos Testes Automatizados de POO
```bash
java -cp bin test.TestePOO
```

---

## 📊 Documentação Técnica
- [Especificação de Requisitos](docs/requisitos.md)
- [Planejamento de Branches e Git Flow](docs/planejamento.md)
- [Diagramas de Classes e Sequência UML](docs/uml.md)
