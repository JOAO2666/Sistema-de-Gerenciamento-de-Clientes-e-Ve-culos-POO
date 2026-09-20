package service;

import exception.ClienteNaoEncontradoException;
import exception.ValorPremioInvalidoException;
import model.*;

/**
 * Serviço responsável por validar a integridade técnica e contratual de uma proposta.
 */
public class ValidadorDePropostaService implements IValidadorDeProposta {

    @Override
    public void validarProposta(Apolice apolice) throws ClienteNaoEncontradoException, ValorPremioInvalidoException {
        if (apolice == null) {
            throw new IllegalArgumentException("A proposta de apólice não pode ser nula.");
        }

        // 1. Validação de Cliente
        if (apolice.getCliente() == null || apolice.getCliente().getId() <= 0) {
            throw new ClienteNaoEncontradoException("Cliente não informado ou não cadastrado para a proposta.");
        }

        // 2. Validação de Vigência
        if (apolice.getDataInicioVigencia() == null || apolice.getDataFimVigencia() == null) {
            throw new IllegalArgumentException("As datas de início e fim de vigência são obrigatórias.");
        }

        if (apolice.getDataFimVigencia().isBefore(apolice.getDataInicioVigencia())) {
            throw new IllegalArgumentException("A data de término da vigência não pode ser anterior à data de início.");
        }

        // 3. Validação de Regras Específicas por Tipo de Apólice (Polimorfismo)
        if (apolice instanceof ApoliceAuto) {
            ApoliceAuto auto = (ApoliceAuto) apolice;
            if (auto.getVeiculo() == null) {
                throw new IllegalArgumentException("É obrigatório vincular um veículo válido à apólice de automóvel.");
            }
            if (auto.getVeiculo().getPlaca() == null || auto.getVeiculo().getPlaca().isBlank()) {
                throw new IllegalArgumentException("O veículo deve conter uma placa válida informada.");
            }
        } else if (apolice instanceof ApoliceResidencial) {
            ApoliceResidencial res = (ApoliceResidencial) apolice;
            if (res.getImovel() == null) {
                throw new IllegalArgumentException("É obrigatório vincular um imóvel válido à apólice residencial.");
            }
            if (res.getImovel().getEndereco() == null || res.getImovel().getEndereco().isBlank()) {
                throw new IllegalArgumentException("O imóvel deve conter endereço completo informado.");
            }
        } else if (apolice instanceof ApoliceDeVida) {
            ApoliceDeVida vida = (ApoliceDeVida) apolice;
            if (vida.getCapitalSegurado() <= 0) {
                throw new ValorPremioInvalidoException("O capital segurado para apólice de vida deve ser maior que zero.");
            }
            if (vida.getIdadeSegurado() <= 0 || vida.getIdadeSegurado() > 100) {
                throw new IllegalArgumentException("Idade do segurado inválida para contratação de seguro de vida (1 a 100 anos).");
            }
        }
    }
}
