# Sistema de Gerenciamento de Clientes, Veículos e Seguros (POO)

Projeto desenvolvido para a disciplina de **Programação Orientada a Objetos (POO)**, aplicando com rigor os pilares fundamentais da engenharia de software orientada a objetos: **Abstração, Encapsulamento, Herança e Polimorfismo**, além de **Interfaces, Generics, Exceções Personalizadas e Arquitetura em Camadas (MVC / DAO)**.

---

## 🏛️ Arquitetura do Sistema

O projeto está estruturado em pacotes seguindo a separação de responsabilidades:

```
src/
├── model/                     # Camada de Domínio (Entidades e Interfaces)
│   ├── Identificavel.java     # [INTERFACE] Contrato para entidades com ID
│   ├── Seguravel.java         # [INTERFACE] Contrato para bens seguráveis
│   ├── Cliente.java           # [CLASSE ABSTRATA] Base para clientes
│   ├── PessoaFisica.java      # Especialização de Cliente (CPF, RG, Estado Civil)
│   ├── PessoaJuridica.java    # Especialização de Cliente (CNPJ, Razão Social, IE)
│   ├── Apolice.java           # [CLASSE ABSTRATA] Base para apólices de seguro
│   ├── ApoliceAuto.java       # Especialização para seguro de veículos
│   ├── ApoliceResidencial.java# Especialização para seguro de imóveis
│   ├── ApoliceDeVida.java     # Especialização para seguro de vida
│   ├── Veiculo.java           # Bem segurável (Placa, FIPE, Renavam)
│   ├── Imovel.java            # Bem segurável (CEP, Endereço, Valor Mercado)
│   ├── Sinistro.java          # Registro de sinistros/ocorrências
│   ├── Perito.java            # Profissional avaliador de sinistros
│   ├── OficinaParceira.java   # Oficina conveniada para reparos
│   ├── Pagamento.java         # Parcelas financeiras da apólice
│   ├── Cobertura.java         # Coberturas e capitais segurados
│   └── enums/
│       ├── StatusApolice.java # PROPOSTA, ATIVA, SUSPENSA, CANCELADA, EXPIRADA
│       ├── TipoCobertura.java # Tipos de coberturas securitárias
│       ├── TipoSinistro.java  # Colisão, Roubo, Incêndio, Alagamento, etc.
│       └── EstadoCivil.java   # Solteiro, Casado, Divorciado, etc.
├── service/                   # Camada de Serviços / Regras de Negócio
│   ├── ICalculadoraDePremio.java     # [INTERFACE] Contrato de cálculo de prêmios
│   ├── IValidadorDeProposta.java     # [INTERFACE] Contrato de validação
│   ├── CalculadoraDePremioService.java # Cálculo atuarial com polimorfismo e IOF
│   ├── EmissorDeApoliceService.java    # Emissão e geração de parcelas
│   ├── ProcessadorDeSinistroService.java # Análise de sinistro, perícia e liquidação
│   └── ValidadorDePropostaService.java # Validação de regras e integridade
├── repository/                # Camada de Persistência (DAO)
│   ├── DAO.java               # [INTERFACE GENÉRICA] Contrato CRUD
│   ├── ConexaoBanco.java      # Gerenciamento de conexões, IDs e persistência
│   ├── ClienteDAO.java        # Persistência de PF e PJ
│   ├── ApoliceDAO.java        # Persistência polimórfica de apólices
│   ├── SinistroDAO.java       # Persistência de sinistros
│   ├── VeiculoDAO.java        # Persistência de veículos
│   └── PagamentoDAO.java      # Persistência de pagamentos/parcelas
├── exception/                 # Exceções Personalizadas
│   ├── ApoliceVencidaException.java
│   ├── ValorPremioInvalidoException.java
│   ├── ClienteNaoEncontradoException.java
│   └── SinistroJaRegistradoException.java
├── controller/                # Camada de Controle
│   ├── ControladorCliente.java
│   └── ControladorApolice.java
├── view/                      # Camada de Apresentação (Console UI)
│   └── MenuPrincipal.java
└── Main.java                  # Ponto de entrada e injeção de dependências
```

---

## 🎯 Conceitos de POO Aplicados

### 1. Classes Abstratas
- **`Cliente`**: Define atributos essenciais e métodos abstratos (`getDocumentoPrincipal()`, `getTipoCliente()`, `exibirResumo()`) implementados pelas subclasses `PessoaFisica` e `PessoaJuridica`.
- **`Apolice`**: Define atributos comuns e o método abstrato `calcularPremioBase()`, `getTipoApolice()` e `exibirDetalhesApolice()`. Não pode ser instanciada diretamente.

### 2. Interfaces
- **`Identificavel`**: Padroniza o acesso a IDs (`getId()`, `setId(int)`).
- **`Seguravel`**: Define os métodos `getValorAvaliado()`, `getIdentificador()` e `getDescricaoCompleta()` para bens que podem ter apólice (`Veiculo`, `Imovel`).
- **`DAO<T>`**: Interface genérica para desacoplar a camada de persistência com métodos CRUD.
- **`ICalculadoraDePremio`** e **`IValidadorDeProposta`**: Interfaces de serviços para inversão de dependência (SOLID).

### 3. Polimorfismo
- O cálculo do prêmio na `CalculadoraDePremioService` invoca `apolice.calcularPremioBase()`, que executa regras diferentes em tempo de execução para **`ApoliceAuto`**, **`ApoliceResidencial`** ou **`ApoliceDeVida`**.
- O repositório `ApoliceDAO` e o `ClienteDAO` tratam coleções de tipos genéricos e subclasses polimorficamente.

### 4. Encapsulamento
- Todos os atributos são privados (`private`), acessados e modificados via getters e setters com validação de consistência.

### 5. Tratamento de Exceções Personalizadas
- `ApoliceVencidaException`: Lançada ao tentar acionar sinistro em apólice expirada.
- `ValorPremioInvalidoException`: Lançada quando o cálculo ou valor contratual é nulo ou negativo.
- `ClienteNaoEncontradoException`: Lançada ao buscar por IDs ou documentos inexistentes.
- `SinistroJaRegistradoException`: Lançada ao tentar registrar evento duplicado para a mesma apólice e data.

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
- Java JDK 11 ou superior instalado

### Compilação e Execução via Terminal

1. **Compilar o projeto**:
   ```bash
   javac -encoding UTF-8 -d bin (Get-ChildItem -Path src, test -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
   ```

2. **Executar a aplicação interativa**:
   ```bash
   java -cp bin Main
   ```

3. **Executar a suíte de testes automatizados**:
   ```bash
   java -cp bin TestePOO
   ```
