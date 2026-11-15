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

        if (apolice.getStatus() != StatusApolice.ATIVA) {
            throw new IllegalStateException("A apólice não está ativa (Status atual: " +
                    apolice.getStatus().getDescricao() + ").");
        }

        if (dataOcorrencia == null) {
            throw new IllegalArgumentException("A data da ocorrência é obrigatória.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do sinistro é obrigatório.");
        }

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do sinistro é obrigatória.");
        }

        if (!Double.isFinite(valorEstimadoPrejuizo) || valorEstimadoPrejuizo <= 0) {
            throw new IllegalArgumentException("O valor estimado do prejuízo deve ser maior que zero.");
        }

        List<Sinistro> existentes = sinistroDAO.listarPorApolice(apoliceId);
        for (Sinistro s : existentes) {
            if (dataOcorrencia.equals(s.getDataOcorrencia()) && s.getTipoSinistro() == tipo) {
                throw new SinistroJaRegistradoException("Já existe um sinistro registrado com o protocolo " +
                        s.getNumeroProtocolo() + " para esta mesma apólice, data e tipo de evento.");
            }
        }

        Sinistro novoSinistro = new Sinistro(0, "", apoliceId, dataOcorrencia, tipo, descricao.trim(), valorEstimadoPrejuizo);
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
        if ("PAGO_CONCLUIDO".equals(s.getStatusSinistro())) {
            throw new IllegalStateException("Não é possível alterar o laudo de um sinistro já liquidado.");
        }
        if (laudo == null || laudo.isBlank()) {
            throw new IllegalArgumentException("O parecer/laudo pericial é obrigatório.");
        }
        if (!Double.isFinite(valorAprovado) || valorAprovado < 0) {
            throw new IllegalArgumentException("O valor aprovado para reparo não pode ser negativo.");
        }

        s.setPerito(perito);
        s.setOficinaParceira(oficina);
        s.setLaudoPericial(laudo.trim());

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
        if (!"APROVADO".equals(s.getStatusSinistro())) {
            throw new IllegalStateException("O sinistro precisa ter laudo aprovado antes da liquidação (status atual: "
                    + s.getStatusSinistro() + ").");
        }
        s.setStatusSinistro("PAGO_CONCLUIDO");
        sinistroDAO.atualizar(s);
    }
}
