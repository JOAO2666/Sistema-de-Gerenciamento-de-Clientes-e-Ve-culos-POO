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
            System.out.println("\n--- Sistema de Clientes, Veículos e Seguros ---");
            System.out.println("1 - Clientes");
            System.out.println("2 - Veículos");
            System.out.println("3 - Apólices");
            System.out.println("4 - Sinistros");
            System.out.println("5 - Pagamentos");
            System.out.println("6 - Relatório geral");
            System.out.println("7 - Carregar dados de exemplo");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

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
                    exibirRelatorioGeral();
                    break;
                case 7:
                    carregarDadosExemplo();
                    break;
                case 0:
                    System.out.println("\nSaindo do sistema...");
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while (opcao != 0);
    }

    private void menuClientes() {
        System.out.println("\n--- Clientes ---");
        System.out.println("1 - Cadastrar Pessoa Física");
        System.out.println("2 - Cadastrar Pessoa Jurídica");
        System.out.println("3 - Listar clientes");
        System.out.println("4 - Buscar por CPF/CNPJ");
        System.out.println("5 - Buscar por ID");
        System.out.println("6 - Atualizar cliente");
        System.out.println("7 - Remover cliente");
        System.out.println("0 - Voltar");
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
                buscarClientePorId();
                break;
            case 6:
                atualizarCliente();
                break;
            case 7:
                removerCliente();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void cadastrarPF() {
        System.out.println("\nNovo cadastro - Pessoa Física");
        System.out.print("Nome: ");
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
        System.out.print("Data de nascimento (dd/mm/aaaa): ");
        String nasc = scanner.nextLine().trim();
        System.out.print("Sexo (M/F): ");
        String sexo = scanner.nextLine().trim();

        System.out.println("Estado civil (1-Solteiro, 2-Casado, 3-Divorciado, 4-Viúvo, 5-União Estável): ");
        int ecOp = lerInteiro();
        EstadoCivil ec = EstadoCivil.SOLTEIRO;
        if (ecOp == 2) ec = EstadoCivil.CASADO;
        else if (ecOp == 3) ec = EstadoCivil.DIVORCIADO;
        else if (ecOp == 4) ec = EstadoCivil.VIUVO;
        else if (ecOp == 5) ec = EstadoCivil.UNIAO_ESTAVEL;

        try {
            PessoaFisica pf = new PessoaFisica(0, nome, email, tel, end, cpf, rg, nasc, sexo, ec);
            controladorCliente.cadastrarCliente(pf);
            System.out.println("Cliente cadastrado (ID " + pf.getId() + ").");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }
    }

    private void cadastrarPJ() {
        System.out.println("\nNovo cadastro - Pessoa Jurídica");
        System.out.print("Nome fantasia: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Razão social: ");
        String razao = scanner.nextLine().trim();
        System.out.print("CNPJ: ");
        String cnpj = scanner.nextLine().trim();
        System.out.print("Inscrição estadual: ");
        String ie = scanner.nextLine().trim();
        System.out.print("Representante legal: ");
        String rep = scanner.nextLine().trim();
        System.out.print("E-mail: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine().trim();
        System.out.print("Endereço: ");
        String end = scanner.nextLine().trim();

        try {
            PessoaJuridica pj = new PessoaJuridica(0, nome, email, tel, end, cnpj, razao, ie, nome, rep);
            controladorCliente.cadastrarCliente(pj);
            System.out.println("Empresa cadastrada (ID " + pj.getId() + ").");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }
    }

    private void listarClientes() {
        List<Cliente> lista = controladorCliente.listarTodosClientes();
        if (lista.isEmpty()) {
            System.out.println("\nNenhum cliente cadastrado.");
            return;
        }
        System.out.println("\nClientes cadastrados (" + lista.size() + "):");
        for (Cliente c : lista) {
            System.out.println(c.exibirResumo());
        }
    }

    private void buscarClienteDocumento() {
        System.out.print("\nCPF ou CNPJ: ");
        String doc = scanner.nextLine().trim();
        try {
            Cliente c = controladorCliente.buscarPorDocumento(doc);
            System.out.println(c.exibirResumo());
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarClientePorId() {
        System.out.print("\nID do cliente: ");
        int id = lerInteiro();
        try {
            Cliente c = controladorCliente.buscarPorId(id);
            System.out.println(c.exibirResumo());
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarCliente() {
        System.out.print("\nID do cliente para atualizar: ");
        int id = lerInteiro();
        try {
            Cliente c = controladorCliente.buscarPorId(id);
            System.out.println("Atual: " + c.exibirResumo());
            System.out.println("Deixe em branco para manter o valor atual.");

            System.out.print("Novo e-mail (" + c.getEmail() + "): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                c.setEmail(email);
            }

            System.out.print("Novo telefone (" + c.getTelefone() + "): ");
            String tel = scanner.nextLine().trim();
            if (!tel.isEmpty()) {
                c.setTelefone(tel);
            }

            System.out.print("Novo endereço (" + c.getEndereco() + "): ");
            String end = scanner.nextLine().trim();
            if (!end.isEmpty()) {
                c.setEndereco(end);
            }

            controladorCliente.atualizarCliente(c);
            System.out.println("Cadastro atualizado.");
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removerCliente() {
        System.out.print("\nID do cliente: ");
        int id = lerInteiro();
        if (controladorCliente.removerCliente(id)) {
            System.out.println("Cliente removido.");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    private void menuVeiculos() {
        System.out.println("\n--- Veículos ---");
        System.out.println("1 - Cadastrar veículo");
        System.out.println("2 - Listar veículos de um cliente");
        System.out.println("3 - Listar todos os veículos");
        System.out.println("4 - Remover veículo");
        System.out.println("0 - Voltar");
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
            case 4:
                removerVeiculo();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void cadastrarVeiculo() {
        System.out.print("\nID do cliente proprietário: ");
        int clienteId = lerInteiro();

        try {
            controladorCliente.buscarPorId(clienteId);
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine().trim();
            System.out.print("Ano: ");
            int ano = lerInteiro();
            System.out.print("Placa: ");
            String placa = scanner.nextLine().trim();
            System.out.print("Renavam: ");
            String renavam = scanner.nextLine().trim();
            System.out.print("Chassi: ");
            String chassi = scanner.nextLine().trim();
            System.out.print("Valor na tabela FIPE (R$): ");
            double fipe = lerDouble();

            Veiculo v = new Veiculo(0, clienteId, marca, modelo, ano, placa, renavam, chassi, fipe);
            controladorCliente.cadastrarVeiculo(v);
            System.out.println("Veículo cadastrado (ID " + v.getId() + ").");
        } catch (ClienteNaoEncontradoException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarVeiculosCliente() {
        System.out.print("\nID do cliente: ");
        int id = lerInteiro();
        List<Veiculo> lista = controladorCliente.listarVeiculosDoCliente(id);
        if (lista.isEmpty()) {
            System.out.println("Nenhum veículo vinculado a esse cliente.");
            return;
        }
        System.out.println("\nVeículos do cliente " + id + ":");
        for (Veiculo v : lista) {
            System.out.println(v.getDescricaoCompleta());
        }
    }

    private void listarTodosVeiculos() {
        List<Veiculo> lista = controladorCliente.listarTodosVeiculos();
        if (lista.isEmpty()) {
            System.out.println("\nNenhum veículo cadastrado.");
            return;
        }
        System.out.println("\nVeículos cadastrados (" + lista.size() + "):");
        for (Veiculo v : lista) {
            System.out.println(v.getDescricaoCompleta() + " | Cliente ID: " + v.getClienteId());
        }
    }

    private void removerVeiculo() {
        System.out.print("\nID do veículo: ");
        int id = lerInteiro();
        if (controladorCliente.removerVeiculo(id)) {
            System.out.println("Veículo removido.");
        } else {
            System.out.println("Veículo não encontrado.");
        }
    }

    private void menuApolices() {
        System.out.println("\n--- Apólices ---");
        System.out.println("1 - Emitir apólice de automóvel");
        System.out.println("2 - Emitir apólice residencial");
        System.out.println("3 - Emitir apólice de vida");
        System.out.println("4 - Listar apólices");
        System.out.println("5 - Consultar apólice por ID");
        System.out.println("6 - Cancelar apólice");
        System.out.println("0 - Voltar");
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
            case 6:
                cancelarApolice();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void emitirApoliceAuto() {
        System.out.print("\nID do cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);
            List<Veiculo> veiculos = controladorCliente.listarVeiculosDoCliente(cId);
            if (veiculos.isEmpty()) {
                System.out.println("Esse cliente não tem veículos cadastrados.");
                return;
            }

            System.out.println("Escolha o veículo:");
            for (int i = 0; i < veiculos.size(); i++) {
                System.out.println((i + 1) + " - " + veiculos.get(i).getDescricaoCompleta());
            }
            int vEscolha = lerInteiro() - 1;
            if (vEscolha < 0 || vEscolha >= veiculos.size()) {
                System.out.println("Opção de veículo inválida.");
                return;
            }
            Veiculo veiculo = veiculos.get(vEscolha);

            System.out.print("Classe de bônus (0 a 10): ");
            int bonus = lerInteiro();
            System.out.print("Perfil do condutor (ex: Trabalho, Lazer): ");
            String perfil = scanner.nextLine().trim();
            System.out.print("Valor da franquia (R$): ");
            double franquia = lerDouble();
            System.out.print("Número de parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de pagamento (BOLETO, CARTAO ou PIX): ");
            String forma = scanner.nextLine().trim();

            ApoliceAuto auto = new ApoliceAuto(0, "", cliente, 0.0, franquia,
                    LocalDate.now(), LocalDate.now().plusYears(1), veiculo, bonus, perfil);

            auto.adicionarCobertura("Colisão e Danos Materiais", veiculo.getValorFipe(), franquia, TipoCobertura.COMPREENSIVA);
            auto.adicionarCobertura("Danos a Terceiros", 100000.0, 0.0, TipoCobertura.DANOS_A_TERCEIROS);

            Apolice emitida = controladorApolice.emitirApolice(auto, parcelas, forma);
            int totalParcelas = Math.max(1, emitida.getPagamentos().size());
            System.out.println("\nApólice emitida:");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio total: R$ %.2f (%dx de R$ %.2f)\n",
                    emitida.getValorPremio(), totalParcelas, (emitida.getValorPremio() / totalParcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException | IllegalArgumentException e) {
            System.out.println("Não foi possível emitir a apólice: " + e.getMessage());
        }
    }

    private void emitirApoliceResidencial() {
        System.out.print("\nID do cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);

            System.out.print("Endereço do imóvel: ");
            String end = scanner.nextLine().trim();
            System.out.print("CEP: ");
            String cep = scanner.nextLine().trim();
            System.out.print("Cidade: ");
            String cidade = scanner.nextLine().trim();
            System.out.print("UF: ");
            String uf = scanner.nextLine().trim();
            System.out.print("Valor do imóvel (R$): ");
            double valorMercado = lerDouble();
            System.out.print("Área construída (m²): ");
            double area = lerDouble();
            System.out.print("Possui alarme? (S/N): ");
            boolean alarme = scanner.nextLine().trim().equalsIgnoreCase("S");
            System.out.print("Valor da franquia (R$): ");
            double franquia = lerDouble();
            System.out.print("Número de parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de pagamento (BOLETO, CARTAO ou PIX): ");
            String forma = scanner.nextLine().trim();

            Imovel imovel = new Imovel(1, cId, cep, end, cidade, uf, valorMercado, area, "Alvenaria", alarme);
            ApoliceResidencial res = new ApoliceResidencial(0, "", cliente, 0.0, franquia,
                    LocalDate.now(), LocalDate.now().plusYears(1), imovel, "Residencial", alarme);

            res.adicionarCobertura("Incêndio e Raio", valorMercado, franquia, TipoCobertura.INCENDIO);
            res.adicionarCobertura("Roubo e Furto", valorMercado * 0.2, franquia, TipoCobertura.ROUBO_FURTO);

            Apolice emitida = controladorApolice.emitirApolice(res, parcelas, forma);
            int totalParcelas = Math.max(1, emitida.getPagamentos().size());
            System.out.println("\nApólice emitida:");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio total: R$ %.2f (%dx de R$ %.2f)\n",
                    emitida.getValorPremio(), totalParcelas, (emitida.getValorPremio() / totalParcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException | IllegalArgumentException e) {
            System.out.println("Não foi possível emitir a apólice: " + e.getMessage());
        }
    }

    private void emitirApoliceVida() {
        System.out.print("\nID do cliente: ");
        int cId = lerInteiro();
        try {
            Cliente cliente = controladorCliente.buscarPorId(cId);

            System.out.print("Capital segurado (R$): ");
            double capital = lerDouble();
            System.out.print("Idade do segurado: ");
            int idade = lerInteiro();
            System.out.print("Fumante? (S/N): ");
            boolean fumante = scanner.nextLine().trim().equalsIgnoreCase("S");
            System.out.print("Nome do beneficiário: ");
            String benef = scanner.nextLine().trim();
            System.out.print("Número de parcelas (1 a 12): ");
            int parcelas = lerInteiro();
            System.out.print("Forma de pagamento (BOLETO, CARTAO ou PIX): ");
            String forma = scanner.nextLine().trim();

            ApoliceDeVida vida = new ApoliceDeVida(0, "", cliente, 0.0, 0.0,
                    LocalDate.now(), LocalDate.now().plusYears(1), capital, fumante, idade);
            vida.adicionarBeneficiario(benef);

            vida.adicionarCobertura("Morte Acidental", capital, 0.0, TipoCobertura.MORTE_ACIDENTAL);
            vida.adicionarCobertura("Invalidez Permanente", capital, 0.0, TipoCobertura.INVALIDEZ_PERMANENTE);

            Apolice emitida = controladorApolice.emitirApolice(vida, parcelas, forma);
            int totalParcelas = Math.max(1, emitida.getPagamentos().size());
            System.out.println("\nApólice emitida:");
            emitida.exibirDetalhesApolice();
            System.out.printf("Prêmio total: R$ %.2f (%dx de R$ %.2f)\n",
                    emitida.getValorPremio(), totalParcelas, (emitida.getValorPremio() / totalParcelas));
        } catch (ClienteNaoEncontradoException | ValorPremioInvalidoException | IllegalArgumentException e) {
            System.out.println("Não foi possível emitir a apólice: " + e.getMessage());
        }
    }

    private void listarApolices() {
        List<Apolice> lista = controladorApolice.listarTodasApolices();
        if (lista.isEmpty()) {
            System.out.println("\nNenhuma apólice cadastrada.");
            return;
        }
        System.out.println("\nApólices (" + lista.size() + "):");
        for (Apolice a : lista) {
            System.out.println(a);
        }
    }

    private void detalharApolice() {
        System.out.print("\nID da apólice: ");
        int id = lerInteiro();
        Apolice a = controladorApolice.buscarApolicePorId(id);
        if (a == null) {
            System.out.println("Apólice não encontrada.");
            return;
        }
        a.exibirDetalhesApolice();
    }

    private void cancelarApolice() {
        System.out.print("\nID da apólice para cancelar: ");
        int id = lerInteiro();
        if (controladorApolice.cancelarApolice(id)) {
            System.out.println("Apólice " + id + " cancelada.");
        } else {
            System.out.println("Apólice não encontrada ou já cancelada.");
        }
    }

    private void menuSinistros() {
        System.out.println("\n--- Sinistros ---");
        System.out.println("1 - Registrar sinistro");
        System.out.println("2 - Listar sinistros");
        System.out.println("3 - Registrar laudo da perícia");
        System.out.println("4 - Liquidar indenização");
        System.out.println("0 - Voltar");
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
            case 4:
                liquidarSinistro();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void registrarSinistro() {
        System.out.print("\nID da apólice: ");
        int apId = lerInteiro();
        System.out.println("Tipo (1-Colisão, 2-Roubo/Furto, 3-Incêndio, 4-Alagamento, 5-Danos a Terceiros): ");
        int tOp = lerInteiro();
        TipoSinistro tipo = TipoSinistro.COLISAO;
        if (tOp == 2) tipo = TipoSinistro.ROUBO_FURTO;
        else if (tOp == 3) tipo = TipoSinistro.INCENDIO;
        else if (tOp == 4) tipo = TipoSinistro.ALAGAMENTO;
        else if (tOp == 5) tipo = TipoSinistro.DANOS_A_TERCEIROS;

        System.out.print("Descrição do ocorrido: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Prejuízo estimado (R$): ");
        double prejuizo = lerDouble();

        try {
            Sinistro s = controladorApolice.registrarSinistro(apId, LocalDate.now(), tipo, desc, prejuizo);
            System.out.println("Sinistro registrado (ID " + s.getId() + ", protocolo " + s.getNumeroProtocolo() + ").");
        } catch (ApoliceVencidaException | SinistroJaRegistradoException e) {
            System.out.println("Sinistro recusado: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarSinistros() {
        List<Sinistro> lista = controladorApolice.listarSinistros();
        if (lista.isEmpty()) {
            System.out.println("\nNenhum sinistro registrado.");
            return;
        }
        System.out.println("\nSinistros (" + lista.size() + "):");
        for (Sinistro s : lista) {
            System.out.println(s);
        }
    }

    private void laudarSinistro() {
        System.out.print("\nID do sinistro: ");
        int sId = lerInteiro();
        System.out.print("Nome do perito: ");
        String peritoNome = scanner.nextLine().trim();
        System.out.print("Registro do perito: ");
        String peritoReg = scanner.nextLine().trim();
        System.out.print("Oficina parceira: ");
        String oficinaNome = scanner.nextLine().trim();
        System.out.print("Parecer / laudo: ");
        String laudo = scanner.nextLine().trim();
        System.out.print("Valor aprovado para reparo (R$): ");
        double valorAprovado = lerDouble();

        Perito perito = new Perito(1, peritoNome, peritoReg, "Vistoria de Sinistros", "(11) 98888-7777", "perito@seguros.com");
        OficinaParceira oficina = new OficinaParceira(1, oficinaNome, "12.345.678/0001-99", "Av. Industrial 500", "(11) 3333-2222", "Carlos", 15.0);

        try {
            controladorApolice.laudarSinistro(sId, perito, oficina, laudo, valorAprovado);
            System.out.println("Laudo registrado e indenização atualizada.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar laudo: " + e.getMessage());
        }
    }

    private void liquidarSinistro() {
        System.out.print("\nID do sinistro para liquidar: ");
        int sId = lerInteiro();
        try {
            controladorApolice.liquidarSinistro(sId);
            System.out.println("Sinistro " + sId + " liquidado (pago).");
        } catch (Exception e) {
            System.out.println("Erro ao liquidar sinistro: " + e.getMessage());
        }
    }

    private void menuPagamentos() {
        System.out.println("\n--- Pagamentos ---");
        System.out.println("1 - Listar parcelas de uma apólice");
        System.out.println("2 - Dar baixa em parcela");
        System.out.println("3 - Listar todos os pagamentos");
        System.out.println("0 - Voltar");
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
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void listarPagamentosApolice() {
        System.out.print("\nID da apólice: ");
        int apId = lerInteiro();
        List<Pagamento> lista = controladorApolice.listarPagamentosPorApolice(apId);
        if (lista.isEmpty()) {
            System.out.println("Nenhuma parcela encontrada para essa apólice.");
            return;
        }
        System.out.println("\nParcelas da apólice " + apId + ":");
        for (Pagamento p : lista) {
            System.out.println(p);
        }
    }

    private void quitarParcela() {
        System.out.print("\nID da parcela: ");
        int pId = lerInteiro();
        System.out.print("Forma de pagamento (PIX, CARTAO ou BOLETO): ");
        String forma = scanner.nextLine().trim();

        if (controladorApolice.quitarPagamento(pId, forma)) {
            System.out.println("Parcela " + pId + " quitada.");
        } else {
            System.out.println("Parcela não encontrada ou já paga.");
        }
    }

    private void listarTodosPagamentos() {
        List<Pagamento> lista = controladorApolice.listarTodosPagamentos();
        if (lista.isEmpty()) {
            System.out.println("\nNenhum pagamento registrado.");
            return;
        }
        System.out.println("\nPagamentos (" + lista.size() + "):");
        for (Pagamento p : lista) {
            System.out.println(p);
        }
    }

    private void exibirRelatorioGeral() {
        List<Cliente> clientes = controladorCliente.listarTodosClientes();
        List<Veiculo> veiculos = controladorCliente.listarTodosVeiculos();
        List<Apolice> apolices = controladorApolice.listarTodasApolices();
        List<Sinistro> sinistros = controladorApolice.listarSinistros();
        List<Pagamento> pagamentos = controladorApolice.listarTodosPagamentos();

        double totalPremios = 0.0;
        for (Apolice a : apolices) {
            totalPremios += a.getValorPremio();
        }

        double totalRecebido = 0.0;
        int parcelasPagas = 0;
        for (Pagamento p : pagamentos) {
            if (p.isPago()) {
                totalRecebido += p.getValor();
                parcelasPagas++;
            }
        }

        System.out.println("\n--- Relatório Geral ---");
        System.out.println("Clientes cadastrados: " + clientes.size());
        System.out.println("Veículos cadastrados: " + veiculos.size());
        System.out.println("Apólices emitidas: " + apolices.size());
        System.out.printf("Soma dos prêmios: R$ %.2f\n", totalPremios);
        System.out.printf("Parcelas pagas: %d de %d (R$ %.2f recebidos)\n",
                parcelasPagas, pagamentos.size(), totalRecebido);
        System.out.println("Sinistros registrados: " + sinistros.size());
    }

    private void carregarDadosExemplo() {
        if (!controladorCliente.listarTodosClientes().isEmpty()) {
            System.out.println("\nJá existem dados cadastrados no sistema.");
            return;
        }

        PessoaFisica pf = new PessoaFisica(0, "João Silva", "joao.silva@email.com", "(11) 98765-4321",
                "Rua das Flores, 123", "123.456.789-00", "MG-12.345.678", "15/05/1990", "M", EstadoCivil.CASADO);
        controladorCliente.cadastrarCliente(pf);

        PessoaJuridica pj = new PessoaJuridica(0, "Transportes Rápidos LTDA", "contato@transrapidos.com",
                "(11) 3214-5678", "Av. Brasil, 4500", "12.345.678/0001-90", "Transportes Rápidos LTDA",
                "123.456.789.000", "TransRápidos", "Carlos Alberto");
        controladorCliente.cadastrarCliente(pj);

        Veiculo v1 = new Veiculo(0, pf.getId(), "Toyota", "Corolla XEi", 2022, "ABC-1D23", "12345678901", "9BR12345678901234", 125000.0);
        Veiculo v2 = new Veiculo(0, pj.getId(), "Mercedes-Benz", "Sprinter Van", 2023, "XYZ-9A87", "98765432109", "9BM98765432109876", 240000.0);
        try {
            controladorCliente.cadastrarVeiculo(v1);
            controladorCliente.cadastrarVeiculo(v2);
        } catch (ClienteNaoEncontradoException ignored) {}

        try {
            ApoliceAuto auto = new ApoliceAuto(0, "", pf, 0.0, 3500.0, LocalDate.now(), LocalDate.now().plusYears(1), v1, 3, "Lazer e Trabalho");
            auto.adicionarCobertura("Colisão e Roubo", 125000.0, 3500.0, TipoCobertura.COMPREENSIVA);
            controladorApolice.emitirApolice(auto, 6, "CARTAO");

            Imovel casa = new Imovel(1, pf.getId(), "01310-100", "Av. Paulista, 1000", "São Paulo", "SP", 650000.0, 140.0, "Alvenaria", true);
            ApoliceResidencial res = new ApoliceResidencial(0, "", pf, 0.0, 2000.0, LocalDate.now(), LocalDate.now().plusYears(1), casa, "Apartamento", true);
            res.adicionarCobertura("Incêndio e Vendaval", 650000.0, 2000.0, TipoCobertura.INCENDIO);
            controladorApolice.emitirApolice(res, 4, "BOLETO");

            ApoliceDeVida vida = new ApoliceDeVida(0, "", pf, 0.0, 0.0, LocalDate.now(), LocalDate.now().plusYears(1), 300000.0, false, 34);
            vida.adicionarBeneficiario("Maria Silva (Esposa)");
            vida.adicionarCobertura("Morte Acidental e Invalidez", 300000.0, 0.0, TipoCobertura.MORTE_ACIDENTAL);
            controladorApolice.emitirApolice(vida, 12, "PIX");

            System.out.println("\nDados de exemplo carregados (2 clientes, 2 veículos e 3 apólices).");
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados de exemplo: " + e.getMessage());
        }
    }

    private int lerInteiro() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro: ");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Digite um valor numérico: ");
            }
        }
    }
}
