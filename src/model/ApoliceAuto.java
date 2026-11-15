package model;

import java.time.LocalDate;

public class ApoliceAuto extends Apolice {
    private Veiculo veiculo;
    private int classeBonus;
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
            return 1200.0;
        }
        double base = veiculo.getValorFipe() * 0.04;
        double descontoBonus = base * (classeBonus * 0.05);
        return Math.max(500.0, base - descontoBonus);
    }

    @Override
    public String getTipoApolice() {
        return "Seguro Auto";
    }

    @Override
    public void exibirDetalhesApolice() {
        System.out.println("\n--- Dados da Apólice Auto ---");
        System.out.println(this);
        if (veiculo != null) {
            System.out.println("Veículo: " + veiculo.getDescricaoCompleta());
        }
        System.out.println("Classe de bônus: " + classeBonus + " (" + (classeBonus * 5) + "% de desconto)");
        System.out.println("Perfil do condutor: " + perfilPrincipalCondutor);
        System.out.printf("Franquia: R$ %.2f\n", getValorFranquia());
    }
}
