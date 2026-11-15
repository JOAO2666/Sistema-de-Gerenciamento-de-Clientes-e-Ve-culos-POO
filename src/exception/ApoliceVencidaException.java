package exception;

public class ApoliceVencidaException extends Exception {
    public ApoliceVencidaException(String mensagem) {
        super(mensagem);
    }

    public ApoliceVencidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
