package model;

import java.time.LocalDate;

/**
 * Representa uma apólice de seguro de automóvel.
 * Especialização de Apolice com atributos e regras de cálculo específicas para veículos.
 */
public class ApoliceAuto extends Apolice {
    private Veiculo veiculo;
    private int classeBonus; // De 0 a 10 (cada nível dá 5% de desconto)
    private String perfilPrincipalCondutor;

    public ApoliceAuto() {
        super();
    }

    public ApoliceAuto(int id, String numeroApolice, Cliente cliente, double valorPremio,
                       double valorFranquia, LocalDate dataInicioVigencia, LocalDate dataFimVigencia,
                       Veiculo veiculo, int classeBonus, String perfilPrincipalCondutor) {
        super(id, numeroApolice, cliente, valorPremio, valorFranquia, dataInicioVigencia, dataFimVigencia);
        this.veiculo = veiculo;
        this.classeBonus = Math.max(0, Math.min(10, classeBonus));
        this.perfilPrincipalCondutor = perfilPrincipalCondutor;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getClasseBonus() {
        return classeBonus;
    }

    public void setClasseBonus(int classeBonus) {
        this.classeBonus = Math.max(0, Math.min(10, classeBonus));
    }

    public String getPerfilPrincipalCondutor() {
        return perfilPrincipalCondutor;
    }

    public void setPerfilPrincipalCondutor(String perfilPrincipalCondutor) {
        this.perfilPrincipalCondutor = perfilPrincipalCondutor;
    }

    @Override
    public double calcularPremioBase() {
        if (veiculo == null || veiculo.getValorFipe() <= 0) {
            return 1200.0; // Valor padrão mínimo
        }
        // Base: 4% do valor FIPE do veículo
        double base = veiculo.getValorFipe() * 0.04;
        // Desconto por classe de bônus (até 50%)
        double descontoBonus = base * (classeBonus * 0.05);
        return Math.max(500.0, base - descontoBonus);
    }

    @Override
    public String getTipoApolice() {
        return "Seguro Auto";
    }

    @Override
    public void exibirDetalhesApolice() {
        System.out.println("=== Detalhes da Apólice Auto ===");
        System.out.println(this);
        if (veiculo != null) {
            System.out.println("Veículo Segurado: " + veiculo.getDescricaoCompleta());
        }
        System.out.println("Classe de Bônus: Nível " + classeBonus + " (" + (classeBonus * 5) + "% de desconto)");
        System.out.println("Perfil do Condutor: " + perfilPrincipalCondutor);
        System.out.println("Franquia Obrigatória: R$ " + String.format("%.2f", getValorFranquia()));
        System.out.println("Coberturas ativas: " + getCoberturas().size());
    }
}
