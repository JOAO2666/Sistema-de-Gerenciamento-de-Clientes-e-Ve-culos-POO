package model;

import java.time.LocalDate;

/**
 * Representa uma apólice de seguro residencial.
 * Especialização de Apolice com atributos e regras de cálculo específicas para imóveis.
 */
public class ApoliceResidencial extends Apolice {
    private Imovel imovel;
    private String tipoResidencia; // Casa, Apartamento, Condomínio Fechado
    private boolean possuiAlarme;

    public ApoliceResidencial() {
        super();
    }

    public ApoliceResidencial(int id, String numeroApolice, Cliente cliente, double valorPremio,
                              double valorFranquia, LocalDate dataInicioVigencia, LocalDate dataFimVigencia,
                              Imovel imovel, String tipoResidencia, boolean possuiAlarme) {
        super(id, numeroApolice, cliente, valorPremio, valorFranquia, dataInicioVigencia, dataFimVigencia);
        this.imovel = imovel;
        this.tipoResidencia = tipoResidencia;
        this.possuiAlarme = possuiAlarme;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public String getTipoResidencia() {
        return tipoResidencia;
    }

    public void setTipoResidencia(String tipoResidencia) {
        this.tipoResidencia = tipoResidencia;
    }

    public boolean isPossuiAlarme() {
        return possuiAlarme;
    }

    public void setPossuiAlarme(boolean possuiAlarme) {
        this.possuiAlarme = possuiAlarme;
    }

    @Override
    public double calcularPremioBase() {
        if (imovel == null || imovel.getValorMercado() <= 0) {
            return 800.0;
        }
        // Base: 0.3% do valor de mercado do imóvel
        double base = imovel.getValorMercado() * 0.003;
        // Desconto se possuir alarme/segurança privada (15%)
        if (possuiAlarme || imovel.isPossuiAlarmeIncendio()) {
            base *= 0.85;
        }
        return Math.max(350.0, base);
    }

    @Override
    public String getTipoApolice() {
        return "Seguro Residencial";
    }

    @Override
    public void exibirDetalhesApolice() {
        System.out.println("=== Detalhes da Apólice Residencial ===");
        System.out.println(this);
        if (imovel != null) {
            System.out.println("Imóvel Segurado: " + imovel.getDescricaoCompleta());
        }
        System.out.println("Tipo de Residência: " + tipoResidencia);
        System.out.println("Possui Alarme / Monitoramento: " + (possuiAlarme ? "SIM" : "NÃO"));
        System.out.println("Franquia: R$ " + String.format("%.2f", getValorFranquia()));
        System.out.println("Coberturas ativas: " + getCoberturas().size());
    }
}
