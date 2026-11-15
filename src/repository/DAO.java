package repository;

import java.util.List;

public interface DAO<T> {
    void salvar(T entidade);

    T buscarPorId(int id);

    List<T> listarTodos();

    void atualizar(T entidade);

    boolean deletar(int id);
}
