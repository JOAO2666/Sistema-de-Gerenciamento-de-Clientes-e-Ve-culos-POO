package exception;

/**
 * Exceção lançada quando ocorre tentativa de registrar um sinistro já existente ou duplicado para o mesmo evento.
 */
public class SinistroJaRegistradoException extends Exception {
    public SinistroJaRegistradoException(String mensagem) {
        super(mensagem);
    }

    public SinistroJaRegistradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
