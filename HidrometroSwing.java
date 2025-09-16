import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

// Classe que representa o fluxo de água
class FluxoAgua {
    private double vazao;      // m³/h
    private double acumulado;  // m³

    public FluxoAgua(double vazaoInicial) {
        this.vazao = vazaoInicial;
        this.acumulado = 0;
    }

    public void setVazao(double vazao) {
        this.vazao = vazao;
    }

    public double getVazao() {
        return vazao;
    }

    public double getAcumulado() {
        return acumulado;
    }

    // Simula passagem de tempo em segundos
    public void simularSegundos(int segundos) {
        if (vazao > 0) {
            double m3PorSegundo = vazao / 3600.0; // converte de m³/h para m³/s
            acumulado += m3PorSegundo * segundos;
        }
    }
}

// Interface gráfica
public class HidrometroSwing extends JFrame {
    private final JLabel lblVazao;
    private final JLabel lblVolume;
    private final FluxoAgua fluxo;
    private final DecimalFormat df2 = new DecimalFormat("0.00");
    private final DecimalFormat df3 = new DecimalFormat("0.000");

    public HidrometroSwing() {
        setTitle("Simulador de Hidrômetro");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        // Instancia fluxo de água com vazão inicial
        fluxo = new FluxoAgua(100); // m³/h inicial

        // Configura labels
        lblVazao = new JLabel("Vazão: 0.00 m³/h", SwingConstants.CENTER);
        lblVolume = new JLabel("Volume acumulado: 0.000 m³", SwingConstants.CENTER);

        lblVazao.setFont(new Font("Consolas", Font.BOLD, 20));
        lblVolume.setFont(new Font("Consolas", Font.BOLD, 20));

        add(lblVazao);
        add(lblVolume);

        // Timer Swing para atualizar a cada 1 segundo
        Timer timer = new Timer(1000, e -> atualizar());
        timer.start();
    }

    // Atualiza a simulação e os labels
    private void atualizar() {
        fluxo.simularSegundos(1); // simula passagem de 1 segundo
        lblVazao.setText("Vazão: " + df2.format(fluxo.getVazao()) + " m³/h");
        lblVolume.setText("Volume acumulado: " + df3.format(fluxo.getAcumulado()) + " m³");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HidrometroSwing tela = new HidrometroSwing();
            tela.setVisible(true);
        });
    }
}
