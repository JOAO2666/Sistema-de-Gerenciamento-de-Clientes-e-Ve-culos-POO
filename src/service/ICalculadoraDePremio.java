package service;

import exception.ValorPremioInvalidoException;
import model.Apolice;

/**
 * Interface que define o contrato para cálculo de prêmios de seguro.
 */
public interface ICalculadoraDePremio {
    /**
     * Calcula o prêmio final da apólice considerando base, coberturas e encargos.
     *
     * @param apolice Apólice para cálculo
     * @return Valor final do prêmio
     * @throws ValorPremioInvalidoException caso o prêmio calculado seja nulo ou <= 0
     */
    double calcularPremio(Apolice apolice) throws ValorPremioInvalidoException;
}
