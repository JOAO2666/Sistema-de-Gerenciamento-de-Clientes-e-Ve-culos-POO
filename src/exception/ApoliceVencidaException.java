package exception;

/**
 * Exceção lançada quando uma operação é solicitada para uma apólice cuja vigência expirou.
 */
public class ApoliceVencidaException extends Exception {
    public ApoliceVencidaException(String mensagem) {
        super(mensagem);
    }

    public ApoliceVencidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
