package repository;

import model.Cliente;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.enums.EstadoCivil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Camada de Acesso a Dados (DAO) para Clientes (Pessoa Física e Pessoa Jurídica).
 * Implementa a interface genérica DAO<Cliente>.
 */
public class ClienteDAO implements DAO<Cliente> {
    private final List<Cliente> clientes;
    private final ConexaoBanco conexao;
    private final Path arquivoDados;

    public ClienteDAO() {
        this.clientes = new ArrayList<>();
        this.conexao = ConexaoBanco.getInstancia();
        this.arquivoDados = conexao.getCaminhoArquivo("clientes.txt");
        carregarDados();
    }

    @Override
    public synchronized void salvar(Cliente cliente) {
        if (cliente == null) return;
        if (cliente.getId() <= 0) {
            cliente.setId(conexao.getProximoId("cliente"));
        } else {
            conexao.sincronizarIdMaximo("cliente", cliente.getId());
        }
        clientes.removeIf(c -> c.getId() == cliente.getId());
        clientes.add(cliente);
        salvarDados();
    }

    @Override
    public synchronized Cliente buscarPorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public synchronized Optional<Cliente> buscarPorDocumento(String documento) {
        if (documento == null) return Optional.empty();
        String limpo = documento.replaceAll("[^0-9a-zA-Z]", "");
        for (Cliente c : clientes) {
            String docCliente = c.getDocumentoPrincipal().replaceAll("[^0-9a-zA-Z]", "");
            if (docCliente.equalsIgnoreCase(limpo)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    @Override
    public synchronized void atualizar(Cliente cliente) {
        if (cliente == null) return;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                salvarDados();
                return;
            }
        }
    }

    @Override
    public synchronized boolean deletar(int id) {
        boolean removido = clientes.removeIf(c -> c.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private synchronized void salvarDados() {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivoDados)) {
            for (Cliente c : clientes) {
                if (c instanceof PessoaFisica) {
                    PessoaFisica pf = (PessoaFisica) c;
                    // Formato: PF;id;nome;email;telefone;endereco;cpf;rg;dataNasc;sexo;estadoCivil
                    bw.write(String.format("PF;%d;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                            pf.getId(), pf.getNome(), pf.getEmail(), pf.getTelefone(), pf.getEndereco(),
                            pf.getCpf(), pf.getRg(), pf.getDataNascimento(), pf.getSexo(),
                            pf.getEstadoCivil() != null ? pf.getEstadoCivil().name() : "SOLTEIRO"));
                } else if (c instanceof PessoaJuridica) {
                    PessoaJuridica pj = (PessoaJuridica) c;
                    // Formato: PJ;id;nome;email;telefone;endereco;cnpj;razaoSocial;ie;nomeFantasia;repLegal
                    bw.write(String.format("PJ;%d;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                            pj.getId(), pj.getNome(), pj.getEmail(), pj.getTelefone(), pj.getEndereco(),
                            pj.getCnpj(), pj.getRazaoSocial(), pj.getInscricaoEstadual(),
                            pj.getNomeFantasia(), pj.getRepresentanteLegal()));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir clientes: " + e.getMessage());
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
                int id = Integer.parseInt(p[1]);
                conexao.sincronizarIdMaximo("cliente", id);

                if ("PF".equals(p[0])) {
                    EstadoCivil ec = EstadoCivil.SOLTEIRO;
                    try { ec = EstadoCivil.valueOf(p[10]); } catch (Exception ignored) {}
                    PessoaFisica pf = new PessoaFisica(id, p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9], ec);
                    clientes.add(pf);
                } else if ("PJ".equals(p[0])) {
                    PessoaJuridica pj = new PessoaJuridica(id, p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9], p[10]);
                    clientes.add(pj);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler clientes do arquivo: " + e.getMessage());
        }
    }
}
