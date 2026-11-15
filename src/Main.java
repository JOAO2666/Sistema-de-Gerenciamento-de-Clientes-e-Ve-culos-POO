import controller.ControladorApolice;
import controller.ControladorCliente;
import repository.*;
import service.EmissorDeApoliceService;
import service.ProcessadorDeSinistroService;
import view.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAO();
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        ApoliceDAO apoliceDAO = new ApoliceDAO(clienteDAO, veiculoDAO);
        SinistroDAO sinistroDAO = new SinistroDAO();

        EmissorDeApoliceService emissorService = new EmissorDeApoliceService(apoliceDAO, pagamentoDAO);
        ProcessadorDeSinistroService sinistroService = new ProcessadorDeSinistroService(apoliceDAO, sinistroDAO);

        ControladorCliente controladorCliente = new ControladorCliente(clienteDAO, veiculoDAO);
        ControladorApolice controladorApolice = new ControladorApolice(apoliceDAO, pagamentoDAO, sinistroDAO, emissorService, sinistroService);

        MenuPrincipal menu = new MenuPrincipal(controladorCliente, controladorApolice);
        menu.exibirMenu();
    }
}
