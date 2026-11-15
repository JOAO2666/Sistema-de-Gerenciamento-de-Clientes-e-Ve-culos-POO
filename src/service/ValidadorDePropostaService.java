package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.*;

public class ValidadorDePropostaService implements IValidadorDeProposta {

    @Override
    public void validarProposta(Apolice apolice) throws ClienteNaoEncontradoException, ValorPremioInvalidoException {
        if (apolice == null) {
            throw new IllegalArgumentException("A proposta de apólice não pode ser nula.");
        }

        if (apolice.getCliente() == null || apolice.getCliente().getId() <= 0) {
            throw new ClienteNaoEncontradoException("Cliente não informado ou não cadastrado para a proposta.");
        }

        if (apolice.getDataInicioVigencia() == null || apolice.getDataFimVigencia() == null) {
            throw new IllegalArgumentException("As datas de início e fim de vigência são obrigatórias.");
        }

        if (apolice.getDataFimVigencia().isBefore(apolice.getDataInicioVigencia())) {
            throw new IllegalArgumentException("A data de término da vigência não pode ser anterior à data de início.");
        }

        if (!Double.isFinite(apolice.getValorFranquia()) || apolice.getValorFranquia() < 0) {
            throw new ValorPremioInvalidoException("O valor da franquia não pode ser negativo.");
        }

        if (apolice instanceof ApoliceAuto) {
            ApoliceAuto auto = (ApoliceAuto) apolice;
            if (auto.getVeiculo() == null) {
                throw new IllegalArgumentException("É obrigatório vincular um veículo válido à apólice de automóvel.");
            }
            if (auto.getVeiculo().getPlaca() == null || auto.getVeiculo().getPlaca().isBlank()) {
                throw new IllegalArgumentException("O veículo deve conter uma placa válida informada.");
            }
            if (!Double.isFinite(auto.getVeiculo().getValorFipe()) || auto.getVeiculo().getValorFipe() <= 0) {
                throw new ValorPremioInvalidoException("O valor FIPE do veículo deve ser maior que zero.");
            }
        } else if (apolice instanceof ApoliceResidencial) {
            ApoliceResidencial res = (ApoliceResidencial) apolice;
            if (res.getImovel() == null) {
                throw new IllegalArgumentException("É obrigatório vincular um imóvel válido à apólice residencial.");
            }
            if (res.getImovel().getEndereco() == null || res.getImovel().getEndereco().isBlank()) {
                throw new IllegalArgumentException("O imóvel deve conter endereço completo informado.");
            }
            if (!Double.isFinite(res.getImovel().getValorMercado()) || res.getImovel().getValorMercado() <= 0) {
                throw new ValorPremioInvalidoException("O valor de mercado do imóvel deve ser maior que zero.");
            }
            if (!Double.isFinite(res.getImovel().getAreaM2()) || res.getImovel().getAreaM2() <= 0) {
                throw new IllegalArgumentException("A área construída do imóvel deve ser maior que zero.");
            }
        } else if (apolice instanceof ApoliceDeVida) {
            ApoliceDeVida vida = (ApoliceDeVida) apolice;
            if (!Double.isFinite(vida.getCapitalSegurado()) || vida.getCapitalSegurado() <= 0) {
                throw new ValorPremioInvalidoException("O capital segurado para apólice de vida deve ser maior que zero.");
            }
            if (vida.getIdadeSegurado() <= 0 || vida.getIdadeSegurado() > 100) {
                throw new IllegalArgumentException("Idade do segurado inválida para contratação de seguro de vida (1 a 100 anos).");
            }
        }
    }
}
