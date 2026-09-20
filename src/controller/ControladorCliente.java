package controller;

import exception.ClienteNaoEncontradoException;
import model.Cliente;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Veiculo;
import repository.ClienteDAO;
import repository.VeiculoDAO;

import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável pela gestão de Clientes (PF e PJ) e seus Veículos.
 */
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
        clienteDAO.atualizar(cliente);
    }

    public boolean removerCliente(int id) {
        return clienteDAO.deletar(id);
    }

    public void cadastrarVeiculo(Veiculo veiculo) throws ClienteNaoEncontradoException {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo.");
        }
        buscarPorId(veiculo.getClienteId()); // Garante que o cliente existe
        veiculoDAO.salvar(veiculo);
    }

    public List<Veiculo> listarVeiculosDoCliente(int clienteId) {
        return veiculoDAO.listarPorCliente(clienteId);
    }

    public List<Veiculo> listarTodosVeiculos() {
        return veiculoDAO.listarTodos();
    }
}
