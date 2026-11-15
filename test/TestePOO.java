import controller.ControladorCliente;
import exception.ApoliceVencidaException;
import exception.ClienteNaoEncontradoException;
import exception.SinistroJaRegistradoException;
import exception.ValorPremioInvalidoException;
import model.*;
import model.enums.EstadoCivil;
import model.enums.StatusApolice;
import model.enums.TipoCobertura;
import model.enums.TipoSinistro;
import repository.ApoliceDAO;
import repository.ClienteDAO;
import repository.PagamentoDAO;
import repository.SinistroDAO;
import repository.VeiculoDAO;
import service.CalculadoraDePremioService;
import service.EmissorDeApoliceService;
import service.ProcessadorDeSinistroService;
import service.ValidadorDePropostaService;

import java.time.LocalDate;
import java.util.List;

public class TestePOO {
    private static int testesPassados = 0;
    private static int testesTotais = 0;

    public static void main(String[] args) {
        System.out.println("Iniciando bateria de testes...\n");

        testarHierarquiaClientesEPessoas();
        testarHierarquiaApoliceEPolimorfismo();
        testarEmissaoDeApoliceEParcelas();
        testarExcecaoClienteNaoEncontrado();
        testarExcecaoApoliceVencida();
        testarExcecaoSinistroDuplicado();
        testarExcecaoValorPremioInvalido();
        testarFluxoCompletoSinistroEValidacoes();
        testarCadastrosDuplicadosEPersistencia();

        System.out.printf("\nResultado: %d de %d testes passaram.\n", testesPassados, testesTotais);

        if (testesPassados != testesTotais) {
            System.exit(1);
        }
    }

    private static void verificar(boolean condicao, String descricao) {
        testesTotais++;
        if (condicao) {
            testesPassados++;
            System.out.println("[OK] " + descricao);
        } else {
            System.err.println("[FALHA] " + descricao);
        }
    }

    private static void testarHierarquiaClientesEPessoas() {
        System.out.println("-- Teste 1: Heranca e classes abstratas (Cliente e Pessoa) --");

        Cliente pf = new PessoaFisica(1, "Ana Souza", "ana@email.com", "1199999999", "Rua A",
                "111.222.333-44", "MG-112233", "20/10/1995", "F", EstadoCivil.SOLTEIRO);

        Cliente pj = new PessoaJuridica(2, "Tech Solutions", "contato@tech.com", "1133334444", "Av B",
                "11.222.333/0001-44", "Tech Solutions LTDA", "123456", "Tech Sol", "Marcos");

        Pessoa perito = new Perito(1, "Roberto Lima", "666.777.888-99", "8798811-2233", "M",
                "roberto@pericia.com", "CREA-PE 44556", "Automotiva");

        verificar(pf instanceof Cliente && pf instanceof Identificavel, "PessoaFisica herda de Cliente e implementa Identificavel");
        verificar(pj instanceof Cliente && pj instanceof Identificavel, "PessoaJuridica herda de Cliente e implementa Identificavel");
        verificar(perito instanceof Pessoa && perito instanceof Identificavel, "Perito herda de Pessoa e implementa Identificavel");
        verificar("111.222.333-44".equals(pf.getDocumentoPrincipal()), "getDocumentoPrincipal retorna CPF na PessoaFisica");
        verificar("11.222.333/0001-44".equals(pj.getDocumentoPrincipal()), "getDocumentoPrincipal retorna CNPJ na PessoaJuridica");
        verificar(perito.exibirResumo().contains("CREA-PE 44556"), "Metodo polimorfico exibirResumo de Perito (Pessoa)");
    }

