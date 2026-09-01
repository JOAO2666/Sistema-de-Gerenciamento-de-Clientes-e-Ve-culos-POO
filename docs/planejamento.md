# Planejamento do Projeto e Divisão da Equipe

**Projeto:** Sistema de Gerenciamento de Clientes e Veículos (POO em Java)  
**Equipe:** 5 Integrantes  

---

## 1. Divisão de Responsabilidades

Para garantir que todos os 5 integrantes participem ativamente do desenvolvimento em código e na apresentação, o projeto foi dividido em frentes modulares:

| Integrante | Papel no Projeto | Módulos e Classes | Foco Teórico na Apresentação |
| :--- | :--- | :--- | :--- |
| **João** | Integração, Arquitetura e Controle | `Main.java`, `ApplicationController.java`, Git & Repositório | Arquitetura MVC, coordenação de camadas e integridade relacional |
| **Ícaro** | Domínio Base e Encapsulamento | `Pessoa.java` (abstrata), `Cliente.java` | Encapsulamento (`private`), modificadores de acesso, herança e `super()` |
| **Alexandre** | Abstração de Veículos e Especialização Carro | `Veiculo.java` (abstrata), `Carro.java` | Classes abstratas, métodos abstratos e herança de especialização |
| **Hícaro** | Especialização Moto e Polimorfismo | `Moto.java`, demonstração com `List<Veiculo>` | Polimorfismo de inclusão (subtipo), `@Override` e dynamic binding |
| **Natanael** | Persistência, Testes e Documentação | `ClienteRepository.java`, `VeiculoRepository.java`, `IdGenerator.java`, `docs/`, `test/` | Persistência em arquivo CSV, separação de responsabilidades e testes de validação |

---

## 2. Estratégia de Branches (Git Flow Simplificado)

Para evitar conflitos de merge e organizar as entregas, o repositório adota o modelo:

```text
main (produção / versão final estável)
  ↑
develop (integração e testes contínuos)
  ↑
  ├── feat/pessoa-cliente      (Ícaro)
  ├── feat/veiculos-carro      (Alexandre)
  ├── feat/moto-polimorfismo   (Hícaro)
  ├── feat/persistencia-csv    (Natanael)
  └── feat/controller-view     (João)
```

---

## 3. Passo a Passo do Git para Cada Integrante

### Passo 1: Clonar o repositório
```bash
git clone https://github.com/JOAO2666/Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO.git
cd Sistema-de-Gerenciamento-de-Clientes-e-Ve-culos-POO
```

### Passo 2: Criar e alternar para a sua branch de trabalho
```bash
git checkout develop
git checkout -b feat/sua-feature
```

### Passo 3: Fazer alterações e commitar
```bash
git add .
git commit -m "feat: implementa classe X com encapsulamento"
```

### Passo 4: Enviar para o GitHub e abrir Pull Request
```bash
git push -u origin feat/sua-feature
```
*No GitHub, abrir o Pull Request apontando para a branch `develop`.*

---

## 4. Padrão de Commits Semânticos

Utilizamos convenções semânticas para mensagens claras:
- `feat:` Nova funcionalidade ou classe implementada (ex: `feat: adiciona classe Carro com atributos de portas e combustivel`).
- `fix:` Correção de bug ou regra de validação (ex: `fix: impede cadastro de veiculo com clienteId inexistente`).
- `docs:` Alterações na documentação ou UML (ex: `docs: atualiza diagrama de classes no uml.md`).
- `test:` Inclusão ou ajuste de testes automatizados (ex: `test: adiciona testes de polimorfismo no TestePOO`).
- `refactor:` Melhorias internas no código sem alterar o comportamento externo.

---

## 5. Roteiro Sugerido para a Apresentação em Aula

1. **Introdução (João):** Apresentar a proposta do sistema de clientes e veículos e a arquitetura em camadas (MVC).
2. **Encapsulamento e Herança de Pessoa (Ícaro):** Explicar os atributos `private` e a relação `Cliente extends Pessoa`.
3. **Abstração e Herança de Veículo (Alexandre):** Explicar a classe abstrata `Veiculo` e a subclasse `Carro`.
4. **Polimorfismo e Especialização Moto (Hícaro):** Explicar `Moto extends Veiculo`, o método abstrato `exibirDetalhes()` e como o Java escolhe o método correto em tempo de execução via `List<Veiculo>`.
5. **Persistência, Repositórios e Conclusão (Natanael / João):** Demonstrar a gravação nos arquivos CSV, ausência de falhas com entradas inválidas e os testes automatizados passando.
