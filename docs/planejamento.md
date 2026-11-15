# Planejamento e Divisão da Equipe

Projeto: Sistema de Gerenciamento de Clientes, Veículos e Seguros (POO)
Equipe: 4 integrantes

## 1. Divisão de tarefas

O desenvolvimento foi dividido entre os 4 integrantes do grupo para que cada um trabalhasse em uma parte do código e apresentasse os conceitos correspondentes:

| Integrante | Responsabilidade no projeto | Principais arquivos | Tópicos na apresentação |
| :--- | :--- | :--- | :--- |
| João | Integração geral, controladores, interface de console e testes | `Main.java`, `MenuPrincipal.java`, `ControladorCliente.java`, `ControladorApolice.java`, `TestePOO.java` | Organização em pacotes (MVC), fluxo do menu, integração entre camadas e execução dos testes |
| Ícaro | Classes base de pessoas e clientes | `Pessoa.java`, `Cliente.java`, `PessoaFisica.java`, `PessoaJuridica.java`, `Perito.java` | Encapsulamento, modificadores de acesso, classes abstratas e herança com `super()` |
| Alexandre | Bens seguráveis, apólices e persistência (DAO) | `Seguravel.java`, `Identificavel.java`, `Veiculo.java`, `Imovel.java`, `Apolice.java`, `ApoliceAuto.java`, `DAO.java`, `ConexaoBanco.java`, `ClienteDAO.java`, `VeiculoDAO.java`, `ApoliceDAO.java` | Interfaces, Generics (`DAO<T>`), associação entre classes e armazenamento em arquivos `.txt` |
| Hícaro | Demais apólices, regras de cálculo, sinistros, pagamentos e exceções | `ApoliceResidencial.java`, `ApoliceDeVida.java`, `Sinistro.java`, `Pagamento.java`, `CalculadoraDePremioService.java`, `EmissorDeApoliceService.java`, `ProcessadorDeSinistroService.java`, `PagamentoDAO.java`, `SinistroDAO.java`, `exception/` | Polimorfismo de subtipo (sobrescrita), sobrecarga de métodos (overload) e exceções personalizadas |

## 2. Organização no Git

O repositório utiliza uma branch principal (`main`), uma branch de integração (`develop`) e branches separadas para as tarefas do grupo:

```text
main
  ↑
develop
  ↑
  ├── feat/pessoa-cliente
  ├── feat/bens-apolice-dao
  ├── feat/apolices-servicos
  └── feat/controller-menu
```

## 3. Fluxo básico de uso do Git

1. Clonar o repositório:
```bash
git clone https://github.com/JOAO2666/Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO.git
cd Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO
```

2. Criar a branch da tarefa a partir da `develop`:
```bash
git checkout develop
git checkout -b feat/nome-da-tarefa
```

3. Salvar as alterações e enviar para o GitHub:
```bash
git add .
git commit -m "feat: descricao da alteracao"
git push -u origin feat/nome-da-tarefa
```

4. Abrir o Pull Request no GitHub para revisão e merge.

## 4. Ordem sugerida para a apresentação

1. João: visão geral do problema, estrutura de pacotes (MVC), funcionamento do menu e execução do `TestePOO`.
2. Ícaro: explicação de encapsulamento e herança nas classes `Pessoa`, `Cliente`, `PessoaFisica`, `PessoaJuridica` e `Perito`.
3. Alexandre: apresentação das interfaces (`Identificavel`, `Seguravel` e a genérica `DAO<T>`), das classes `Veiculo`, `Imovel`, `Apolice` / `ApoliceAuto` e da gravação nos arquivos `.txt`.
4. Hícaro: demonstração de polimorfismo (sobrescrita em `calcularPremioBase()` e sobrecarga nos serviços e pagamentos) e tratamento das exceções de negócio.
