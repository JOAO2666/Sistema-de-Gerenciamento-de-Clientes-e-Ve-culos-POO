import controller.ControladorApolice;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Suite de Testes Automatizados para validação de todas as camadas do sistema:
 * - Classes Abstratas e Herança (Cliente, Apolice)
 * - Interfaces (Identificavel, Seguravel, DAO, ICalculadoraDePremio)
 * - Polimorfismo (Cálculo de prêmio de Auto, Residencial e Vida)
 * - Exceções de Regra de Negócio
 */
public class TestePOO {
    private static int testesPassados = 0;
    private static int testesTotais = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Executando Testes Automatizados do Sistema POO  ");
        System.out.println("=================================================");

        testarHierarquiaClientesEPolimorfismo();
        testarHierarquiaApoliceECalculoDePremio();
        testarEmissaoDeApoliceEParcelas();
        testarExcecaoClienteNaoEncontrado();
        testarExcecaoApoliceVencida();
        testarExcecaoSinistroDuplicado();

        System.out.println("=================================================");
        System.out.printf(" Resumo dos Testes: %d / %d passaram com sucesso!\n", testesPassados, testesTotais);
        System.out.println("=================================================");

        if (testesPassados == testesTotais) {
            System.out.println(">>> TODOS OS TESTES PASSARAM COM SUCESSO! <<<");
        } else {
            System.err.println(">>> ALGUNS TESTES FALHARAM! <<<");
            System.exit(1);
        }
    }

    private static void afirme(boolean condicao, String nomeTeste) {
        testesTotais++;
        if (condicao) {
            testesPassados++;
            System.out.println("  [PASSOU] " + nomeTeste);
        } else {
            System.err.println("  [FALHOU] " + nomeTeste);
        }
    }

    private static void testarHierarquiaClientesEPolimorfismo() {
        System.out.println("\n1. Testando Herança e Classes Abstratas de Clientes:");

        Cliente pf = new PessoaFisica(1, "Ana Souza", "ana@email.com", "1199999999", "Rua A",
                "111.222.333-44", "MG-112233", "20/10/1995", "F", EstadoCivil.SOLTEIRO);

        Cliente pj = new PessoaJuridica(2, "Tech Solutions", "contato@tech.com", "1133334444", "Av B",
                "11.222.333/0001-44", "Tech Solutions LTDA", "123456", "Tech Sol", "Marcos");

        afirme(pf instanceof Cliente && pf instanceof Identificavel, "PessoaFisica é Cliente e Identificavel");
        afirme(pj instanceof Cliente && pj instanceof Identificavel, "PessoaJuridica é Cliente e Identificavel");
        afirme("111.222.333-44".equals(pf.getDocumentoPrincipal()), "Documento principal PF é CPF");
        afirme("11.222.333/0001-44".equals(pj.getDocumentoPrincipal()), "Documento principal PJ é CNPJ");
    }

    private static void testarHierarquiaApoliceECalculoDePremio() {
        System.out.println("\n2. Testando Classes Abstratas de Apólices e Polimorfismo de Prêmios:");

        Cliente pf = new PessoaFisica(10, "Carlos", "carlos@email.com", "1198888", "Rua C",
                "222.333.444-55", "RG-9988", "10/02/1988", "M", EstadoCivil.CASADO);

        Veiculo v = new Veiculo(1, 10, "Honda", "Civic", 2021, "ABC-1234", "12345", "CH123", 100000.0);
        afirme(v instanceof Seguravel, "Veiculo implementa Seguravel");

        ApoliceAuto auto = new ApoliceAuto(1, "AP-AUTO-01", pf, 0.0, 3000.0,
                LocalDate.now(), LocalDate.now().plusYears(1), v, 2, "Trabalho");

        ApoliceResidencial res = new ApoliceResidencial(2, "AP-RES-01", pf, 0.0, 1500.0,
                LocalDate.now(), LocalDate.now().plusYears(1),
                new Imovel(1, 10, "01001-000", "Praça da Sé", "São Paulo", "SP", 500000.0, 90.0, "Alvenaria", true),
                "Apartamento", true);

        ApoliceDeVida vida = new ApoliceDeVida(3, "AP-VIDA-01", pf, 0.0, 0.0,
                LocalDate.now(), LocalDate.now().plusYears(1), 200000.0, false, 30);

        afirme(auto instanceof Apolice && res instanceof Apolice && vida instanceof Apolice,
                "Auto, Residencial e Vida herdam de Apolice");

        CalculadoraDePremioService calc = new CalculadoraDePremioService();
        try {
            double premioAuto = calc.calcularPremio(auto);
            double premioRes = calc.calcularPremio(res);
            double premioVida = calc.calcularPremio(vida);

            afirme(premioAuto > 0, "Cálculo polimórfico de prêmio Auto > 0 (R$ " + premioAuto + ")");
            afirme(premioRes > 0, "Cálculo polimórfico de prêmio Residencial > 0 (R$ " + premioRes + ")");
            afirme(premioVida > 0, "Cálculo polimórfico de prêmio Vida > 0 (R$ " + premioVida + ")");
        } catch (ValorPremioInvalidoException e) {
            afirme(false, "Erro inesperado de prêmio: " + e.getMessage());
        }
    }

    private static void testarEmissaoDeApoliceEParcelas() {
        System.out.println("\n3. Testando Emissão de Apólice e Geração de Parcelas (DAO & Service):");

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
            afirme(emitida.getStatus() == StatusApolice.ATIVA, "Status da apólice emitida está ATIVA");
            afirme(emitida.getId() > 0, "Apólice possui ID gerado pelo banco");

            List<Pagamento> parcelas = pagDao.listarPorApolice(emitida.getId());
            afirme(parcelas.size() == 5, "Foram geradas exatamente 5 parcelas financeiras");
        } catch (Exception e) {
            afirme(false, "Falha na emissão de apólice: " + e.getMessage());
        }
    }

    private static void testarExcecaoClienteNaoEncontrado() {
        System.out.println("\n4. Testando Lançamento de ClienteNaoEncontradoException:");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ControladorCliente ctrl = new ControladorCliente(cliDao, veiDao);

        boolean lancou = false;
        try {
            ctrl.buscarPorId(999999);
        } catch (ClienteNaoEncontradoException e) {
            lancou = true;
        }
        afirme(lancou, "ClienteNaoEncontradoException capturada corretamente");
    }

    private static void testarExcecaoApoliceVencida() {
        System.out.println("\n5. Testando Lançamento de ApoliceVencidaException:");

        ClienteDAO cliDao = new ClienteDAO();
        VeiculoDAO veiDao = new VeiculoDAO();
        ApoliceDAO apDao = new ApoliceDAO(cliDao, veiDao);
        SinistroDAO sinDao = new SinistroDAO();
        ProcessadorDeSinistroService proc = new ProcessadorDeSinistroService(apDao, sinDao);

        PessoaFisica pf = new PessoaFisica(0, "Segurado Teste", "teste@email.com", "1198888", "Rua E",
                "444.555.666-77", "RG-5544", "01/01/1980", "M", EstadoCivil.CASADO);
        cliDao.salvar(pf);

        // Apólice vencida no ano passado
        ApoliceAuto apVencida = new ApoliceAuto(0, "AP-VENCIDA", pf, 1500.0, 2000.0,
                LocalDate.now().minusYears(2), LocalDate.now().minusDays(1), null, 0, "Padrao");
        apVencida.setStatus(StatusApolice.EXPIRADA);
        apDao.salvar(apVencida);

        boolean lancou = false;
        try {
            proc.registrarSinistro(apVencida.getId(), LocalDate.now(), TipoSinistro.COLISAO, "Batida leve", 3000.0);
        } catch (ApoliceVencidaException e) {
            lancou = true;
        } catch (Exception ignored) {}

        afirme(lancou, "ApoliceVencidaException lançada ao tentar registrar sinistro em apólice expirada");
    }

    private static void testarExcecaoSinistroDuplicado() {
        System.out.println("\n6. Testando Lançamento de SinistroJaRegistradoException:");

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
        try {
            proc.registrarSinistro(apAtiva.getId(), hoje, TipoSinistro.COLISAO, "Primeira colisão", 2500.0);
            // Segunda tentativa com a mesma data e tipo para a mesma apólice
            proc.registrarSinistro(apAtiva.getId(), hoje, TipoSinistro.COLISAO, "Tentativa duplicada", 2500.0);
        } catch (SinistroJaRegistradoException e) {
            lancou = true;
        } catch (Exception ignored) {}

        afirme(lancou, "SinistroJaRegistradoException lançada ao detectar duplicidade no mesmo evento");
    }
}
