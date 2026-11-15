# Sistema de Gerenciamento de Clientes, Veículos e Seguros

Projeto desenvolvido em Java para a disciplina de Programação Orientada a Objetos (POO), ministrada pela Prof.ª Ana Emília de Melo Queiroz (UNIVASF).

## Sobre o projeto

O sistema gerencia o cadastro de clientes (pessoa física e pessoa jurídica), seus bens (veículos e imóveis), a emissão de apólices de seguro (automóvel, residencial e vida), o controle de parcelas e o registro de sinistros.

Os dados ficam salvos em arquivos `.txt` na pasta `data/`, mantendo os cadastros entre as execuções do programa pelo terminal.

## Funcionalidades

* Cadastro, listagem, busca por ID/documento, atualização e remoção de clientes (PF e PJ)
* Cadastro e vínculo de veículos e imóveis aos clientes
* Emissão de apólices de seguro (auto, residencial e vida) com cálculo de prêmio e geração de parcelas
* Consulta e baixa de pagamentos de parcelas
* Abertura de sinistros, designação de perito e liquidação de indenização
* Relatório geral com totais de clientes, apólices, arrecadação e sinistros

## Estrutura do projeto

O código está dividido nos seguintes pacotes dentro de `src/`:

* `model`: classes de domínio (`Pessoa`, `Cliente`, `PessoaFisica`, `PessoaJuridica`, `Apolice`, `ApoliceAuto`, `ApoliceResidencial`, `ApoliceDeVida`, `Veiculo`, `Imovel`, `Sinistro`, `Pagamento`, `Cobertura`, `Perito`, `OficinaParceira`) e as interfaces `Identificavel` e `Seguravel`
* `model.enums`: enumerações usadas nos cadastros (`EstadoCivil`, `StatusApolice`, `TipoCobertura`, `TipoSinistro`)
* `repository`: interface genérica `DAO<T>`, controle de arquivos em `ConexaoBanco` e classes DAO (`ClienteDAO`, `VeiculoDAO`, `ApoliceDAO`, `PagamentoDAO`, `SinistroDAO`)
* `service`: interfaces e classes com as regras de negócio de cálculo de prêmio, validação de propostas, emissão de apólices e análise de sinistros
* `controller`: `ControladorCliente` e `ControladorApolice`, que ligam o menu de console aos serviços e DAOs
* `view`: `MenuPrincipal`, responsável pela leitura de dados e exibição no terminal
* `exception`: exceções específicas do domínio (`ApoliceVencidaException`, `ClienteNaoEncontradoException`, `SinistroJaRegistradoException`, `ValorPremioInvalidoException`)

Na pasta `test/` fica a classe `TestePOO`, com os testes automatizados das regras do sistema, e na pasta `docs/` estão os documentos de requisitos, planejamento da equipe e diagramas UML.

## Conceitos de POO utilizados

* Encapsulamento: atributos privados nas classes de modelo, com acesso e validação feitos pelos construtores, getters e setters.
* Herança e classes abstratas:
  * `Cliente` (abstrata) é estendida por `PessoaFisica` e `PessoaJuridica`;
  * `Apolice` (abstrata) é estendida por `ApoliceAuto`, `ApoliceResidencial` e `ApoliceDeVida`;
  * `Pessoa` (abstrata) é estendida por `Perito`.
* Interfaces e Generics:
  * `Identificavel` para entidades que possuem ID;
  * `Seguravel` implementada por `Veiculo` e `Imovel`;
  * `DAO<T extends Identificavel>` usando Generics para padronizar as operações de salvar, buscar, listar, atualizar e deletar;
  * `ICalculadoraDePremio` e `IValidadorDeProposta` na camada de serviço.
* Polimorfismo:
  * Sobrescrita (subtipo): métodos como `calcularPremioBase()` nas subclasses de `Apolice`, `getDocumentoPrincipal()` e `exibirResumo()` nas subclasses de `Cliente` e `Pessoa`, e `getValorAvaliado()` em `Seguravel`;
  * Sobrecarga (overload): métodos com o mesmo nome e assinaturas diferentes, como `calcularPremio(Apolice)` e `calcularPremio(Apolice, double)` em `CalculadoraDePremioService`, `emitirApolice` em `EmissorDeApoliceService`, `adicionarCobertura` em `Apolice` e `registrarQuitacao` em `Pagamento`.
* Tratamento de exceções: uso de exceções checadas para impedir operações inválidas, como abrir sinistro em apólice vencida, cadastrar sinistro duplicado ou calcular prêmio com valor inválido.

## Como compilar e executar

É necessário ter o Java JDK 11 ou superior instalado.

### No Linux ou macOS

Compilar os arquivos do projeto e de teste:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin $(find src test -name "*.java")
```

Executar o menu principal:

```bash
java -cp bin Main
```

Executar os testes:

```bash
java -cp bin TestePOO
```

### No Windows (PowerShell)

Compilar:

```powershell
New-Item -ItemType Directory -Force -Path bin | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src, test -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
```

Executar o menu principal:

```powershell
java -cp bin Main
```

Executar os testes:

```powershell
java -cp bin TestePOO
```
