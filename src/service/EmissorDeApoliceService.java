package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.Apolice;
import model.Pagamento;
import model.enums.StatusApolice;
import repository.ApoliceDAO;
import repository.PagamentoDAO;

import java.time.LocalDate;

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

    public Apolice emitirApolice(Apolice proposta)
            throws ClienteNaoEncontradoException, ValorPremioInvalidoException {
        return emitirApolice(proposta, 1, "PIX");
    }

    public Apolice emitirApolice(Apolice proposta, int quantidadeParcelas, String formaPagamento)
            throws ClienteNaoEncontradoException, ValorPremioInvalidoException {

        validador.validarProposta(proposta);

        double premioFinal = calculadora.calcularPremio(proposta);

        proposta.setStatus(StatusApolice.ATIVA);

        apoliceDAO.salvar(proposta);

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
