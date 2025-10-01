package src;

// Definição da classe que realiza as operações para o volume acumulado de água
class FluxoAgua 
{
    private double volume;  

    // Construtor padrão
    public FluxoAgua() 
    {
        this.volume = 0.0;
    }

    // get e set
    public double getVolume() 
    {
        return volume;
    }

    private void setVolume(double vol)
    {
        volume += vol;
    }
    
    // Operação de medição de volume a partir do diâmetro de entrada e velocidade média de fluxo de água
    private double medicao(double dE, double vmFA)
    {
        double area = Math.PI * (dE * dE / 4);

        return (area * vmFA * 3600);
    }
 
    // Seta o volume após aplicado fator oscilatório
    public void atualizar(int tempo, double dE, double vmFA)
    {
        int horaDoDia = tempo % 24; // Atualiza o tempo para horas do dia em um padrão de 00h a 23h
    
        double oscillacao = (Math.sin((2* Math.PI * horaDoDia / 24.0) - (Math.PI / 2)) + 1.0) / 2.0; 
        double fatorOscilatorio = 0.1 + (oscillacao * 0.9); // Calcula uma oscilação para simular a variação do consumo de água ao longo do dia
        
        double velocidadeOscilatoria = vmFA * fatorOscilatorio; // Aplica a oscilação na velocidade média de fluxo de água 
        double vol = medicao(dE, velocidadeOscilatoria);
        
        setVolume(vol);
    }
}