package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma apólice de seguro de vida individual ou em grupo.
 * Especialização de Apolice com regras de cálculo atuariais simplificadas.
 */
public class ApoliceDeVida extends Apolice {
    private double capitalSegurado;
    private boolean fumante;
    private int idadeSegurado;
    private List<String> beneficiarios;

    public ApoliceDeVida() {
        super();
        this.beneficiarios = new ArrayList<>();
    }

    public ApoliceDeVida(int id, String numeroApolice, Cliente cliente, double valorPremio,
                         double valorFranquia, LocalDate dataInicioVigencia, LocalDate dataFimVigencia,
                         double capitalSegurado, boolean fumante, int idadeSegurado) {
        super(id, numeroApolice, cliente, valorPremio, valorFranquia, dataInicioVigencia, dataFimVigencia);
        this.capitalSegurado = capitalSegurado;
        this.fumante = fumante;
        this.idadeSegurado = idadeSegurado;
        this.beneficiarios = new ArrayList<>();
    }

    public double getCapitalSegurado() {
        return capitalSegurado;
    }

    public void setCapitalSegurado(double capitalSegurado) {
        this.capitalSegurado = capitalSegurado;
    }

    public boolean isFumante() {
        return fumante;
    }

    public void setFumante(boolean fumante) {
        this.fumante = fumante;
    }

    public int getIdadeSegurado() {
        return idadeSegurado;
    }

    public void setIdadeSegurado(int idadeSegurado) {
        this.idadeSegurado = idadeSegurado;
    }

    public List<String> getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(List<String> beneficiarios) {
        this.beneficiarios = beneficiarios != null ? beneficiarios : new ArrayList<>();
    }

    public void adicionarBeneficiario(String beneficiario) {
        if (beneficiario != null && !beneficiario.isBlank()) {
            this.beneficiarios.add(beneficiario.trim());
        }
    }

    @Override
    public double calcularPremioBase() {
        if (capitalSegurado <= 0) {
            return 600.0;
        }
        // Base proporcional ao capital segurado (0.4%)
        double base = capitalSegurado * 0.004;

        // Fator idade: acréscimo proporcional para idades mais avançadas
        if (idadeSegurado > 50) {
            base *= 1.4;
        } else if (idadeSegurado > 35) {
            base *= 1.15;
        }

        // Fator tabagismo (+30% de agravo de risco)
        if (fumante) {
            base *= 1.30;
        }

        return Math.max(300.0, base);
    }

    @Override
    public String getTipoApolice() {
        return "Seguro de Vida";
    }

    @Override
    public void exibirDetalhesApolice() {
        System.out.println("=== Detalhes da Apólice de Vida ===");
        System.out.println(this);
        System.out.println("Capital Segurado: R$ " + String.format("%.2f", capitalSegurado));
        System.out.println("Idade do Segurado: " + idadeSegurado + " anos");
        System.out.println("Fumante: " + (fumante ? "SIM (+30% risco)" : "NÃO"));
        System.out.println("Beneficiários Cadastrados (" + beneficiarios.size() + "): " +
                (beneficiarios.isEmpty() ? "Herdeiros Legais" : String.join(", ", beneficiarios)));
        System.out.println("Coberturas ativas: " + getCoberturas().size());
    }
}
