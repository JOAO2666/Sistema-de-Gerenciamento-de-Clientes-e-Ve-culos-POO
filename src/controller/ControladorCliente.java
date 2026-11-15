package controller;

import exception.ClienteNaoEncontradoException;
import model.Cliente;
import model.Veiculo;
import repository.ClienteDAO;
import repository.VeiculoDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ControladorCliente {
    private final ClienteDAO clienteDAO;
    private final VeiculoDAO veiculoDAO;

    public ControladorCliente(ClienteDAO clienteDAO, VeiculoDAO veiculoDAO) {
        this.clienteDAO = clienteDAO;
        this.veiculoDAO = veiculoDAO;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Dados do cliente não podem ser nulos.");
        }
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        String doc = cliente.getDocumentoPrincipal();
        if (doc == null || doc.replaceAll("[^0-9a-zA-Z]", "").isEmpty()) {
            throw new IllegalArgumentException("O CPF/CNPJ do cliente é obrigatório.");
        }
        if (clienteDAO.buscarPorDocumento(doc).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com o CPF/CNPJ informado.");
        }
        clienteDAO.salvar(cliente);
    }

    public List<Cliente> listarTodosClientes() {
        return clienteDAO.listarTodos();
    }

    public Cliente buscarPorId(int id) throws ClienteNaoEncontradoException {
        Cliente c = clienteDAO.buscarPorId(id);
        if (c == null) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + id + " não foi encontrado no sistema.");
        }
        return c;
    }

    public Cliente buscarPorDocumento(String documento) throws ClienteNaoEncontradoException {
        Optional<Cliente> opt = clienteDAO.buscarPorDocumento(documento);
        if (opt.isEmpty()) {
            throw new ClienteNaoEncontradoException("Nenhum cliente encontrado com o documento: " + documento);
        }
        return opt.get();
    }

    public void atualizarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Dados do cliente inválidos.");
        }
        clienteDAO.atualizar(cliente);
    }

    public boolean removerCliente(int id) {
        boolean removido = clienteDAO.deletar(id);
        if (removido) {
            for (Veiculo v : veiculoDAO.listarPorCliente(id)) {
                veiculoDAO.deletar(v.getId());
            }
        }
        return removido;
    }

    public void cadastrarVeiculo(Veiculo veiculo) throws ClienteNaoEncontradoException {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo.");
        }
        buscarPorId(veiculo.getClienteId());
        if (veiculo.getMarca() == null || veiculo.getMarca().isBlank()) {
            throw new IllegalArgumentException("A marca do veículo é obrigatória.");
        }
        if (veiculo.getModelo() == null || veiculo.getModelo().isBlank()) {
            throw new IllegalArgumentException("O modelo do veículo é obrigatório.");
        }
        int anoMax = LocalDate.now().getYear() + 1;
        if (veiculo.getAno() < 1900 || veiculo.getAno() > anoMax) {
            throw new IllegalArgumentException("Ano do veículo inválido (1900 a " + anoMax + ").");
        }
        if (veiculo.getPlaca() == null || veiculo.getPlaca().replaceAll("[^0-9a-zA-Z]", "").isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo é obrigatória.");
        }
        if (veiculoDAO.buscarPorPlaca(veiculo.getPlaca()).isPresent()) {
            throw new IllegalArgumentException("Já existe um veículo cadastrado com a placa informada.");
        }
        if (!Double.isFinite(veiculo.getValorFipe()) || veiculo.getValorFipe() <= 0) {
            throw new IllegalArgumentException("O valor FIPE do veículo deve ser maior que zero.");
        }
        veiculoDAO.salvar(veiculo);
    }

    public boolean removerVeiculo(int veiculoId) {
        return veiculoDAO.deletar(veiculoId);
    }

    public List<Veiculo> listarVeiculosDoCliente(int clienteId) {
        return veiculoDAO.listarPorCliente(clienteId);
    }

    public List<Veiculo> listarTodosVeiculos() {
        return veiculoDAO.listarTodos();
    }
}
