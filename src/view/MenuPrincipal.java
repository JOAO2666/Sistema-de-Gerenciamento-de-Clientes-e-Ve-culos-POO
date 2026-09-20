package view;

import controller.ControladorApolice;
import controller.ControladorCliente;
import exception.ApoliceVencidaException;
import exception.ClienteNaoEncontradoException;
import exception.SinistroJaRegistradoException;
import exception.ValorPremioInvalidoException;
import model.*;
import model.enums.EstadoCivil;
import model.enums.TipoCobertura;
import model.enums.TipoSinistro;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Menu principal da aplicação de gerenciamento de clientes, apólices e sinistros.
 * Fornece interface via console com navegação interativa e tratamento de exceções.
 */
public class MenuPrincipal {
    private final ControladorCliente controladorCliente;
    private final ControladorApolice controladorApolice;
    private final Scanner scanner;

    public MenuPrincipal(ControladorCliente controladorCliente, ControladorApolice controladorApolice) {
        this.controladorCliente = controladorCliente;
        this.controladorApolice = controladorApolice;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=======================================================");
            System.out.println("  SISTEMA DE GERENCIAMENTO DE SEGUROS E CLIENTES (POO) ");
            System.out.println("=======================================================");
            System.out.println(" 1. Gestão de Clientes (Pessoa Física e Jurídica)");
            System.out.println(" 2. Gestão de Veículos");
            System.out.println(" 3. Emissão e Gestão de Apólices (Auto, Residencial, Vida)");
            System.out.println(" 4. Gestão de Sinistros e Perícias");
            System.out.println(" 5. Gestão Financeira e Pagamentos");
            System.out.println(" 6. Inserir Dados de Exemplo para Demonstração");
            System.out.println(" 0. Sair");
            System.out.println("=======================================================");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuVeiculos();
                    break;
                case 3:
                    menuApolices();
                    break;
                case 4:
                    menuSinistros();
                    break;
                case 5:
                    menuPagamentos();
                    break;
                case 6:
                    carregarDadosExemplo();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema. Até logo!");
                    break;
                default:
                    System.out.println("\n[!] Opção inválida! Digite uma opção válida do menu.");
            }
        } while (opcao != 0);
    }

    // ==========================================
    // 1. SUBMENU CLIENTES
    // ==========================================
    private void menuClientes() {
        System.out.println("\n--- GESTÃO DE CLIENTES ---");
        System.out.println("1. Cadastrar Pessoa Física");
        System.out.println("2. Cadastrar Pessoa Jurídica");
        System.out.println("3. Listar Todos os Clientes");
        System.out.println("4. Buscar Cliente por Documento (CPF/CNPJ)");
        System.out.println("5. Remover Cliente por ID");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");
        int op = lerInteiro();

        switch (op) {
            case 1:
                cadastrarPF();
                break;
            case 2:
                cadastrarPJ();
                break;
            case 3:
                listarClientes();
                break;
            case 4:
                buscarClienteDocumento();
                break;
            case 5:
                removerCliente();
                break;
            default:
                break;
        }
    }

    private void cadastrarPF() {
        System.out.println("\n[+] Cadastro de Pessoa Física");
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine().trim();
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine().trim();
        System.out.print("Endereço: ");
        String end = scanner.nextLine().trim();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("RG: ");
        String rg = scanner.nextLine().trim();
        System.out.print("Data de Nascimento (dd/mm/aaaa): ");
        String nasc = scanner.nextLine().trim();
        System.out.print("Sexo (M/F): ");
        String sexo = scanner.nextLine().trim();

        System.out.println("Estado Civil: 1-Solteiro, 2-Casado, 3-Divorciado, 4-Viúvo, 5-União Estável");
        int ecOp = lerInteiro();
        EstadoCivil ec = EstadoCivil.SOLTEIRO;
        if (ecOp == 2) ec = EstadoCivil.CASADO;
        else if (ecOp == 3) ec = EstadoCivil.DIVORCIADO;
        else if (ecOp == 4) ec = EstadoCivil.VIUVO;
        else if (ecOp == 5) ec = EstadoCivil.UNIAO_ESTAVEL;

        PessoaFisica pf = new PessoaFisica(0, nome, email, tel, end, cpf, rg, nasc, sexo, ec);
        controladorCliente.cadastrarCliente(pf);
        System.out.println("\n[OK] Pessoa Física cadastrada com sucesso! ID gerado: " + pf.getId());
    }

    private void cadastrarPJ() {
        System.out.println("\n[+] Cadastro de Pessoa Jurídica");
        System.out.print("Nome Fantasia: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Razão Social: ");
        String razao = scanner.nextLine().trim();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine().trim();
        System.out.print("Inscrição Estadual: ");
        String ie = scanner.nextLine().trim();
        System.out.print("Representante Legal: ");
        String rep = scanner.nextLine().trim();
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine().trim();
        System.out.print("Endereço: ");
        String end = scanner.nextLine().trim();

        PessoaJuridica pj = new PessoaJuridica(0, nome, email, tel, end, cnpj, razao, ie, nome, rep);
        controladorCliente.cadastrarCliente(pj);
        System.out.println("\n[OK] Pessoa Jurídica cadastrada com sucesso! ID gerado: " + pj.getId());
    }

    private void listarClientes() {
        List<Cliente> lista = controladorCliente.listarTodosClientes();
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum cliente cadastrado no momento.");
            return;
        }
        System.out.println("\n--- LISTA DE CLIENTES CADASTRADOS (" + lista.size() + ") ---");
        for (Cliente c : lista) {
            System.out.println(c.exibirResumo());
        }
    }

    private void buscarClienteDocumento() {
        System.out.print("\nDigite o CPF ou CNPJ para busca: ");
        String doc = scanner.nextLine().trim();
        try {
            Cliente c = controladorCliente.buscarPorDocumento(doc);
            System.out.println("\n[+] Cliente Encontrado:");
            System.out.println(c.exibirResumo());
        } catch (ClienteNaoEncontradoException e) {
            System.out.println("\n[!] Erro: " + e.getMessage());
        }
    }

    private void removerCliente() {
        System.out.print("\nDigite o ID do cliente a remover: ");
        int id = lerInteiro();
        if (controladorCliente.removerCliente(id)) {
            System.out.println("\n[OK] Cliente ID " + id + " removido com sucesso!");
        } else {
            System.out.println("\n[!] Cliente não encontrado.");
        }
    }

    // ==========================================
    // 2. SUBMENU VEÍCULOS
    // ==========================================
    private void menuVeiculos() {
        System.out.println("\n--- GESTÃO DE VEÍCULOS ---");
        System.out.println("1. Cadastrar Veículo para um Cliente");
        System.out.println("2. Listar Veículos de um Cliente");
        System.out.println("3. Listar Todos os Veículos");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");
        int op = lerInteiro();

        switch (op) {
            case 1:
                cadastrarVeiculo();
                break;
            case 2:
                listarVeiculosCliente();
                break;
            case 3:
                listarTodosVeiculos();
                break;
            default:
                break;
        }
    }

    private void cadastrarVeiculo() {
        System.out.print("\nID do Cliente Proprietário: ");
        int clienteId = lerInteiro();

        try {
            controladorCliente.buscarPorId(clienteId);
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine().trim();
            System.out.print("Ano de Fabricação: ");
            int ano = lerInteiro();
            System.out.print("Placa: ");
            String placa = scanner.nextLine().trim();
            System.out.print("Renavam: ");
            String renavam = scanner.nextLine().trim();
            System.out.print("Chassi: ");
            String chassi = scanner.nextLine().trim();
            System.out.print("Valor Tabela FIPE (R$): ");
            double fipe = lerDouble();

            Veiculo v = new Veiculo(0, clienteId, marca, modelo, ano, placa, renavam, chassi, fipe);
            controladorCliente.cadastrarVeiculo(v);
            System.out.println("\n[OK] Veículo cadastrado com sucesso! ID: " + v.getId());
        } catch (ClienteNaoEncontradoException e) {
            System.out.println("\n[!] Erro: " + e.getMessage());
        }
    }

    private void listarVeiculosCliente() {
        System.out.print("\nID do Cliente: ");
        int id = lerInteiro();
        List<Veiculo> lista = controladorCliente.listarVeiculosDoCliente(id);
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum veículo encontrado para o cliente ID " + id);
            return;
        }
        System.out.println("\n--- Veículos do Cliente ID " + id + " ---");
        for (Veiculo v : lista) {
            System.out.println(v.getDescricaoCompleta());
        }
    }

    private void listarTodosVeiculos() {
        List<Veiculo> lista = controladorCliente.listarTodosVeiculos();
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum veículo cadastrado no sistema.");
            return;
        }
        System.out.println("\n--- Todos os Veículos Cadastrados (" + lista.size() + ") ---");
        for (Veiculo v : lista) {
            System.out.println(v.getDescricaoCompleta() + " | Cliente ID: " + v.getClienteId());
        }
    }

    // ==========================================
    // 3. SUBMENU APÓLICES
    // ==========================================
    private void menuApolices() {
        System.out.println("\n--- EMISSÃO E GESTÃO DE APÓLICES ---");
        System.out.println("1. Emitir Apólice de Automóvel (Auto)");
        System.out.println("2. Emitir Apólice Residencial");
        System.out.println("3. Emitir Apólice de Vida");
        System.out.println("4. Listar Todas as Apólices");
        System.out.println("5. Ver Detalhes de uma Apólice");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");
        int op = lerInteiro();

        switch (op) {
            case 1:
                emitirApoliceAuto();
                break;
            case 2:
                emitirApoliceResidencial();
                break;
            case 3:
                emitirApoliceVida();
                break;
            case 4:
                listarApolices();
                break;
            case 5:
                detalharApolice();
                break;
            default:
                break;
        }
    }

    private void emitirApoliceAuto() {
        System.out.print("\nID do Cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);
            List<Veiculo> veiculos = controladorCliente.listarVeiculosDoCliente(cId);
            if (veiculos.isEmpty()) {
                System.out.println("\n[!] O cliente não possui veículos cadastrados. Cadastre um veículo primeiro.");
                return;
            }

            System.out.println("Selecione o Veículo:");
            for (int i = 0; i < veiculos.size(); i++) {
                System.out.println((i + 1) + ". " + veiculos.get(i).getDescricaoCompleta());
            }
            int vEscolha = lerInteiro() - 1;
            if (vEscolha < 0 || vEscolha >= veiculos.size()) {
                System.out.println("\n[!] Escolha de veículo inválida.");
                return;
            }
            Veiculo veiculo = veiculos.get(vEscolha);

            System.out.print("Classe de Bônus (0 a 10): ");
            int bonus = lerInteiro();
            System.out.print("Perfil Principal do Condutor (ex: Trabalho / Lazer): ");
            String perfil = scanner.nextLine().trim();
            System.out.print("Valor da Franquia (R$): ");
            double franquia = lerDouble();
            System.out.print("Quantidade de Parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de Pagamento (BOLETO / CARTAO / PIX): ");
            String forma = scanner.nextLine().trim();

            ApoliceAuto auto = new ApoliceAuto(0, "", cliente, 0.0, franquia,
                    LocalDate.now(), LocalDate.now().plusYears(1), veiculo, bonus, perfil);

            auto.adicionarCobertura(new Cobertura(1, "Colisão e Danos Materiais", "Cobertura de colisão", veiculo.getValorFipe(), franquia, TipoCobertura.COMPREENSIVA));
            auto.adicionarCobertura(new Cobertura(2, "Danos a Terceiros", "RCF-V", 100000.0, 0.0, TipoCobertura.DANOS_A_TERCEIROS));

            Apolice emitida = controladorApolice.emitirApolice(auto, parcelas, forma);
            System.out.println("\n[OK] Apólice de Automóvel emitida com SUCESSO!");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio Total Calculado: R$ %.2f em %dx de R$ %.2f\n",
                    emitida.getValorPremio(), parcelas, (emitida.getValorPremio() / parcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException e) {
            System.out.println("\n[!] Falha na emissão: " + e.getMessage());
        }
    }

    private void emitirApoliceResidencial() {
        System.out.print("\nID do Cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);

            System.out.print("Endereço do Imóvel: ");
            String end = scanner.nextLine().trim();
            System.out.print("CEP: ");
            String cep = scanner.nextLine().trim();
            System.out.print("Cidade: ");
            String cidade = scanner.nextLine().trim();
            System.out.print("Estado (UF): ");
            String uf = scanner.nextLine().trim();
            System.out.print("Valor de Mercado do Imóvel (R$): ");
            double valorMercado = lerDouble();
            System.out.print("Área Construída (m²): ");
            double area = lerDouble();
            System.out.print("Possui Alarme / Monitoramento (S/N): ");
            boolean alarme = scanner.nextLine().trim().equalsIgnoreCase("S");
            System.out.print("Valor da Franquia (R$): ");
            double franquia = lerDouble();
            System.out.print("Quantidade de Parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de Pagamento (BOLETO / CARTAO / PIX): ");
            String forma = scanner.nextLine().trim();

            Imovel imovel = new Imovel(1, cId, cep, end, cidade, uf, valorMercado, area, "Alvenaria", alarme);
            ApoliceResidencial res = new ApoliceResidencial(0, "", cliente, 0.0, franquia,
                    LocalDate.now(), LocalDate.now().plusYears(1), imovel, "Residencial", alarme);

            res.adicionarCobertura(new Cobertura(1, "Incêndio e Raio", "Cobertura estrutural básica", valorMercado, franquia, TipoCobertura.INCENDIO));
            res.adicionarCobertura(new Cobertura(2, "Roubo e Furto Qualificado", "Bens no interior", valorMercado * 0.2, franquia, TipoCobertura.ROUBO_FURTO));

            Apolice emitida = controladorApolice.emitirApolice(res, parcelas, forma);
            System.out.println("\n[OK] Apólice Residencial emitida com SUCESSO!");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio Total Calculado: R$ %.2f em %dx de R$ %.2f\n",
                    emitida.getValorPremio(), parcelas, (emitida.getValorPremio() / parcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException e) {
            System.out.println("\n[!] Falha na emissão: " + e.getMessage());
        }
    }

    private void emitirApoliceVida() {
        System.out.print("\nID do Cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);

            System.out.print("Capital Segurado Desejado (R$): ");
            double capital = lerDouble();
            System.out.print("Idade do Segurado: ");
            int idade = lerInteiro();
            System.out.print("É fumante? (S/N): ");
            boolean fumante = scanner.nextLine().trim().equalsIgnoreCase("S");
            System.out.print("Nome do Beneficiário Principal: ");
            String benef = scanner.nextLine().trim();
            System.out.print("Quantidade de Parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de Pagamento (BOLETO / CARTAO / PIX): ");
            String forma = scanner.nextLine().trim();

            ApoliceDeVida vida = new ApoliceDeVida(0, "", cliente, 0.0, 0.0,
                    LocalDate.now(), LocalDate.now().plusYears(1), capital, fumante, idade);
            vida.adicionarBeneficiario(benef);

            vida.adicionarCobertura(new Cobertura(1, "Morte Acidental", "Indenização integral", capital, 0.0, TipoCobertura.MORTE_ACIDENTAL));
            vida.adicionarCobertura(new Cobertura(2, "Invalidez Permanente", "Cobertura por acidente", capital, 0.0, TipoCobertura.INVALIDEZ_PERMANENTE));

            Apolice emitida = controladorApolice.emitirApolice(vida, parcelas, forma);
            System.out.println("\n[OK] Apólice de Vida emitida com SUCESSO!");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio Total Calculado: R$ %.2f em %dx de R$ %.2f\n",
                    emitida.getValorPremio(), parcelas, (emitida.getValorPremio() / parcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException e) {
            System.out.println("\n[!] Falha na emissão: " + e.getMessage());
        }
    }

    private void listarApolices() {
        List<Apolice> lista = controladorApolice.listarTodasApolices();
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhuma apólice cadastrada no sistema.");
            return;
        }
        System.out.println("\n--- APÓLICES EMITIDAS NO SISTEMA (" + lista.size() + ") ---");
        for (Apolice a : lista) {
            System.out.println(a);
        }
    }

    private void detalharApolice() {
        System.out.print("\nDigite o ID da apólice: ");
        int id = lerInteiro();
        Apolice a = controladorApolice.buscarApolicePorId(id);
        if (a == null) {
            System.out.println("\n[!] Apólice ID " + id + " não encontrada.");
            return;
        }
        a.exibirDetalhesApolice();
    }

    // ==========================================
    // 4. SUBMENU SINISTROS
    // ==========================================
    private void menuSinistros() {
        System.out.println("\n--- GESTÃO DE SINISTROS E PERÍCIAS ---");
        System.out.println("1. Registrar Novo Sinistro");
        System.out.println("2. Listar Todos os Sinistros");
        System.out.println("3. Realizar Perícia e Aprovar Indenização");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");
        int op = lerInteiro();

        switch (op) {
            case 1:
                registrarSinistro();
                break;
            case 2:
                listarSinistros();
                break;
            case 3:
                laudarSinistro();
                break;
            default:
                break;
        }
    }

    private void registrarSinistro() {
        System.out.print("\nID da Apólice: ");
        int apId = lerInteiro();
        System.out.println("Tipo de Sinistro:");
        System.out.println("1-Colisão, 2-Roubo/Furto, 3-Incêndio, 4-Alagamento, 5-Danos a Terceiros");
        int tOp = lerInteiro();
        TipoSinistro tipo = TipoSinistro.COLISAO;
        if (tOp == 2) tipo = TipoSinistro.ROUBO_FURTO;
        else if (tOp == 3) tipo = TipoSinistro.INCENDIO;
        else if (tOp == 4) tipo = TipoSinistro.ALAGAMENTO;
        else if (tOp == 5) tipo = TipoSinistro.DANOS_A_TERCEIROS;

        System.out.print("Descrição do Ocorrido: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Valor Estimado do Prejuízo (R$): ");
        double prejuizo = lerDouble();

        try {
            Sinistro s = controladorApolice.registrarSinistro(apId, LocalDate.now(), tipo, desc, prejuizo);
            System.out.println("\n[OK] Sinistro registrado com sucesso!");
            System.out.println("Protocolo de Atendimento: " + s.getNumeroProtocolo() + " | ID: " + s.getId());
        } catch (ApoliceVencidaException | SinistroJaRegistradoException e) {
            System.out.println("\n[!] Recusa no registro do sinistro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[!] Erro: " + e.getMessage());
        }
    }

    private void listarSinistros() {
        List<Sinistro> lista = controladorApolice.listarSinistros();
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum sinistro registrado.");
            return;
        }
        System.out.println("\n--- SINISTROS REGISTRADOS (" + lista.size() + ") ---");
        for (Sinistro s : lista) {
            System.out.println(s);
        }
    }

    private void laudarSinistro() {
        System.out.print("\nID do Sinistro a laudar: ");
        int sId = lerInteiro();
        System.out.print("Nome do Perito: ");
        String peritoNome = scanner.nextLine().trim();
        System.out.print("Registro Profissional do Perito: ");
        String peritoReg = scanner.nextLine().trim();
        System.out.print("Nome da Oficina Parceira: ");
        String oficinaNome = scanner.nextLine().trim();
        System.out.print("Parecer Técnico / Laudo: ");
        String laudo = scanner.nextLine().trim();
        System.out.print("Valor Total dos Reparos Aprovados (R$): ");
        double valorAprovado = lerDouble();

        Perito perito = new Perito(1, peritoNome, peritoReg, "Avarias Automotivas", "(11) 98888-7777", "perito@seguros.com");
        OficinaParceira oficina = new OficinaParceira(1, oficinaNome, "12.345.678/0001-99", "Av. Industrial 500", "(11) 3333-2222", "Carlos", 15.0);

        try {
            controladorApolice.laudarSinistro(sId, perito, oficina, laudo, valorAprovado);
            System.out.println("\n[OK] Sinistro laudado e atualizado com sucesso! Indenização calculada com desconto de franquia.");
        } catch (Exception e) {
            System.out.println("\n[!] Erro ao laudar: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. SUBMENU PAGAMENTOS
    // ==========================================
    private void menuPagamentos() {
        System.out.println("\n--- GESTÃO FINANCEIRA E PAGAMENTOS ---");
        System.out.println("1. Listar Pagamentos de uma Apólice");
        System.out.println("2. Quitar Parcela de Apólice");
        System.out.println("3. Listar Todos os Pagamentos");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");
        int op = lerInteiro();

        switch (op) {
            case 1:
                listarPagamentosApolice();
                break;
            case 2:
                quitarParcela();
                break;
            case 3:
                listarTodosPagamentos();
                break;
            default:
                break;
        }
    }

    private void listarPagamentosApolice() {
        System.out.print("\nID da Apólice: ");
        int apId = lerInteiro();
        List<Pagamento> lista = controladorApolice.listarPagamentosPorApolice(apId);
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum pagamento encontrado para a apólice ID " + apId);
            return;
        }
        System.out.println("\n--- Parcelas da Apólice ID " + apId + " ---");
        for (Pagamento p : lista) {
            System.out.println(p);
        }
    }

    private void quitarParcela() {
        System.out.print("\nID do Pagamento / Parcela: ");
        int pId = lerInteiro();
        System.out.print("Forma de Quitação (PIX / CARTAO / BOLETO): ");
        String forma = scanner.nextLine().trim();

        if (controladorApolice.quitarPagamento(pId, forma)) {
            System.out.println("\n[OK] Parcela ID " + pId + " quitada com sucesso!");
        } else {
            System.out.println("\n[!] Parcela não encontrada ou já quitada anteriormente.");
        }
    }

    private void listarTodosPagamentos() {
        List<Pagamento> lista = controladorApolice.listarTodosPagamentos();
        if (lista.isEmpty()) {
            System.out.println("\n[i] Nenhum pagamento registrado no sistema.");
            return;
        }
        System.out.println("\n--- Todos os Pagamentos Registrados (" + lista.size() + ") ---");
        for (Pagamento p : lista) {
            System.out.println(p);
        }
    }

    // ==========================================
    // 6. CARGA DE DADOS DE DEMONSTRAÇÃO
    // ==========================================
    private void carregarDadosExemplo() {
        System.out.println("\n[*] Criando dados de demonstração (Clientes, Veículos, Apólices)...");

        // 1. Cliente PF
        PessoaFisica pf = new PessoaFisica(0, "João Silva", "joao.silva@email.com", "(11) 98765-4321",
                "Rua das Flores, 123", "123.456.789-00", "MG-12.345.678", "15/05/1990", "M", EstadoCivil.CASADO);
        controladorCliente.cadastrarCliente(pf);

        // 2. Cliente PJ
        PessoaJuridica pj = new PessoaJuridica(0, "Transportes Rápidos LTDA", "contato@transrapidos.com",
                "(11) 3214-5678", "Av. Brasil, 4500", "12.345.678/0001-90", "Transportes Rápidos LTDA",
                "123.456.789.000", "TransRápidos", "Carlos Alberto");
        controladorCliente.cadastrarCliente(pj);

        // 3. Veículos
        Veiculo v1 = new Veiculo(0, pf.getId(), "Toyota", "Corolla XEi", 2022, "ABC-1D23", "12345678901", "9BR12345678901234", 125000.0);
        Veiculo v2 = new Veiculo(0, pj.getId(), "Mercedes-Benz", "Sprinter Van", 2023, "XYZ-9A87", "98765432109", "9BM98765432109876", 240000.0);
        try {
            controladorCliente.cadastrarVeiculo(v1);
            controladorCliente.cadastrarVeiculo(v2);
        } catch (ClienteNaoEncontradoException ignored) {}

        // 4. Apólice Auto para o PF
        try {
            ApoliceAuto auto = new ApoliceAuto(0, "", pf, 0.0, 3500.0, LocalDate.now(), LocalDate.now().plusYears(1), v1, 3, "Lazer e Trabalho");
            auto.adicionarCobertura(new Cobertura(1, "Colisão e Roubo", "Cobertura completa", 125000.0, 3500.0, TipoCobertura.COMPREENSIVA));
            controladorApolice.emitirApolice(auto, 6, "CARTAO");

            // 5. Apólice Residencial
            Imovel casa = new Imovel(1, pf.getId(), "01310-100", "Av. Paulista, 1000", "São Paulo", "SP", 650000.0, 140.0, "Alvenaria", true);
            ApoliceResidencial res = new ApoliceResidencial(0, "", pf, 0.0, 2000.0, LocalDate.now(), LocalDate.now().plusYears(1), casa, "Apartamento", true);
            res.adicionarCobertura(new Cobertura(2, "Incêndio e Vendaval", "Cobertura estrutural", 650000.0, 2000.0, TipoCobertura.INCENDIO));
            controladorApolice.emitirApolice(res, 4, "BOLETO");

            // 6. Apólice de Vida
            ApoliceDeVida vida = new ApoliceDeVida(0, "", pf, 0.0, 0.0, LocalDate.now(), LocalDate.now().plusYears(1), 300000.0, false, 34);
            vida.adicionarBeneficiario("Maria Silva (Esposa)");
            vida.adicionarCobertura(new Cobertura(3, "Morte Acidental e Invalidez", "Seguro de vida", 300000.0, 0.0, TipoCobertura.MORTE_ACIDENTAL));
            controladorApolice.emitirApolice(vida, 12, "PIX");

            System.out.println("[OK] Carga de demonstração concluída com sucesso! 2 Clientes, 2 Veículos e 3 Apólices criadas.");
        } catch (Exception e) {
            System.out.println("[!] Erro ao gerar carga de exemplo: " + e.getMessage());
        }
    }

    private int lerInteiro() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida! Digite um número inteiro: ");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida! Digite um valor numérico: ");
            }
        }
    }
}
