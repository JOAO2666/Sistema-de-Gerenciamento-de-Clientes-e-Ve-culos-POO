package repository;

import model.OficinaParceira;
import model.Perito;
import model.Sinistro;
import model.enums.TipoSinistro;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de Acesso a Dados (DAO) para Sinistros.
 * Implementa a interface genérica DAO<Sinistro>.
 */
public class SinistroDAO implements DAO<Sinistro> {
    private final List<Sinistro> sinistros;
    private final ConexaoBanco conexao;
    private final Path arquivoDados;

    public SinistroDAO() {
        this.sinistros = new ArrayList<>();
        this.conexao = ConexaoBanco.getInstancia();
        this.arquivoDados = conexao.getCaminhoArquivo("sinistros.txt");
        carregarDados();
    }

    @Override
    public synchronized void salvar(Sinistro sinistro) {
        if (sinistro == null) return;
        if (sinistro.getId() <= 0) {
            sinistro.setId(conexao.getProximoId("sinistro"));
        } else {
            conexao.sincronizarIdMaximo("sinistro", sinistro.getId());
        }
        if (sinistro.getNumeroProtocolo() == null || sinistro.getNumeroProtocolo().isBlank()) {
            sinistro.setNumeroProtocolo("SIN-" + String.format("%06d", sinistro.getId()));
        }
        sinistros.removeIf(s -> s.getId() == sinistro.getId());
        sinistros.add(sinistro);
        salvarDados();
    }

    @Override
    public synchronized Sinistro buscarPorId(int id) {
        for (Sinistro s : sinistros) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public synchronized Optional<Sinistro> buscarPorProtocolo(String protocolo) {
        if (protocolo == null) return Optional.empty();
        for (Sinistro s : sinistros) {
            if (s.getNumeroProtocolo().equalsIgnoreCase(protocolo.trim())) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public synchronized List<Sinistro> listarPorApolice(int apoliceId) {
        List<Sinistro> resultado = new ArrayList<>();
        for (Sinistro s : sinistros) {
            if (s.getApoliceId() == apoliceId) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    @Override
    public synchronized List<Sinistro> listarTodos() {
        return new ArrayList<>(sinistros);
    }

    @Override
    public synchronized void atualizar(Sinistro sinistro) {
        if (sinistro == null) return;
        for (int i = 0; i < sinistros.size(); i++) {
            if (sinistros.get(i).getId() == sinistro.getId()) {
                sinistros.set(i, sinistro);
                salvarDados();
                return;
            }
        }
    }

    @Override
    public synchronized boolean deletar(int id) {
        boolean removido = sinistros.removeIf(s -> s.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Sinistro s : sinistros) {
                // Formato: id;protocolo;apoliceId;dataOcorrencia;dataRegistro;tipoSinistro;descricao;prejuizo;indenizado;status;laudo
                bw.write(String.format("%d;%s;%d;%s;%s;%s;%s;%.2f;%.2f;%s;%s",
                        s.getId(),
                        s.getNumeroProtocolo(),
                        s.getApoliceId(),
                        s.getDataOcorrencia() != null ? s.getDataOcorrencia().toString() : "",
                        s.getDataRegistro() != null ? s.getDataRegistro().toString() : "",
                        s.getTipoSinistro() != null ? s.getTipoSinistro().name() : "COLISAO",
                        s.getDescricao() != null ? s.getDescricao().replace(";", ",") : "",
                        s.getValorEstimadoPrejuizo(),
                        s.getValorIndenizado(),
                        s.getStatusSinistro(),
                        s.getLaudoPericial() != null ? s.getLaudoPericial().replace(";", ",") : ""));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar sinistros: " + e.getMessage());
        }
    }

    private synchronized void carregarDados() {
        if (!Files.exists(arquivoDados)) return;
        try (BufferedReader br = Files.newBufferedReader(arquivoDados)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(";", -1);
                if (p.length < 11) continue;
                int id = Integer.parseInt(p[0]);
                conexao.sincronizarIdMaximo("sinistro", id);

                LocalDate ocorrencia = !p[3].isEmpty() ? LocalDate.parse(p[3]) : LocalDate.now();
                LocalDate registro = !p[4].isEmpty() ? LocalDate.parse(p[4]) : LocalDate.now();
                TipoSinistro tipo = TipoSinistro.valueOf(p[5]);

                Sinistro sin = new Sinistro(
                        id,
                        p[1],
                        Integer.parseInt(p[2]),
                        ocorrencia,
                        tipo,
                        p[6],
                        Double.parseDouble(p[7].replace(",", "."))
                );
                sin.setDataRegistro(registro);
                sin.setValorIndenizado(Double.parseDouble(p[8].replace(",", ".")));
                sin.setStatusSinistro(p[9]);
                sin.setLaudoPericial(p[10]);

                sinistros.add(sin);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar sinistros: " + e.getMessage());
        }
    }
}