    private static void testarHierarquiaApoliceEPolimorfismo() {
        System.out.println("\n-- Teste 2: Polimorfismo (sobrescrita e sobrecarga) em Apolices --");

        Cliente pf = new PessoaFisica(10, "Carlos", "carlos@email.com", "1198888", "Rua C",
                "222.333.444-55", "RG-9988", "10/02/1988", "M", EstadoCivil.CASADO);

        Veiculo v = new Veiculo(1, 10, "Honda", "Civic", 2021, "ABC-1234", "12345", "CH123", 100000.0);
        Imovel im = new Imovel(1, 10, "01001-000", "Praca da Se", "Sao Paulo", "SP", 500000.0, 90.0, "Alvenaria", true);

        verificar(v instanceof Seguravel && im instanceof Seguravel, "Veiculo e Imovel implementam Seguravel");

        ApoliceAuto auto = new ApoliceAuto(1, "AP-AUTO-01", pf, 0.0, 3000.0,
                LocalDate.now(), LocalDate.now().plusYears(1), v, 2, "Trabalho");
        auto.adicionarCobertura("Vidros", 5000.0, 350.0, TipoCobertura.VIDROS);

        ApoliceResidencial res = new ApoliceResidencial(2, "AP-RES-01", pf, 0.0, 1500.0,
                LocalDate.now(), LocalDate.now().plusYears(1), im, "Apartamento", true);

        ApoliceDeVida vida = new ApoliceDeVida(3, "AP-VIDA-01", pf, 0.0, 0.0,
                LocalDate.now(), LocalDate.now().plusYears(1), 200000.0, false, 30);

        verificar(auto.getCoberturas().size() == 1, "Sobrecarga adicionarCobertura na Apolice");

        CalculadoraDePremioService calc = new CalculadoraDePremioService();
        try {
            double premioAuto = calc.calcularPremio(auto);
            double premioAutoComDesconto = calc.calcularPremio(auto, 10.0);
            double premioRes = calc.calcularPremio(res);
            double premioVida = calc.calcularPremio(vida);

            verificar(premioAuto > 0 && premioRes > 0 && premioVida > 0,
                    "Calculo de premio polimorfico por subtipo (Auto, Residencial e Vida)");
            verificar(premioAutoComDesconto < premioAuto,
                    "Sobrecarga calcularPremio com percentual de desconto");
        } catch (ValorPremioInvalidoException e) {
            verificar(false, "Falha inesperada no calculo de premio: " + e.getMessage());
        }
    }

