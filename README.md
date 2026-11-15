# Sistema de Gerenciamento de Clientes, Veículos e Seguros

Projeto desenvolvido em Java para a disciplina de Programação Orientada a Objetos (POO), ministrada pela Prof.ª Ana Emília de Melo Queiroz (UNIVASF).

## Sobre o projeto

O sistema gerencia o cadastro de clientes (pessoa física e pessoa jurídica), seus bens (veículos e imóveis), a emissão de apólices de seguro (automóvel, residencial e vida), o controle de parcelas e o registro de sinistros.

Os dados ficam salvos em arquivos `.txt` na pasta `data/` para que os cadastros sejam mantidos entre uma execução e outra no terminal.

## Integrantes e divisão do projeto

* João: `Main`, `MenuPrincipal`, `ControladorCliente`, `ControladorApolice` e `TestePOO` (organização dos pacotes em MVC, fluxo do menu no terminal, ligação com os controladores e bateria de testes)
* Ícaro: `Pessoa`, `Cliente`, `PessoaFisica`, `PessoaJuridica` e `Perito` (encapsulamento, validação nos getters e setters, classes abstratas e herança com `super()`)
* Alexandre: `Identificavel`, `Seguravel`, `Veiculo`, `Imovel`, `Apolice`, `ApoliceAuto`, `DAO`, `ConexaoBanco`, `ClienteDAO`, `VeiculoDAO` e `ApoliceDAO` (interfaces, uso de Generics em `DAO<T>`, associação entre classes e leitura/escrita nos arquivos `.txt`)
* Hícaro: `ApoliceResidencial`, `ApoliceDeVida`, `Sinistro`, `Pagamento`, `Cobertura`, `PagamentoDAO`, `SinistroDAO`, pacote `service` e pacote `exception` (polimorfismo por sobrescrita e sobrecarga, cálculo de prêmio e tratamento das exceções personalizadas)

## Funcionalidades

* Cadastro, listagem, busca por ID ou documento, atualização e remoção de clientes (PF e PJ)
* Cadastro e vínculo de veículos e imóveis aos clientes
* Emissão de apólices de seguro (auto, residencial e vida) com cálculo de prêmio e geração de parcelas
* Consulta e baixa de pagamentos de parcelas
* Abertura de sinistros, designação de perito e liquidação de indenização
* Relatório geral com totais de clientes, apólices, valores recebidos e sinistros

## Estrutura de pacotes

Dentro da pasta `src/`, o código foi separado em:

* `model`: classes principais do domínio (`Pessoa`, `Cliente`, `PessoaFisica`, `PessoaJuridica`, `Apolice`, `ApoliceAuto`, `ApoliceResidencial`, `ApoliceDeVida`, `Veiculo`, `Imovel`, `Sinistro`, `Pagamento`, `Cobertura`, `Perito`, `OficinaParceira`) e as interfaces `Identificavel` e `Seguravel`
* `model.enums`: enumerações (`EstadoCivil`, `StatusApolice`, `TipoCobertura`, `TipoSinistro`)
* `repository`: interface genérica `DAO<T>`, controle de arquivos em `ConexaoBanco` e classes DAO (`ClienteDAO`, `VeiculoDAO`, `ApoliceDAO`, `PagamentoDAO`, `SinistroDAO`)
* `service`: interfaces e classes com as regras de cálculo de prêmio, validação de propostas, emissão de apólices e análise de sinistros
* `controller`: `ControladorCliente` e `ControladorApolice`, que fazem a ponte entre o menu e os serviços/DAOs
* `view`: `MenuPrincipal`, responsável pela interação no console
* `exception`: exceções do domínio (`ApoliceVencidaException`, `ClienteNaoEncontradoException`, `SinistroJaRegistradoException`, `ValorPremioInvalidoException`)

Na pasta `test/` fica a classe `TestePOO` e na pasta `docs/` estão os arquivos de requisitos, planejamento e diagramas UML.

## Conceitos de POO aplicados

Durante o desenvolvimento foram utilizados:

* Encapsulamento com atributos `private` e acesso controlado por construtores, getters e setters.
* Herança e classes abstratas em três hierarquias: `Cliente` (`PessoaFisica` e `PessoaJuridica`), `Apolice` (`ApoliceAuto`, `ApoliceResidencial` e `ApoliceDeVida`) e `Pessoa` (`Perito`).
* Interfaces e Generics com `Identificavel`, `Seguravel` (implementada por `Veiculo` e `Imovel`), `DAO<T extends Identificavel>`, `ICalculadoraDePremio` e `IValidadorDeProposta`.
* Polimorfismo por sobrescrita (`@Override`) nos métodos `calcularPremioBase()`, `getDocumentoPrincipal()`, `exibirResumo()` e `getValorAvaliado()`.
* Polimorfismo por sobrecarga (overload) em métodos com o mesmo nome e parâmetros diferentes, como `calcularPremio(Apolice)` e `calcularPremio(Apolice, double)` em `CalculadoraDePremioService`, `emitirApolice` em `EmissorDeApoliceService`, `adicionarCobertura` em `Apolice` e `registrarQuitacao` em `Pagamento`.
* Exceções personalizadas para barrar operações inválidas, como abrir sinistro em apólice vencida, registrar sinistro repetido no mesmo dia ou calcular prêmio com valor inválido.

## Como compilar e executar

É necessário ter o Java JDK 11 ou superior instalado.

### No Linux ou macOS

Compilar:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin $(find src test -name "*.java")
```

Rodar o menu principal:

```bash
java -cp bin Main
```

Rodar os testes:

```bash
java -cp bin TestePOO
```

### No Windows (PowerShell)

Compilar:

```powershell
New-Item -ItemType Directory -Force -Path bin | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src, test -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
```

Rodar o menu principal:

```powershell
java -cp bin Main
```

Rodar os testes:

```powershell
java -cp bin TestePOO
```
