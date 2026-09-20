package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.Apolice;

/**
 * Interface que define o contrato para validação de propostas de seguro antes da emissão.
 */
public interface IValidadorDeProposta {
    /**
     * Valida os requisitos mínimos da proposta de apólice.
     *
     * @param apolice Proposta a ser validada
     * @throws ClienteNaoEncontradoException se o cliente associado for inexistente
     * @throws ValorPremioInvalidoException se os valores contratuais forem inconsistentes
     * @throws IllegalArgumentException para erros de preenchimento ou vigência
     */
    void validarProposta(Apolice apolice) throws ClienteNaoEncontradoException, ValorPremioInvalidoException;
}
