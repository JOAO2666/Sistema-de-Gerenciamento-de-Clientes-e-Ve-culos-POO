import controller.ControladorApolice;
import controller.ControladorCliente;
import repository.*;
import service.EmissorDeApoliceService;
import service.ProcessadorDeSinistroService;
import view.MenuPrincipal;

/**
 * Ponto de entrada principal do Sistema de Gerenciamento de Clientes, Veículos e Seguros (POO).
 * Inicializa a injeção de dependências das camadas de Persistência, Negócio e Controle.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Inicializando Sistema de Gerenciamento POO...  ");
        System.out.println("=================================================");

        // 1. Inicialização da Camada de Persistência (DAO)
        ClienteDAO clienteDAO = new ClienteDAO();
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        ApoliceDAO apoliceDAO = new ApoliceDAO(clienteDAO, veiculoDAO);
        SinistroDAO sinistroDAO = new SinistroDAO();

        // 2. Inicialização da Camada de Serviços / Regras de Negócio
        EmissorDeApoliceService emissorService = new EmissorDeApoliceService(apoliceDAO, pagamentoDAO);
        ProcessadorDeSinistroService sinistroService = new ProcessadorDeSinistroService(apoliceDAO, sinistroDAO);

        // 3. Inicialização da Camada de Controle
        ControladorCliente controladorCliente = new ControladorCliente(clienteDAO, veiculoDAO);
        ControladorApolice controladorApolice = new ControladorApolice(apoliceDAO, pagamentoDAO, sinistroDAO, emissorService, sinistroService);

        // 4. Inicialização da Camada de Apresentação (View)
        MenuPrincipal menu = new MenuPrincipal(controladorCliente, controladorApolice);
        menu.exibirMenu();
    }
}
