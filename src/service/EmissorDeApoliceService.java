package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.Apolice;
import model.Pagamento;
import model.enums.StatusApolice;
import repository.ApoliceDAO;
import repository.PagamentoDAO;

import java.time.LocalDate;

/**
 * Serviço que orquestra a emissão formal de apólices de seguro.
 * Valida a proposta, calcula o prêmio tarifário, divide em parcelas de pagamento
 * e ativa a apólice no sistema.
 */
public class EmissorDeApoliceService {
    private final IValidadorDeProposta validador;
    private final ICalculadoraDePremio calculadora;
    private final ApoliceDAO apoliceDAO;
    private final PagamentoDAO pagamentoDAO;

    public EmissorDeApoliceService(ApoliceDAO apoliceDAO, PagamentoDAO pagamentoDAO) {
        this.validador = new ValidadorDePropostaService();
        this.calculadora = new CalculadoraDePremioService();
        this.apoliceDAO = apoliceDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    public EmissorDeApoliceService(IValidadorDeProposta validador,
                                  ICalculadoraDePremio calculadora,
                                  ApoliceDAO apoliceDAO,
                                  PagamentoDAO pagamentoDAO) {
        this.validador = validador;
        this.calculadora = calculadora;
        this.apoliceDAO = apoliceDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    /**
     * Emite uma apólice formal a partir de uma proposta.
     *
     * @param proposta           Objeto da proposta preenchido
     * @param quantidadeParcelas Número de parcelas para pagamento (1 a 12)
     * @param formaPagamento     Forma de pagamento (ex: BOLETO, CARTAO, PIX)
     * @return Apólice ativada e persistida
     */
    public Apolice emitirApolice(Apolice proposta, int quantidadeParcelas, String formaPagamento)
            throws ClienteNaoEncontradoException, ValorPremioInvalidoException {

        // 1. Validação técnica e de negócio
        validador.validarProposta(proposta);

        // 2. Cálculo do prêmio total com coberturas e tributos
        double premioFinal = calculadora.calcularPremio(proposta);

        // 3. Ativação da apólice
        proposta.setStatus(StatusApolice.ATIVA);

        // 4. Salvar apólice no DAO para gerar o ID definitivo
        apoliceDAO.salvar(proposta);

        // 5. Geração e persistência das parcelas financeiras
        int parcelas = Math.max(1, Math.min(12, quantidadeParcelas));
        double valorParcela = Math.round((premioFinal / parcelas) * 100.0) / 100.0;

        for (int i = 1; i <= parcelas; i++) {
            LocalDate vencimento = LocalDate.now().plusMonths(i - 1);
            Pagamento pag = new Pagamento(0, proposta.getId(), i, parcelas, valorParcela, vencimento, formaPagamento);
            pagamentoDAO.salvar(pag);
            proposta.adicionarPagamento(pag);
        }

        return proposta;
    }
}
