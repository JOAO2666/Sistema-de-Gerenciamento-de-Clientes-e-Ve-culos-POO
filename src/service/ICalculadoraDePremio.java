package service;

import exception.ValorPremioInvalidoException;
import model.Apolice;

public interface ICalculadoraDePremio {
    double calcularPremio(Apolice apolice) throws ValorPremioInvalidoException;
}
