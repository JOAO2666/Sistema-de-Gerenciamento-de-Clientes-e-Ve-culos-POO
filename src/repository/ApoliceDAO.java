package repository;

import model.*;
import model.enums.StatusApolice;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de Acesso a Dados (DAO) para Apólices de Seguros.
 * Gerencia apólices de Auto, Residencial e Vida de forma polimórfica.
 * Implementa a interface genérica DAO<Apolice>.
 */
public class ApoliceDAO implements DAO<Apolice> {
    private final List<Apolice> apolices;
    private final ConexaoBanco conexao;
    private final Path arquivoDados;
    private final ClienteDAO clienteDAO;
    private final VeiculoDAO veiculoDAO;

    public ApoliceDAO(ClienteDAO clienteDAO, VeiculoDAO veiculoDAO) {
        this.apolices = new ArrayList<>();
        this.conexao = ConexaoBanco.getInstancia();
        this.arquivoDados = conexao.getCaminhoArquivo("apolices.txt");
        this.clienteDAO = clienteDAO;
        this.veiculoDAO = veiculoDAO;
        carregarDados();
    }

    @Override
    public synchronized void salvar(Apolice apolice) {
        if (apolice == null) return;
        if (apolice.getId() <= 0) {
            apolice.setId(conexao.getProximoId("apolice"));
        } else {
            conexao.sincronizarIdMaximo("apolice", apolice.getId());
        }
        if (apolice.getNumeroApolice() == null || apolice.getNumeroApolice().isBlank()) {
            apolice.setNumeroApolice("AP-" + String.format("%06d", apolice.getId()));
        }
        apolices.removeIf(a -> a.getId() == apolice.getId());
        apolices.add(apolice);
        salvarDados();
    }

