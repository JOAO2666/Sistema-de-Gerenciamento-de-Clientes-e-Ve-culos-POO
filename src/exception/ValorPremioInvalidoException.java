package exception;

public class ValorPremioInvalidoException extends Exception {
    public ValorPremioInvalidoException(String mensagem) {
        super(mensagem);
    }

    public ValorPremioInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