    private static void testarEmissaoDeApoliceEParcelas() {
        System.out.println("\n-- Teste 3: Emissao de apolice, sobrecarga e persistencia (DAO) --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        PagamentoDAO pagDao = new PagamentoDAO();
        ApoliceDAO apDao = new ApoliceDAO(cliDao, veiDao);

        EmissorDeApoliceService emissor = new EmissorDeApoliceService(apDao, pagDao);

        PessoaFisica pf = new PessoaFisica(0, "Mariana", "mariana@email.com", "1197777", "Rua D",
                "333.444.555-66", "RG-7766", "12/12/1992", "F", EstadoCivil.SOLTEIRO);
        cliDao.salvar(pf);

        Veiculo v = new Veiculo(0, pf.getId(), "Hyundai", "HB20", 2020, "XYZ-9988", "99881", "CH9988", 60000.0);
        veiDao.salvar(v);

        ApoliceAuto proposta = new ApoliceAuto(0, "", pf, 0.0, 2500.0,
                LocalDate.now(), LocalDate.now().plusYears(1), v, 1, "Lazer");

        try {
            boolean recusouParcelaInvalida = false;
            try {
                emissor.emitirApolice(proposta, 0, "PIX");
            } catch (IllegalArgumentException e) {
                recusouParcelaInvalida = true;
            }
            verificar(recusouParcelaInvalida, "Recusa emissao com quantidade de parcelas fora de 1..12");

            Apolice emitida = emissor.emitirApolice(proposta, 5, "   ");
            verificar(emitida.getStatus() == StatusApolice.ATIVA, "Status da apolice emitida passa para ATIVA");
            verificar(emitida.getId() > 0, "Apolice salva com ID gerado");

            List<Pagamento> parcelas = pagDao.listarPorApolice(emitida.getId());
            verificar(parcelas.size() == 5, "Geracao de 5 parcelas no PagamentoDAO");
            verificar("PIX".equals(parcelas.get(0).getFormaPagamento()), "Forma de pagamento em branco assume PIX");

            double somaParcelas = 0.0;
            for (Pagamento p : parcelas) {
                somaParcelas += p.getValor();
            }
            verificar(Math.abs(somaParcelas - emitida.getValorPremio()) < 0.01, "Soma das parcelas corresponde ao premio total");

            Pagamento primeiraParcela = parcelas.get(0);
            primeiraParcela.registrarQuitacao();
            verificar(primeiraParcela.isPago(), "Sobrecarga registrarQuitacao sem argumentos quita na data atual");

            for (Pagamento p : parcelas) {
                pagDao.deletar(p.getId());
            }
            apDao.deletar(emitida.getId());
        } catch (Exception e) {
            verificar(false, "Erro na emissao de apolice: " + e.getMessage());
        } finally {
            veiDao.deletar(v.getId());
            cliDao.deletar(pf.getId());
        }
    }

    private static void testarExcecaoClienteNaoEncontrado() {
        System.out.println("\n-- Teste 4: Excecao ClienteNaoEncontradoException --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ControladorCliente ctrl = new ControladorCliente(cliDao, veiDao);

        boolean lancou = false;
        try {
            ctrl.buscarPorId(999999);
        } catch (ClienteNaoEncontradoException e) {
            lancou = true;
        }
        verificar(lancou, "ClienteNaoEncontradoException lancada ao buscar ID inexistente");
    }

    private static void testarExcecaoApoliceVencida() {
        System.out.println("\n-- Teste 5: Excecao ApoliceVencidaException --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ApoliceDAO apDao = new ApoliceDAO(cliDao, veiDao);
        SinistroDAO sinDao = new SinistroDAO();
        ProcessadorDeSinistroService proc = new ProcessadorDeSinistroService(apDao, sinDao);

        PessoaFisica pf = new PessoaFisica(0, "Segurado Teste", "teste@email.com", "1198888", "Rua E",
                "444.555.666-77", "RG-5544", "01/01/1980", "M", EstadoCivil.CASADO);
        cliDao.salvar(pf);

        ApoliceAuto apVencida = new ApoliceAuto(0, "AP-VENCIDA", pf, 1500.0, 2000.0,
                LocalDate.now().minusYears(2), LocalDate.now().minusDays(1), null, 0, "Padrao");
        apVencida.setStatus(StatusApolice.EXPIRADA);
        apDao.salvar(apVencida);

        boolean lancou = false;
        try {
            proc.registrarSinistro(apVencida.getId(), LocalDate.now(), TipoSinistro.COLISAO, "Batida leve", 3000.0);
        } catch (ApoliceVencidaException e) {
            lancou = true;
        } catch (Exception ignored) {
        } finally {
            apDao.deletar(apVencida.getId());
            cliDao.deletar(pf.getId());
        }

        verificar(lancou, "ApoliceVencidaException lancada ao abrir sinistro em apolice expirada");
    }

    private static void testarExcecaoSinistroDuplicado() {
        System.out.println("\n-- Teste 6: Excecao SinistroJaRegistradoException --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ApoliceDAO apDao = new ApoliceDAO(cliDao, veiDao);
        SinistroDAO sinDao = new SinistroDAO();
        ProcessadorDeSinistroService proc = new ProcessadorDeSinistroService(apDao, sinDao);

        PessoaFisica pf = new PessoaFisica(0, "Segurado Duplicidade", "dup@email.com", "1198888", "Rua F",
                "555.666.777-88", "RG-6655", "01/01/1985", "M", EstadoCivil.CASADO);
        cliDao.salvar(pf);

        ApoliceAuto apAtiva = new ApoliceAuto(0, "AP-DUPLICIDADE", pf, 1800.0, 2000.0,
                LocalDate.now(), LocalDate.now().plusYears(1), null, 0, "Padrao");
        apAtiva.setStatus(StatusApolice.ATIVA);
        apDao.salvar(apAtiva);

        LocalDate hoje = LocalDate.now();
        boolean lancou = false;
        Sinistro criado = null;
        try {
            criado = proc.registrarSinistro(apAtiva.getId(), hoje, TipoSinistro.COLISAO, "Primeira colisao", 2500.0);
            proc.registrarSinistro(apAtiva.getId(), hoje, TipoSinistro.COLISAO, "Tentativa duplicada", 2500.0);
        } catch (SinistroJaRegistradoException e) {
            lancou = true;
        } catch (Exception ignored) {
        } finally {
            if (criado != null) {
                sinDao.deletar(criado.getId());
            }
            apDao.deletar(apAtiva.getId());
            cliDao.deletar(pf.getId());
        }

        verificar(lancou, "SinistroJaRegistradoException lancada ao registrar evento duplicado");
    }

    private static void testarExcecaoValorPremioInvalido() {
        System.out.println("\n-- Teste 7: Excecao ValorPremioInvalidoException e validacoes de proposta --");

        Cliente pf = new PessoaFisica(20, "Lucas", "lucas@email.com", "119999", "Rua G",
                "777.888.999-00", "RG-1122", "05/05/1990", "M", EstadoCivil.SOLTEIRO);

        ValidadorDePropostaService validador = new ValidadorDePropostaService();
        CalculadoraDePremioService calc = new CalculadoraDePremioService();

        ApoliceDeVida vidaInvalida = new ApoliceDeVida(4, "AP-INV", pf, 0.0, 0.0,
                LocalDate.now(), LocalDate.now().plusYears(1), 0.0, false, 25);

        boolean lancouVida = false;
        try {
            validador.validarProposta(vidaInvalida);
        } catch (ValorPremioInvalidoException e) {
            lancouVida = true;
        } catch (ClienteNaoEncontradoException ignored) {
        }
        verificar(lancouVida, "ValorPremioInvalidoException lancada quando capital segurado e zerado");

        Veiculo veiculoZero = new Veiculo(1, 20, "Fiat", "Uno", 2020, "TST-0001", "111", "CH1", 0.0);
        ApoliceAuto autoFipeZero = new ApoliceAuto(5, "AP-AUTO-INV", pf, 0.0, -50.0,
                LocalDate.now(), LocalDate.now().plusYears(1), veiculoZero, 0, "Padrao");

        boolean lancouFranquia = false;
        try {
            validador.validarProposta(autoFipeZero);
        } catch (ValorPremioInvalidoException e) {
            lancouFranquia = true;
        } catch (ClienteNaoEncontradoException ignored) {
        }
        verificar(lancouFranquia, "ValorPremioInvalidoException lancada para franquia negativa");

        autoFipeZero.setValorFranquia(1500.0);
        boolean lancouFipe = false;
        try {
            validador.validarProposta(autoFipeZero);
        } catch (ValorPremioInvalidoException e) {
            lancouFipe = true;
        } catch (ClienteNaoEncontradoException ignored) {
        }
        verificar(lancouFipe, "ValorPremioInvalidoException lancada quando valor FIPE e zero");

        Imovel imovelInvalido = new Imovel(1, 20, "01001-000", "Rua Teste", "SP", "SP", 0.0, 0.0, "Alvenaria", false);
        ApoliceResidencial resInvalida = new ApoliceResidencial(6, "AP-RES-INV", pf, 0.0, 1000.0,
                LocalDate.now(), LocalDate.now().plusYears(1), imovelInvalido, "Casa", false);
        boolean lancouImovel = false;
        try {
            validador.validarProposta(resInvalida);
        } catch (ValorPremioInvalidoException e) {
            lancouImovel = true;
        } catch (ClienteNaoEncontradoException ignored) {
        }
        verificar(lancouImovel, "ValorPremioInvalidoException lancada quando valor de mercado do imovel e zero");

        ApoliceDeVida vidaValida = new ApoliceDeVida(7, "AP-DESC", pf, 0.0, 0.0,
                LocalDate.now(), LocalDate.now().plusYears(1), 100000.0, false, 30);
        boolean lancouDescontoNegativo = false;
        boolean lancouDescontoCem = false;
        try {
            calc.calcularPremio(vidaValida, -10.0);
        } catch (ValorPremioInvalidoException e) {
            lancouDescontoNegativo = true;
        }
        try {
            calc.calcularPremio(vidaValida, 100.0);
        } catch (ValorPremioInvalidoException e) {
            lancouDescontoCem = true;
        }
        verificar(lancouDescontoNegativo && lancouDescontoCem,
                "Calculadora recusa descontos negativos ou iguais/maiores que 100%");
    }

    private static void testarFluxoCompletoSinistroEValidacoes() {
        System.out.println("\n-- Teste 8: Ciclo de sinistro, laudo pericial e bloqueios --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ApoliceDAO apDao = new ApoliceDAO(cliDao, veiDao);
        SinistroDAO sinDao = new SinistroDAO();
        ProcessadorDeSinistroService proc = new ProcessadorDeSinistroService(apDao, sinDao);

        PessoaFisica pf = new PessoaFisica(0, "Cliente Sinistro", "sin@email.com", "1191111", "Rua H",
                "888.999.000-11", "RG-2233", "03/03/1991", "M", EstadoCivil.CASADO);
        cliDao.salvar(pf);

        ApoliceAuto apCancelada = new ApoliceAuto(0, "AP-CANC", pf, 1800.0, 2000.0,
                LocalDate.now(), LocalDate.now().plusYears(1), null, 0, "Padrao");
        apCancelada.setStatus(StatusApolice.CANCELADA);
        apDao.salvar(apCancelada);

        ApoliceAuto apAtiva = new ApoliceAuto(0, "AP-SIN-OK", pf, 2000.0, 1500.0,
                LocalDate.now(), LocalDate.now().plusYears(1), null, 0, "Padrao");
        apAtiva.setStatus(StatusApolice.ATIVA);
        apDao.salvar(apAtiva);

        Sinistro s1 = null;
        Sinistro s2 = null;
        try {
            boolean recusouCancelada = false;
            try {
                proc.registrarSinistro(apCancelada.getId(), LocalDate.now(), TipoSinistro.COLISAO, "Batida", 3000.0);
            } catch (IllegalStateException e) {
                recusouCancelada = true;
            }
            verificar(recusouCancelada, "Recusa abertura de sinistro em apolice cancelada");

            boolean recusouPrejuizoZero = false;
            try {
                proc.registrarSinistro(apAtiva.getId(), LocalDate.now(), TipoSinistro.COLISAO, "Sem prejuizo", 0.0);
            } catch (IllegalArgumentException e) {
                recusouPrejuizoZero = true;
            }
            verificar(recusouPrejuizoZero, "Recusa sinistro com prejuizo menor ou igual a zero");

            s1 = proc.registrarSinistro(apAtiva.getId(), LocalDate.now(), TipoSinistro.COLISAO, "Batida traseira", 8000.0);
            s2 = proc.registrarSinistro(apAtiva.getId(), LocalDate.now(), TipoSinistro.ROUBO_FURTO, "Estepe furtado", 2500.0);
            verificar(!s1.getNumeroProtocolo().equals(s2.getNumeroProtocolo()),
                    "Protocolos sequenciais nao colidem em chamadas consecutivas");

            boolean recusouLiquidarSemLaudo = false;
            try {
                proc.liquidarSinistro(s1.getId());
            } catch (IllegalStateException e) {
                recusouLiquidarSemLaudo = true;
            }
            verificar(recusouLiquidarSemLaudo, "Recusa liquidar sinistro antes da aprovacao da pericia");

            boolean recusouValorNegativo = false;
            Perito perito = new Perito(1, "Carlos Perito", "CREA-SP 1234", "Auto", "11999990000", "perito@email.com");
            OficinaParceira oficina = new OficinaParceira(1, "Oficina Central", "11.111.111/0001-11", "Av 1", "113333", "Joao", 10.0);
            try {
                proc.analisarELaudarSinistro(s1.getId(), perito, oficina, "Laudo negativo", -100.0);
            } catch (IllegalArgumentException e) {
                recusouValorNegativo = true;
            }
            verificar(recusouValorNegativo, "Recusa laudo com valor aprovado negativo");

            proc.analisarELaudarSinistro(s1.getId(), perito, oficina, "Substituicao parachoque", 5000.0);
            verificar("APROVADO".equals(s1.getStatusSinistro()) && Math.abs(s1.getValorIndenizado() - 3500.0) < 0.01,
                    "Laudo desconta a franquia corretamente e aprova sinistro");

            proc.liquidarSinistro(s1.getId());
            verificar("PAGO_CONCLUIDO".equals(s1.getStatusSinistro()), "Liquidacao conclui pagamento apos laudo aprovado");

            boolean recusouDuplaLiquidacao = false;
            try {
                proc.liquidarSinistro(s1.getId());
            } catch (IllegalStateException e) {
                recusouDuplaLiquidacao = true;
            }
            verificar(recusouDuplaLiquidacao, "Recusa nova liquidacao em sinistro ja pago");
        } catch (Exception e) {
            verificar(false, "Erro inesperado no fluxo de sinistro: " + e.getMessage());
        } finally {
            if (s1 != null) sinDao.deletar(s1.getId());
            if (s2 != null) sinDao.deletar(s2.getId());
            apDao.deletar(apCancelada.getId());
            apDao.deletar(apAtiva.getId());
            cliDao.deletar(pf.getId());
        }
    }

    private static void testarCadastrosDuplicadosEPersistencia() {
        System.out.println("\n-- Teste 9: Bloqueio de duplicidade e persistencia com caracteres especiais --");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ControladorCliente ctrl = new ControladorCliente(cliDao, veiDao);

        PessoaFisica pf = new PessoaFisica(0, "Ana;Maria", "ana.p@email.com", "1190000", "Rua X; Bloco B\nApto 1",
                "999.888.777-66", "RG-9988", "04/04/1994", "F", EstadoCivil.CASADO);
        PessoaFisica pfDuplicada = new PessoaFisica(0, "Ana Outra", "ana2@email.com", "1190000", "Rua Y",
                "99988877766", "RG-0001", "04/04/1994", "F", EstadoCivil.SOLTEIRO);

        Veiculo v = null;
        try {
            ctrl.cadastrarCliente(pf);

            boolean recusouDocDuplicado = false;
            try {
                ctrl.cadastrarCliente(pfDuplicada);
            } catch (IllegalArgumentException e) {
                recusouDocDuplicado = true;
            }
            verificar(recusouDocDuplicado, "ControladorCliente bloqueia CPF duplicado mesmo sem pontuacao");

            v = new Veiculo(0, pf.getId(), "VW", "Polo;Highline", 2022, "TST-9Z99", "55555", "CH555", 85000.0);
            ctrl.cadastrarVeiculo(v);

            boolean recusouPlacaDuplicada = false;
            try {
                ctrl.cadastrarVeiculo(new Veiculo(0, pf.getId(), "VW", "Polo", 2022, "tst9z99", "66666", "CH666", 85000.0));
            } catch (IllegalArgumentException e) {
                recusouPlacaDuplicada = true;
            }
            verificar(recusouPlacaDuplicada, "ControladorCliente bloqueia placa duplicada");

            boolean recusouAnoInvalido = false;
            try {
                ctrl.cadastrarVeiculo(new Veiculo(0, pf.getId(), "VW", "Gol", 1800, "OLD-0001", "777", "CH7", 10000.0));
            } catch (IllegalArgumentException e) {
                recusouAnoInvalido = true;
            }
            verificar(recusouAnoInvalido, "ControladorCliente bloqueia veiculo com ano invalido");

            ClienteDAO cliRecarregado = new ClienteDAO();
            VeiculoDAO veiRecarregado = new VeiculoDAO();

            Cliente clienteLido = cliRecarregado.buscarPorId(pf.getId());
            Veiculo veiculoLido = veiRecarregado.buscarPorId(v.getId());

            verificar(clienteLido instanceof PessoaFisica
                            && ((PessoaFisica) clienteLido).getEstadoCivil() == EstadoCivil.CASADO
                            && !clienteLido.getNome().contains(";"),
                    "Cliente com ponto e virgula no nome e quebra no endereco recarrega sem deslocar colunas");
            verificar(veiculoLido != null && veiculoLido.getModelo().contains("Polo,Highline")
                            && Math.abs(veiculoLido.getValorFipe() - 85000.0) < 0.01,
                    "Veiculo com caractere especial recarrega com valor FIPE preservado");
        } catch (Exception e) {
            verificar(false, "Erro inesperado no teste de persistencia: " + e.getMessage());
        } finally {
            if (v != null) veiDao.deletar(v.getId());
            cliDao.deletar(pf.getId());
        }
    }
}
