package src;

// Classe intermediária entre a Config e a HidrometroSwing
public class HidrometroConfig 
{
    private double diametroEntrada;
    private double velmediaFluxoAgua;
    private int tempoSimulacao;

    public double getDiametroEntrada() { return diametroEntrada; }
    public double getVelmediaFluxoAgua() { return velmediaFluxoAgua; }
    public int getTempoSimulacao() {return tempoSimulacao; }
    
    public void configSetada(double dE, double vmFA, int tS)
    {
        diametroEntrada = dE;
        velmediaFluxoAgua = vmFA;
        tempoSimulacao = tS;
    }
}
