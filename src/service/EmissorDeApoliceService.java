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

        if (quantidadeParcelas < 1 || quantidadeParcelas > 12) {
            throw new IllegalArgumentException("Quantidade de parcelas deve ser entre 1 e 12.");
        }

        validador.validarProposta(proposta);

        double premioFinal = calculadora.calcularPremio(proposta);

        proposta.setStatus(StatusApolice.ATIVA);

        apoliceDAO.salvar(proposta);

        String formaFinal = (formaPagamento == null || formaPagamento.isBlank()) ? "PIX" : formaPagamento.trim().toUpperCase();
        double valorParcela = Math.round((premioFinal / quantidadeParcelas) * 100.0) / 100.0;
        double somaAcumulada = 0.0;

        for (int i = 1; i <= quantidadeParcelas; i++) {
            double valorAtual = (i == quantidadeParcelas)
                    ? Math.round((premioFinal - somaAcumulada) * 100.0) / 100.0
                    : valorParcela;
            somaAcumulada += valorAtual;
            LocalDate vencimento = LocalDate.now().plusMonths(i - 1);
            Pagamento pag = new Pagamento(0, proposta.getId(), i, quantidadeParcelas, valorAtual, vencimento, formaFinal);
            pagamentoDAO.salvar(pag);
            proposta.adicionarPagamento(pag);
        }

        return proposta;
    }
}
