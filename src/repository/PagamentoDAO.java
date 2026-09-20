package repository;

import model.Pagamento;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de Acesso a Dados (DAO) para Pagamentos e Parcelas de Apólice.
 * Implementa a interface genérica DAO<Pagamento>.
 */
public class PagamentoDAO implements DAO<Pagamento> {
    private final List<Pagamento> pagamentos;
    private final ConexaoBanco conexao;
    private final Path arquivoDados;

    public PagamentoDAO() {
        this.pagamentos = new ArrayList<>();
        this.conexao = ConexaoBanco.getInstancia();
        this.arquivoDados = conexao.getCaminhoArquivo("pagamentos.txt");
        carregarDados();
    }

    @Override
    public synchronized void salvar(Pagamento pagamento) {
        if (pagamento == null) return;
        if (pagamento.getId() <= 0) {
            pagamento.setId(conexao.getProximoId("pagamento"));
        } else {
            conexao.sincronizarIdMaximo("pagamento", pagamento.getId());
        }
        pagamentos.removeIf(p -> p.getId() == pagamento.getId());
        pagamentos.add(pagamento);
        salvarDados();
    }

    @Override
    public synchronized Pagamento buscarPorId(int id) {
        for (Pagamento p : pagamentos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public synchronized List<Pagamento> listarPorApolice(int apoliceId) {
        List<Pagamento> resultado = new ArrayList<>();
        for (Pagamento p : pagamentos) {
            if (p.getApoliceId() == apoliceId) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public synchronized List<Pagamento> listarTodos() {
        return new ArrayList<>(pagamentos);
    }

    @Override
    public synchronized void atualizar(Pagamento pagamento) {
        if (pagamento == null) return;
        for (int i = 0; i < pagamentos.size(); i++) {
            if (pagamentos.get(i).getId() == pagamento.getId()) {
                pagamentos.set(i, pagamento);
                salvarDados();
                return;
            }
        }
    }

    @Override
    public synchronized boolean deletar(int id) {
        boolean removido = pagamentos.removeIf(p -> p.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Pagamento p : pagamentos) {
                // Formato: id;apoliceId;numeroParcela;totalParcelas;valor;dataVencimento;dataPagamento;pago;formaPagamento
                bw.write(String.format("%d;%d;%d;%d;%.2f;%s;%s;%b;%s",
                        p.getId(),
                        p.getApoliceId(),
                        p.getNumeroParcela(),
                        p.getTotalParcelas(),
                        p.getValor(),
                        p.getDataVencimento() != null ? p.getDataVencimento().toString() : "",
                        p.getDataPagamento() != null ? p.getDataPagamento().toString() : "",
                        p.isPago(),
                        p.getFormaPagamento() != null ? p.getFormaPagamento() : ""));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar pagamentos: " + e.getMessage());
        }
    }

    private synchronized void carregarDados() {
        if (!Files.exists(arquivoDados)) return;
        try (BufferedReader br = Files.newBufferedReader(arquivoDados)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(";", -1);
                if (p.length < 9) continue;
                int id = Integer.parseInt(p[0]);
                conexao.sincronizarIdMaximo("pagamento", id);

                LocalDate venc = !p[5].isEmpty() ? LocalDate.parse(p[5]) : null;
                LocalDate pagto = !p[6].isEmpty() ? LocalDate.parse(p[6]) : null;

                Pagamento pag = new Pagamento(
                        id,
                        Integer.parseInt(p[1]),
                        Integer.parseInt(p[2]),
                        Integer.parseInt(p[3]),
                        Double.parseDouble(p[4].replace(",", ".")),
                        venc,
                        p[8]
                );
                pag.setDataPagamento(pagto);
                pag.setPago(Boolean.parseBoolean(p[7]));
                pagamentos.add(pag);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar pagamentos: " + e.getMessage());
        }
    }
}
