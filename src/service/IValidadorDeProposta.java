package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.Apolice;

public interface IValidadorDeProposta {
    void validarProposta(Apolice apolice) throws ClienteNaoEncontradoException, ValorPremioInvalidoException;
}
