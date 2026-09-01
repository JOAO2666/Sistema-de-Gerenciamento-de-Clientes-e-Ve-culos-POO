# Diagramas UML do Sistema (POO)

Este documento contém os diagramas de modelagem orientada a objetos do sistema, renderizáveis diretamente pelo GitHub via sintaxe **Mermaid**.

---

## 1. Diagrama de Classes Completo

```mermaid
classDiagram
    direction TB

    %% Pacote Model
    class Pessoa {
        <<abstract>>
        -int id
        -String nome
        -String cpf
        -String telefone
        +getId() int
        +setId(int id) void
        +getNome() String
        +setNome(String nome) void
        +getCpf() String
        +setCpf(String cpf) void
        +getTelefone() String
        +setTelefone(String telefone) void
        +exibirResumo()* String
        +toString() String
    }

    class Cliente {
        -String sexo
        -String email
        +getSexo() String
        +setSexo(String sexo) void
        +getEmail() String
        +setEmail(String email) void
        +exibirResumo() String
        +toCsv() String
        +fromCsv(String csvLine)$ Cliente
        +toString() String
    }

    class Veiculo {
        <<abstract>>
        -int id
        -int clienteId
        -String marca
        -String modelo
        -int ano
        -String placa
        -double valor
        +getId() int
        +setId(int id) void
        +getClienteId() int
        +setClienteId(int id) void
        +getMarca() String
        +setMarca(String marca) void
        +getModelo() String
        +setModelo(String modelo) void
        +getAno() int
        +setAno(int ano) void
        +getPlaca() String
        +setPlaca(String placa) void
        +getValor() double
        +setValor(double valor) void
        +getTipo()* String
        +exibirDetalhes()* void
        +toCsv()* String
        +toString() String
    }

    class Carro {
        -int quantidadePortas
        -String tipoCombustivel
        +getQuantidadePortas() int
        +setQuantidadePortas(int qtd) void
        +getTipoCombustivel() String
        +setTipoCombustivel(String combustivel) void
        +getTipo() String
        +exibirDetalhes() void
        +toCsv() String
        +fromCsvParts(String[] parts)$ Carro
    }

    class Moto {
        -int cilindradas
        -boolean partidaEletrica
        +getCilindradas() int
        +setCilindradas(int cc) void
        +isPartidaEletrica() boolean
        +setPartidaEletrica(boolean partida) void
        +getTipo() String
        +exibirDetalhes() void
        +toCsv() String
        +fromCsvParts(String[] parts)$ Moto
    }

    %% Relações de Herança (É-UM)
    Pessoa <|-- Cliente : Herança (extends)
    Veiculo <|-- Carro : Herança e Polimorfismo (extends)
    Veiculo <|-- Moto : Herança e Polimorfismo (extends)

    %% Relações de Associação (Possui)
    Cliente "1" -- "0..*" Veiculo : possui (clienteId)

    %% Pacote Repository
    class IdGenerator {
        -String filename
        +getNextId() int
    }

    class ClienteRepository {
        -String filename
        -IdGenerator idGenerator
        +save(Cliente c) Cliente
        +findAll() List~Cliente~
        +findById(int id) Cliente
        +existsById(int id) boolean
        +update(Cliente c) boolean
        +deleteById(int id) boolean
    }

    class VeiculoRepository {
        -String filename
        -IdGenerator idGenerator
        +save(Veiculo v) Veiculo
        +findAll() List~Veiculo~
        +findById(int id) Veiculo
        +findByClienteId(int clienteId) List~Veiculo~
        +deleteById(int id) boolean
        +deleteByClienteId(int clienteId) int
    }

    ClienteRepository o-- IdGenerator
    VeiculoRepository o-- IdGenerator
    ClienteRepository ..> Cliente : gerencia
    VeiculoRepository ..> Veiculo : gerencia polimorficamente

    %% Pacote Controller e View
    class ApplicationController {
        -Menu menu
        -Input input
        -ClienteRepository clienteRepository
        -VeiculoRepository veiculoRepository
        +iniciar() void
    }

    class Menu {
        +exibirMenuPrincipal() void
        +exibirMenuClientes() void
        +exibirMenuVeiculos() void
        +exibirMenuRelatorios() void
    }

    class Input {
        -Scanner scanner
        +lerOpcao() int
        +lerId(String prompt) int
        +lerTexto(String prompt) String
        +lerDouble(String prompt) double
        +lerConfirmacao(String prompt) boolean
        +coletarDadosNovoCliente() Cliente
        +coletarDadosNovoCarro(int clienteId) Carro
        +coletarDadosNovaMoto(int clienteId) Moto
    }

    class Main {
        +main(String[] args)$ void
    }

    Main ..> ApplicationController : instancia e dispara
    ApplicationController o-- Menu
    ApplicationController o-- Input
    ApplicationController o-- ClienteRepository
    ApplicationController o-- VeiculoRepository
```

---

## 2. Diagrama de Sequência: Cadastro de Veículo com Validação de Integridade

O diagrama abaixo ilustra a interação entre as camadas para evitar o cadastro de veículos órfãos:

```mermaid
sequenceDiagram
    autonumber
    actor Usuario as Usuário
    participant Controller as ApplicationController
    participant Input as Input (View)
    participant CliRepo as ClienteRepository
    participant VeiRepo as VeiculoRepository
    participant IdGen as IdGenerator

    Usuario->>Controller: Escolhe Cadastrar Carro
    Controller->>Input: lerId("ID do Cliente Proprietário:")
    Input-->>Controller: Retorna clienteId (ex: 1)
    
    Controller->>CliRepo: existsById(1)
    alt Cliente Não Existe
        CliRepo-->>Controller: false
        Controller-->>Usuario: Exibe mensagem de erro (Cliente inexistente)
    else Cliente Existe
        CliRepo-->>Controller: true
        Controller->>Input: coletarDadosNovoCarro(1)
        Input-->>Controller: Retorna objeto Carro instanciado
        Controller->>VeiRepo: save(carro)
        VeiRepo->>IdGen: getNextId()
        IdGen-->>VeiRepo: Retorna ID sequencial gerado (ex: 1)
        VeiRepo->>VeiRepo: Grava linha CSV em data/veiculos.txt
        VeiRepo-->>Controller: Retorna Carro salvo com ID
        Controller-->>Usuario: "Carro cadastrado com sucesso! [ID: #1]"
    end
```

---

## 3. Explicação dos Conceitos Aplicados

1. **Encapsulamento:** Todos os atributos em todas as classes de modelo são `private`. O acesso e modificação ocorrem unicamente por métodos públicos com validação de dados.
2. **Herança:** A classe `Cliente` herda atributos e comportamentos de `Pessoa` via palavra-chave `extends`. As classes `Carro` e `Moto` herdam de `Veiculo`.
3. **Abstração:** `Pessoa` e `Veiculo` são declaradas com `abstract`, impedindo instanciação direta incompleta e definindo contratos via métodos abstratos.
4. **Polimorfismo:** `Carro` e `Moto` sobrescrevem (`@Override`) o método `exibirDetalhes()` e `getTipo()`. A coleção `List<Veiculo>` permite iterar sobre veículos heterogêneos chamando o comportamento específico de cada subclasse em tempo de execução.
