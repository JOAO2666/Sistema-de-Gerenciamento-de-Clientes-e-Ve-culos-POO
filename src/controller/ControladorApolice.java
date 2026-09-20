package controller;

import exception.ApoliceVencidaException;
import exception.ClienteNaoEncontradoException;
import exception.SinistroJaRegistradoException;
import exception.ValorPremioInvalidoException;
import model.*;
import model.enums.TipoSinistro;
import repository.ApoliceDAO;
import repository.PagamentoDAO;
import repository.SinistroDAO;
import service.EmissorDeApoliceService;
import service.ProcessadorDeSinistroService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável pela gestão de Apólices, Sinistros e Pagamentos.
 */
public class ControladorApolice {
    private final ApoliceDAO apoliceDAO;
    private final PagamentoDAO pagamentoDAO;
    private final SinistroDAO sinistroDAO;
    private final EmissorDeApoliceService emissorService;
    private final ProcessadorDeSinistroService sinistroService;

    public ControladorApolice(ApoliceDAO apoliceDAO, PagamentoDAO pagamentoDAO,
                              SinistroDAO sinistroDAO, EmissorDeApoliceService emissorService,
                              ProcessadorDeSinistroService sinistroService) {
        this.apoliceDAO = apoliceDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.sinistroDAO = sinistroDAO;
        this.emissorService = emissorService;
        this.sinistroService = sinistroService;
    }

    public Apolice emitirApolice(Apolice proposta, int parcelas, String formaPagamento)
            throws ClienteNaoEncontradoException, ValorPremioInvalidoException {
        return emissorService.emitirApolice(proposta, parcelas, formaPagamento);
    }

    public List<Apolice> listarTodasApolices() {
        return apoliceDAO.listarTodos();
    }

    public Apolice buscarApolicePorId(int id) {
        return apoliceDAO.buscarPorId(id);
    }

    public Optional<Apolice> buscarApolicePorNumero(String numero) {
        return apoliceDAO.buscarPorNumero(numero);
    }

    public List<Apolice> listarApolicesDoCliente(int clienteId) {
        return apoliceDAO.listarPorCliente(clienteId);
    }

    public Sinistro registrarSinistro(int apoliceId, LocalDate data, TipoSinistro tipo,
                                      String descricao, double prejuizo)
            throws ApoliceVencidaException, SinistroJaRegistradoException {
        return sinistroService.registrarSinistro(apoliceId, data, tipo, descricao, prejuizo);
    }

    public List<Sinistro> listarSinistros() {
        return sinistroDAO.listarTodos();
    }

    public List<Sinistro> listarSinistrosPorApolice(int apoliceId) {
        return sinistroDAO.listarPorApolice(apoliceId);
    }

    public void laudarSinistro(int sinistroId, Perito perito, OficinaParceira oficina, String laudo, double valor) {
        sinistroService.analisarELaudarSinistro(sinistroId, perito, oficina, laudo, valor);
    }

    public List<Pagamento> listarPagamentosPorApolice(int apoliceId) {
        return pagamentoDAO.listarPorApolice(apoliceId);
    }

    public List<Pagamento> listarTodosPagamentos() {
        return pagamentoDAO.listarTodos();
    }

    public boolean quitarPagamento(int pagamentoId, String forma) {
        Pagamento p = pagamentoDAO.buscarPorId(pagamentoId);
        if (p != null && !p.isPago()) {
            p.registrarQuitacao(forma);
            pagamentoDAO.atualizar(p);
            return true;
        }
        return false;
    }
}
