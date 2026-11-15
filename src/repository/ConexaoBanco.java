package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConexaoBanco {
    private static ConexaoBanco instancia;
    private final Path diretorioDados;
    private final ConcurrentHashMap<String, AtomicInteger> contadoresId;

    private ConexaoBanco() {
        this.diretorioDados = Paths.get("data");
        this.contadoresId = new ConcurrentHashMap<>();
        inicializarDiretorio();
    }

    public static synchronized ConexaoBanco getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBanco();
        }
        return instancia;
    }

    private void inicializarDiretorio() {
        try {
            if (!Files.exists(diretorioDados)) {
                Files.createDirectories(diretorioDados);
            }
        } catch (IOException e) {
            System.err.println("Erro ao inicializar diretório de dados: " + e.getMessage());
        }
    }

    public synchronized int getProximoId(String tabela) {
        contadoresId.putIfAbsent(tabela, new AtomicInteger(1));
        return contadoresId.get(tabela).getAndIncrement();
    }

    public synchronized void sincronizarIdMaximo(String tabela, int id) {
        contadoresId.compute(tabela, (k, v) -> {
            if (v == null) {
                return new AtomicInteger(id + 1);
            }
            if (id >= v.get()) {
                v.set(id + 1);
            }
            return v;
        });
    }

    public Path getCaminhoArquivo(String nomeArquivo) {
        return diretorioDados.resolve(nomeArquivo);
    }
}
