package repository;

import model.Veiculo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de Acesso a Dados (DAO) para Veículos.
 * Implementa a interface genérica DAO<Veiculo>.
 */
public class VeiculoDAO implements DAO<Veiculo> {
    private final List<Veiculo> veiculos;
    private final ConexaoBanco conexao;
    private final Path arquivoDados;

    public VeiculoDAO() {
        this.veiculos = new ArrayList<>();
        this.conexao = ConexaoBanco.getInstancia();
        this.arquivoDados = conexao.getCaminhoArquivo("veiculos.txt");
        carregarDados();
    }

    @Override
    public synchronized void salvar(Veiculo veiculo) {
        if (veiculo == null) return;
        if (veiculo.getId() <= 0) {
            veiculo.setId(conexao.getProximoId("veiculo"));
        } else {
            conexao.sincronizarIdMaximo("veiculo", veiculo.getId());
        }
        veiculos.removeIf(v -> v.getId() == veiculo.getId());
        veiculos.add(veiculo);
        salvarDados();
    }

    @Override
    public synchronized Veiculo buscarPorId(int id) {
        for (Veiculo v : veiculos) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public synchronized Optional<Veiculo> buscarPorPlaca(String placa) {
        if (placa == null) return Optional.empty();
        String limpo = placa.replaceAll("[^0-9a-zA-Z]", "");
        for (Veiculo v : veiculos) {
            if (v.getPlaca() != null && v.getPlaca().replaceAll("[^0-9a-zA-Z]", "").equalsIgnoreCase(limpo)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    public synchronized List<Veiculo> listarPorCliente(int clienteId) {
        List<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.getClienteId() == clienteId) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    @Override
    public synchronized List<Veiculo> listarTodos() {
        return new ArrayList<>(veiculos);
    }

    @Override
    public synchronized void atualizar(Veiculo veiculo) {
        if (veiculo == null) return;
        for (int i = 0; i < veiculos.size(); i++) {
            if (veiculos.get(i).getId() == veiculo.getId()) {
                veiculos.set(i, veiculo);
                salvarDados();
                return;
            }
        }
    }

    @Override
    public synchronized boolean deletar(int id) {
        boolean removido = veiculos.removeIf(v -> v.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Veiculo v : veiculos) {
                // Formato: id;clienteId;marca;modelo;ano;placa;renavam;chassi;valorFipe
                bw.write(String.format("%d;%d;%s;%s;%d;%s;%s;%s;%.2f",
                        v.getId(), v.getClienteId(), v.getMarca(), v.getModelo(),
                        v.getAno(), v.getPlaca(), v.getRenavam(), v.getChassi(), v.getValorFipe()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar veículos: " + e.getMessage());
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
                conexao.sincronizarIdMaximo("veiculo", id);

                Veiculo v = new Veiculo(
                        id,
                        Integer.parseInt(p[1]),
                        p[2],
                        p[3],
                        Integer.parseInt(p[4]),
                        p[5],
                        p[6],
                        p[7],
                        Double.parseDouble(p[8].replace(",", "."))
                );
                veiculos.add(v);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar veículos: " + e.getMessage());
        }
    }
}
