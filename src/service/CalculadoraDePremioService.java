package service;

import exception.ValorPremioInvalidoException;
import model.Apolice;
import model.Cobertura;

/**
 * Serviço responsável pelo cálculo atuário e tarifário do prêmio do seguro.
 * Aplica polimorfismo ao invocar calcularPremioBase() de cada especialização de Apolice.
 */
public class CalculadoraDePremioService implements ICalculadoraDePremio {
    private static final double ALIQUOTA_IOF = 0.0738; // 7.38% de IOF

    @Override
    public double calcularPremio(Apolice apolice) throws ValorPremioInvalidoException {
        if (apolice == null) {
            throw new ValorPremioInvalidoException("Apólice não informada para cálculo de prêmio.");
        }

        // 1. Cálculo base polimórfico (varia conforme Auto, Residencial ou Vida)
        double premioBase = apolice.calcularPremioBase();

        // 2. Acréscimo pelo custo das coberturas adicionais contratadas
        double custoCoberturas = 0.0;
        if (apolice.getCoberturas() != null) {
            for (Cobertura cob : apolice.getCoberturas()) {
                // 1.2% do valor de limite da cobertura como taxa de risco
                custoCoberturas += (cob.getValorLimite() * 0.012);
            }
        }

        double subtotal = premioBase + custoCoberturas;

        // 3. Aplicação de tributos obrigatórios (IOF)
        double valorIof = subtotal * ALIQUOTA_IOF;
        double premioFinal = subtotal + valorIof;

        // Arredondamento para 2 casas decimais
        premioFinal = Math.round(premioFinal * 100.0) / 100.0;

        if (premioFinal <= 0.0) {
            throw new ValorPremioInvalidoException("O prêmio calculado resultou em valor inválido: R$ " + premioFinal);
        }

        apolice.setValorPremio(premioFinal);
        return premioFinal;
    }
}
