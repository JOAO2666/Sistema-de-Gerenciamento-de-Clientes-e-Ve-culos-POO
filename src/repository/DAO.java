package repository;

import java.util.List;

/**
 * Interface genérica que define os contratos de operações CRUD para a camada DAO.
 * Demonstra o uso de Generics e Abstração em POO.
 *
 * @param <T> Tipo da entidade
 */
public interface DAO<T> {
    /**
     * Salva ou persiste uma entidade.
     */
    void salvar(T entidade);

    /**
     * Busca uma entidade pelo seu ID único.
     */
    T buscarPorId(int id);

    /**
     * Retorna a lista de todas as entidades persistidas.
     */
    List<T> listarTodos();

    /**
     * Atualiza os dados de uma entidade existente.
     */
    void atualizar(T entidade);

    /**
     * Remove uma entidade pelo ID.
     * @return true se removido com sucesso, false caso contrário.
     */
    boolean deletar(int id);
}
