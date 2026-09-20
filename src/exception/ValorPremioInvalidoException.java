package exception;

/**
 * Exceção lançada quando o valor do prêmio calculado ou informado é inválido (ex: <= 0).
 */
public class ValorPremioInvalidoException extends Exception {
    public ValorPremioInvalidoException(String mensagem) {
        super(mensagem);
    }

    public ValorPremioInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
