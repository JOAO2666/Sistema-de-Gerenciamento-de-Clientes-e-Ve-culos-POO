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
            Apolice emitida = emissor.emitirApolice(proposta, 5, "BOLETO");
            verificar(emitida.getStatus() == StatusApolice.ATIVA, "Status da apolice emitida passa para ATIVA");
            verificar(emitida.getId() > 0, "Apolice salva com ID gerado");

            List<Pagamento> parcelas = pagDao.listarPorApolice(emitida.getId());
            verificar(parcelas.size() == 5, "Geracao de 5 parcelas no PagamentoDAO");

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
        System.out.println("\n-- Teste 7: Excecao ValorPremioInvalidoException --");

        Cliente pf = new PessoaFisica(20, "Lucas", "lucas@email.com", "119999", "Rua G",
                "777.888.999-00", "RG-1122", "05/05/1990", "M", EstadoCivil.SOLTEIRO);

        ApoliceDeVida vidaInvalida = new ApoliceDeVida(4, "AP-INV", pf, 0.0, 0.0,
                LocalDate.now(), LocalDate.now().plusYears(1), 0.0, false, 25);

        ValidadorDePropostaService validador = new ValidadorDePropostaService();
        boolean lancou = false;
        try {
            validador.validarProposta(vidaInvalida);
        } catch (ValorPremioInvalidoException e) {
            lancou = true;
        } catch (ClienteNaoEncontradoException ignored) {
        }
        verificar(lancou, "ValorPremioInvalidoException lancada quando capital segurado e zerado");
    }
}