    @Override
    public synchronized Apolice buscarPorId(int id) {
        for (Apolice a : apolices) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public synchronized Optional<Apolice> buscarPorNumero(String numero) {
        if (numero == null) return Optional.empty();
        for (Apolice a : apolices) {
            if (a.getNumeroApolice().equalsIgnoreCase(numero.trim())) {
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    public synchronized List<Apolice> listarPorCliente(int clienteId) {
        List<Apolice> resultado = new ArrayList<>();
        for (Apolice a : apolices) {
            if (a.getCliente() != null && a.getCliente().getId() == clienteId) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public synchronized List<Apolice> listarTodos() {
        return new ArrayList<>(apolices);
    }

    @Override
    public synchronized void atualizar(Apolice apolice) {
        if (apolice == null) return;
        for (int i = 0; i < apolices.size(); i++) {
            if (apolices.get(i).getId() == apolice.getId()) {
                apolices.set(i, apolice);
                salvarDados();
                return;
            }
        }
    }

    @Override
    public synchronized boolean deletar(int id) {
        boolean removido = apolices.removeIf(a -> a.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Apolice a : apolices) {
                int clienteId = a.getCliente() != null ? a.getCliente().getId() : 0;
                String statusStr = a.getStatus() != null ? a.getStatus().name() : "PROPOSTA";
                String inicioStr = a.getDataInicioVigencia() != null ? a.getDataInicioVigencia().toString() : "";
                String fimStr = a.getDataFimVigencia() != null ? a.getDataFimVigencia().toString() : "";

                if (a instanceof ApoliceAuto) {
                    ApoliceAuto auto = (ApoliceAuto) a;
                    int veiculoId = auto.getVeiculo() != null ? auto.getVeiculo().getId() : 0;
                    // AUTO;id;numero;clienteId;premio;franquia;inicio;fim;status;veiculoId;classeBonus;perfil
                    bw.write(String.format("AUTO;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%d;%d;%s",
                            auto.getId(), auto.getNumeroApolice(), clienteId,
                            auto.getValorPremio(), auto.getValorFranquia(),
                            inicioStr, fimStr, statusStr,
                            veiculoId, auto.getClasseBonus(),
                            auto.getPerfilPrincipalCondutor() != null ? auto.getPerfilPrincipalCondutor() : "Padrao"));
                } else if (a instanceof ApoliceResidencial) {
                    ApoliceResidencial res = (ApoliceResidencial) a;
                    int imovelId = res.getImovel() != null ? res.getImovel().getId() : 0;
                    // RESIDENCIAL;id;numero;clienteId;premio;franquia;inicio;fim;status;imovelId;tipoResidencia;possuiAlarme
                    bw.write(String.format("RESIDENCIAL;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%d;%s;%b",
                            res.getId(), res.getNumeroApolice(), clienteId,
                            res.getValorPremio(), res.getValorFranquia(),
                            inicioStr, fimStr, statusStr,
                            imovelId,
                            res.getTipoResidencia() != null ? res.getTipoResidencia() : "Casa",
                            res.isPossuiAlarme()));
                } else if (a instanceof ApoliceDeVida) {
                    ApoliceDeVida vida = (ApoliceDeVida) a;
                    // VIDA;id;numero;clienteId;premio;franquia;inicio;fim;status;capitalSegurado;fumante;idade;beneficiarios
                    String ben = String.join(",", vida.getBeneficiarios());
                    bw.write(String.format("VIDA;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%.2f;%b;%d;%s",
                            vida.getId(), vida.getNumeroApolice(), clienteId,
                            vida.getValorPremio(), vida.getValorFranquia(),
                            inicioStr, fimStr, statusStr,
                            vida.getCapitalSegurado(), vida.isFumante(),
                            vida.getIdadeSegurado(), ben));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar apólices: " + e.getMessage());
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

                String tipo = p[0];
                int id = Integer.parseInt(p[1]);
                conexao.sincronizarIdMaximo("apolice", id);
                String num = p[2];
                int clienteId = Integer.parseInt(p[3]);
                Cliente cliente = clienteDAO.buscarPorId(clienteId);
                double premio = Double.parseDouble(p[4].replace(",", "."));
                double franquia = Double.parseDouble(p[5].replace(",", "."));
                LocalDate inicio = !p[6].isEmpty() ? LocalDate.parse(p[6]) : LocalDate.now();
                LocalDate fim = !p[7].isEmpty() ? LocalDate.parse(p[7]) : LocalDate.now().plusYears(1);
                StatusApolice status = StatusApolice.valueOf(p[8]);

                if ("AUTO".equals(tipo) && p.length >= 12) {
                    int veiculoId = Integer.parseInt(p[9]);
                    Veiculo veiculo = veiculoDAO.buscarPorId(veiculoId);
                    int bonus = Integer.parseInt(p[10]);
                    String perfil = p[11];

                    ApoliceAuto auto = new ApoliceAuto(id, num, cliente, premio, franquia, inicio, fim, veiculo, bonus, perfil);
                    auto.setStatus(status);
                    apolices.add(auto);
                } else if ("RESIDENCIAL".equals(tipo) && p.length >= 12) {
                    int imovelId = Integer.parseInt(p[9]);
                    String tipoRes = p[10];
                    boolean alarme = Boolean.parseBoolean(p[11]);

                    Imovel imovel = new Imovel(imovelId, clienteId, "00000-000", "Endereço Cadastrado", "Cidade", "UF", 300000.0, 100.0, "Alvenaria", alarme);
                    ApoliceResidencial res = new ApoliceResidencial(id, num, cliente, premio, franquia, inicio, fim, imovel, tipoRes, alarme);
                    res.setStatus(status);
                    apolices.add(res);
                } else if ("VIDA".equals(tipo) && p.length >= 13) {
                    double capital = Double.parseDouble(p[9].replace(",", "."));
                    boolean fumante = Boolean.parseBoolean(p[10]);
                    int idade = Integer.parseInt(p[11]);
                    ApoliceDeVida vida = new ApoliceDeVida(id, num, cliente, premio, franquia, inicio, fim, capital, fumante, idade);
                    vida.setStatus(status);
                    if (!p[12].isBlank()) {
                        for (String b : p[12].split(",")) {
                            vida.adicionarBeneficiario(b);
                        }
                    }
                    apolices.add(vida);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar apólices: " + e.getMessage());
        }
    }
}
