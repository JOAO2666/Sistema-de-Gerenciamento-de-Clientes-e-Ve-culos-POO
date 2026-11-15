package exception;

public class SinistroJaRegistradoException extends Exception {
    public SinistroJaRegistradoException(String mensagem) {
        super(mensagem);
    }

    public SinistroJaRegistradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
