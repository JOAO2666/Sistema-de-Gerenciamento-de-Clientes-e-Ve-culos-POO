package model;

/**
 * Interface que define o contrato para bens seguráveis pelo sistema.
 */
public interface Seguravel {
    double getValorAvaliado();
    String getIdentificador();
    String getDescricaoCompleta();
}
