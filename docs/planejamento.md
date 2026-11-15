# Planejamento e Divisão da Equipe

Projeto: Sistema de Gerenciamento de Clientes, Veículos e Seguros (POO)
Equipe: 5 integrantes

## 1. Divisão de tarefas

O desenvolvimento foi dividido entre os 5 integrantes do grupo para que cada um trabalhasse em uma parte do código e apresentasse os conceitos correspondentes:

| Integrante | Responsabilidade no projeto | Principais arquivos | Tópicos na apresentação |
| :--- | :--- | :--- | :--- |
| João | Integração geral, controladores e interface de console | `Main.java`, `MenuPrincipal.java`, `ControladorCliente.java`, `ControladorApolice.java` | Organização em pacotes (MVC), fluxo de telas e integração entre as camadas |
| Ícaro | Classes base de pessoas e clientes | `Pessoa.java`, `Cliente.java`, `PessoaFisica.java`, `PessoaJuridica.java`, `Perito.java` | Encapsulamento, modificadores de acesso, classe abstrata e herança com `super()` |
| Alexandre | Bens seguráveis e hierarquia de apólices | `Seguravel.java`, `Veiculo.java`, `Imovel.java`, `Apolice.java`, `ApoliceAuto.java` | Interfaces de domínio, associação entre classes e especialização de apólices |
| Hícaro | Demais apólices, regras de cálculo e sinistros | `ApoliceResidencial.java`, `ApoliceDeVida.java`, `CalculadoraDePremioService.java`, `ProcessadorDeSinistroService.java`, `exception/` | Polimorfismo de subtipo (sobrescrita), sobrecarga de métodos (overload) e exceções personalizadas |
| Natanael | Persistência em arquivos, parcelas e testes | `DAO.java`, `ConexaoBanco.java`, `ClienteDAO.java`, `VeiculoDAO.java`, `ApoliceDAO.java`, `PagamentoDAO.java`, `SinistroDAO.java`, `TestePOO.java` | Uso de Generics (`DAO<T>`), leitura/escrita em arquivos `.txt` e testes automatizados |

## 2. Organização no Git

O repositório utiliza uma branch principal (`main`), uma branch de integração (`develop`) e branches separadas para as tarefas do grupo:

```text
main
  ↑
develop
  ↑
  ├── feat/pessoa-cliente
  ├── feat/bens-apolice-auto
  ├── feat/apolices-servicos
  ├── feat/persistencia-dao
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

1. João: visão geral do problema, estrutura de pacotes e funcionamento do menu.
2. Ícaro: explicação de encapsulamento e herança nas classes `Pessoa`, `Cliente`, `PessoaFisica`, `PessoaJuridica` e `Perito`.
3. Alexandre: apresentação da interface `Seguravel`, das classes `Veiculo` e `Imovel`, e da classe abstrata `Apolice` com `ApoliceAuto`.
4. Hícaro: demonstração de polimorfismo (sobrescrita em `calcularPremioBase()` e sobrecarga nos serviços) e tratamento das exceções de negócio.
5. Natanael: explicação da interface genérica `DAO<T>`, armazenamento nos arquivos `.txt` e execução do `TestePOO`.
