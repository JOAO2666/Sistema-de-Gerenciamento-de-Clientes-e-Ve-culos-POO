package repository;

import model.*;
import model.enums.StatusApolice;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
            if (a.getNumeroApolice() != null && a.getNumeroApolice().equalsIgnoreCase(numero.trim())) {
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

    private String limparCampo(String valor) {
        if (valor == null) return "";
        return valor.replace(";", ",").replace("\r", " ").replace("\n", " ").trim();
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Apolice a : apolices) {
                int clienteId = a.getCliente() != null ? a.getCliente().getId() : 0;
                String numero = limparCampo(a.getNumeroApolice());
                String statusStr = a.getStatus() != null ? a.getStatus().name() : "PROPOSTA";
                String inicioStr = a.getDataInicioVigencia() != null ? a.getDataInicioVigencia().toString() : "";
                String fimStr = a.getDataFimVigencia() != null ? a.getDataFimVigencia().toString() : "";

                if (a instanceof ApoliceAuto) {
                    ApoliceAuto auto = (ApoliceAuto) a;
                    int veiculoId = auto.getVeiculo() != null ? auto.getVeiculo().getId() : 0;
                    String perfil = auto.getPerfilPrincipalCondutor() != null && !auto.getPerfilPrincipalCondutor().isBlank()
                            ? limparCampo(auto.getPerfilPrincipalCondutor()) : "Padrao";
                    bw.write(String.format(Locale.US, "AUTO;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%d;%d;%s",
                            auto.getId(), numero, clienteId,
                            auto.getValorPremio(), auto.getValorFranquia(),
                            inicioStr, fimStr, statusStr,
                            veiculoId, auto.getClasseBonus(), perfil));
                } else if (a instanceof ApoliceResidencial) {
                    ApoliceResidencial res = (ApoliceResidencial) a;
                    Imovel im = res.getImovel();
                    int imovelId = im != null ? im.getId() : 0;
                    String cep = im != null && im.getCep() != null ? limparCampo(im.getCep()) : "00000-000";
                    String end = im != null && im.getEndereco() != null ? limparCampo(im.getEndereco()) : "Endereço Cadastrado";
                    String cid = im != null && im.getCidade() != null ? limparCampo(im.getCidade()) : "Cidade";
                    String uf = im != null && im.getEstado() != null ? limparCampo(im.getEstado()) : "UF";
                    double valMerc = im != null ? im.getValorMercado() : 300000.0;
                    double area = im != null ? im.getAreaM2() : 100.0;
                    String tipoRes = res.getTipoResidencia() != null ? limparCampo(res.getTipoResidencia()) : "Casa";
                    bw.write(String.format(Locale.US, "RESIDENCIAL;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%d;%s;%b;%s;%s;%s;%s;%.2f;%.2f",
                            res.getId(), numero, clienteId,
                            res.getValorPremio(), res.getValorFranquia(),
                            inicioStr, fimStr, statusStr,
                            imovelId, tipoRes, res.isPossuiAlarme(),
                            cep, end, cid, uf, valMerc, area));
                } else if (a instanceof ApoliceDeVida) {
                    ApoliceDeVida vida = (ApoliceDeVida) a;
                    String ben = limparCampo(String.join(",", vida.getBeneficiarios()));
                    bw.write(String.format(Locale.US, "VIDA;%d;%s;%d;%.2f;%.2f;%s;%s;%s;%.2f;%b;%d;%s",
                            vida.getId(), numero, clienteId,
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
                try {
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
                        String cep = p.length >= 18 ? p[12] : "00000-000";
                        String end = p.length >= 18 ? p[13] : "Endereço Cadastrado";
                        String cid = p.length >= 18 ? p[14] : "Cidade";
                        String uf = p.length >= 18 ? p[15] : "UF";
                        double valMerc = p.length >= 18 ? Double.parseDouble(p[16].replace(",", ".")) : 300000.0;
                        double area = p.length >= 18 ? Double.parseDouble(p[17].replace(",", ".")) : 100.0;

                        Imovel imovel = new Imovel(imovelId, clienteId, cep, end, cid, uf, valMerc, area, "Alvenaria", alarme);
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
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar apólices: " + e.getMessage());
        }
    }
}
