package service;

import exception.ValorPremioInvalidoException;
import model.Apolice;
import model.Cobertura;

public class CalculadoraDePremioService implements ICalculadoraDePremio {
    private static final double ALIQUOTA_IOF = 0.0738;

    @Override
    public double calcularPremio(Apolice apolice) throws ValorPremioInvalidoException {
        return calcularPremio(apolice, 0.0);
    }

    public double calcularPremio(Apolice apolice, double percentualDesconto) throws ValorPremioInvalidoException {
        if (apolice == null) {
            throw new ValorPremioInvalidoException("Apólice não informada para cálculo de prêmio.");
        }

        double premioBase = apolice.calcularPremioBase();

        double custoCoberturas = 0.0;
        if (apolice.getCoberturas() != null) {
            for (Cobertura cob : apolice.getCoberturas()) {
                custoCoberturas += (cob.getValorLimite() * 0.012);
            }
        }

        double subtotal = premioBase + custoCoberturas;
        if (percentualDesconto > 0.0 && percentualDesconto < 100.0) {
            subtotal = subtotal * (1.0 - (percentualDesconto / 100.0));
        }

        double valorIof = subtotal * ALIQUOTA_IOF;
        double premioFinal = subtotal + valorIof;

        premioFinal = Math.round(premioFinal * 100.0) / 100.0;

        if (premioFinal <= 0.0) {
            throw new ValorPremioInvalidoException("O prêmio calculado resultou em valor inválido: R$ " + premioFinal);
        }

        apolice.setValorPremio(premioFinal);
        return premioFinal;
    }
}
