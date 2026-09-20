package exception;

/**
 * Exceção lançada quando uma busca por cliente não retorna nenhum resultado.
 */
public class ClienteNaoEncontradoException extends Exception {
    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public ClienteNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
