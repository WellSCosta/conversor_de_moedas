public class Conversor {
    private final Double usd;
    private final Double ars;
    private final Double brl;
    private final Double cop;

    public Conversor(Moedas moedas) {
        this.usd = moedas.USD();
        this.cop = moedas.COP();
        this.brl = moedas.BRL();
        this.ars = moedas.ARS();
    }

    public Double getUsd() {
        return usd;
    }

    public Double getArs() {
        return ars;
    }

    public Double getBrl() {
        return brl;
    }

    public Double getCop() {
        return cop;
    }

    public Double converter(String moeda2, Double valor) {
        Double valorMoeda2 = 0.0;

        if (moeda2.equalsIgnoreCase("USD")) {
            valorMoeda2 = this.usd;
        } else if (moeda2.equalsIgnoreCase("ARS")) {
            valorMoeda2 = this.ars;
        } else if (moeda2.equalsIgnoreCase("BRL")) {
            valorMoeda2 = this.brl;
        } else if (moeda2.equalsIgnoreCase("COP")) {
            valorMoeda2 = this.cop;
        }

        return valor * valorMoeda2;
    }

}
