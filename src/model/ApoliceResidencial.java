package model;

import java.time.LocalDate;

public class ApoliceResidencial extends Apolice {
    private Imovel imovel;
    private String tipoResidencia;
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
        double base = imovel.getValorMercado() * 0.003;
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
        System.out.println("\n--- Dados da Apólice Residencial ---");
        System.out.println(this);
        if (imovel != null) {
            System.out.println("Imóvel: " + imovel.getDescricaoCompleta());
        }
        System.out.println("Tipo de residência: " + tipoResidencia);
        System.out.println("Possui alarme: " + (possuiAlarme ? "Sim" : "Não"));
        System.out.printf("Franquia: R$ %.2f\n", getValorFranquia());
    }
}
