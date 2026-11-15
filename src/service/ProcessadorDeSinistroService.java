package service;

import exception.ApoliceVencidaException;
import exception.SinistroJaRegistradoException;
import model.Apolice;
import model.OficinaParceira;
import model.Perito;
import model.Sinistro;
import model.enums.StatusApolice;
import model.enums.TipoSinistro;
import repository.ApoliceDAO;
import repository.SinistroDAO;

import java.time.LocalDate;
import java.util.List;

public class ProcessadorDeSinistroService {
    private final ApoliceDAO apoliceDAO;
    private final SinistroDAO sinistroDAO;

    public ProcessadorDeSinistroService(ApoliceDAO apoliceDAO, SinistroDAO sinistroDAO) {
        this.apoliceDAO = apoliceDAO;
        this.sinistroDAO = sinistroDAO;
    }

    public Sinistro registrarSinistro(int apoliceId, TipoSinistro tipo,
                                      String descricao, double valorEstimadoPrejuizo)
            throws ApoliceVencidaException, SinistroJaRegistradoException {
        return registrarSinistro(apoliceId, LocalDate.now(), tipo, descricao, valorEstimadoPrejuizo);
    }

    public Sinistro registrarSinistro(int apoliceId, LocalDate dataOcorrencia, TipoSinistro tipo,
                                      String descricao, double valorEstimadoPrejuizo)
            throws ApoliceVencidaException, SinistroJaRegistradoException {

        Apolice apolice = apoliceDAO.buscarPorId(apoliceId);
        if (apolice == null) {
            throw new IllegalArgumentException("Apólice não encontrada com o ID: " + apoliceId);
        }

        if (apolice.getStatus() == StatusApolice.EXPIRADA || apolice.isVencida()) {
            throw new ApoliceVencidaException("A apólice " + apolice.getNumeroApolice() +
                    " está expirada / vencida desde " + apolice.getDataFimVigencia() + ".");
        }

        if (apolice.getStatus() == StatusApolice.CANCELADA || apolice.getStatus() == StatusApolice.SUSPENSA) {
            throw new IllegalStateException("A apólice não está ativa (Status atual: " +
                    apolice.getStatus().getDescricao() + ").");
        }

        List<Sinistro> existentes = sinistroDAO.listarPorApolice(apoliceId);
        for (Sinistro s : existentes) {
            if (s.getDataOcorrencia().equals(dataOcorrencia) && s.getTipoSinistro() == tipo) {
                throw new SinistroJaRegistradoException("Já existe um sinistro registrado com o protocolo " +
                        s.getNumeroProtocolo() + " para esta mesma apólice, data e tipo de evento.");
            }
        }

        String protocolo = "SIN-" + System.currentTimeMillis() % 1000000;
        Sinistro novoSinistro = new Sinistro(0, protocolo, apoliceId, dataOcorrencia, tipo, descricao, valorEstimadoPrejuizo);
        sinistroDAO.salvar(novoSinistro);
        apolice.adicionarSinistro(novoSinistro);

        return novoSinistro;
    }

    public void analisarELaudarSinistro(int sinistroId, Perito perito, OficinaParceira oficina,
                                       String laudo, double valorAprovado) {
        Sinistro s = sinistroDAO.buscarPorId(sinistroId);
        if (s == null) {
            throw new IllegalArgumentException("Sinistro não encontrado com o ID: " + sinistroId);
        }

        s.setPerito(perito);
        s.setOficinaParceira(oficina);
        s.setLaudoPericial(laudo);

        Apolice apolice = apoliceDAO.buscarPorId(s.getApoliceId());
        double franquia = (apolice != null) ? apolice.getValorFranquia() : 0.0;

        double valorIndenizavel = Math.max(0.0, valorAprovado - franquia);
        s.setValorIndenizado(valorIndenizavel);
        s.setStatusSinistro("APROVADO");

        sinistroDAO.atualizar(s);
    }

    public void liquidarSinistro(int sinistroId) {
        Sinistro s = sinistroDAO.buscarPorId(sinistroId);
        if (s == null) {
            throw new IllegalArgumentException("Sinistro não encontrado.");
        }
        s.setStatusSinistro("PAGO_CONCLUIDO");
        sinistroDAO.atualizar(s);
    }
}
