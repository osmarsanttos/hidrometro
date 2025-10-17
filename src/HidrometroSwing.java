package src;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

// Definição da Classe do Hidrômetro
public class HidrometroSwing extends JFrame 
{
    // Atributos característicos do Hidrômetro
    private final FluxoAgua fluxo;
    private double diametroEntrada;
    private double velmediaFluxoAgua;
    private int hora;
    private int tempoSimulacao;

    // Atributo para o Loop
    private Timer timer;

    // Atributos para o display
    private BufferedImage fundo;
    private BufferedImage ponteiro;

    // Definição da Classe de Display do Hidrômetro
    private class DisplayPanel extends JPanel 
    {
        @Override
        protected void paintComponent(Graphics g) 
        {
            // --- CRIAÇÃO DO OBJETO DA IMAGEM GERADA --- 
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int imgW = getWidth();
            int imgH = getHeight();

            // --- FUNDO ---
            if (fundo != null) 
            {
                g2d.drawImage(fundo, 0, 0, imgW, imgH, null);
            }

            // --- CENTROS DOS PONTEIROS COM SUAS COORDENADAS RELATIVAS ---
            int leftCenterX = (int) (imgW * 0.51);
            int leftCenterY = (int) (imgH * 0.64);
            int rightCenterX = (int) (imgW * 0.6182);
            int rightCenterY = (int) (imgH * 0.559);

            // --- CÁLCULO DOS DÍGITOS ---
            int totalLitros = (int) Math.round(fluxo.getVolume() * 1000); 
            int litrosUnidade = totalLitros % 10;       
            int litrosDezena  = (totalLitros / 10) % 10; 

            // --- ÂNGULOS DOS PONTEIROS---
            double leftAngle  = Math.toRadians(-90 + (litrosDezena / 10.0) * 360);
            double rightAngle = Math.toRadians(-90 + (litrosUnidade / 10.0) * 360);   

            // --- TAMANHO DOS PONTEIROS ---
            int pointerLength = Math.min(imgW, imgH) / 30; // comprimento do ponteiro
            int pointerWidth  = pointerLength / 2;        // largura proporcional fina
            AffineTransform at = new AffineTransform();

            if (ponteiro != null) 
            {
                // --- REDIMENSIONA O PNG DOS PONTEIROS --- 
                BufferedImage scaledPointer = new BufferedImage(pointerWidth, pointerLength, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gPtr = scaledPointer.createGraphics();
                gPtr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gPtr.drawImage(ponteiro, 0, 0, pointerWidth, pointerLength, null);
                gPtr.dispose();

                // --- ACHA AUTOMATICAMENTE A BASE REAL DO PNG DO PONTEIRO (DESCONSIDERANDO TRANSPARÊNCIA) ---
                int ax = pointerWidth / 2;   // fallback: meio
                int ay = pointerLength;      // fallback: base geométrica

                // --- PROCURA A ÚLTIMA LINHA NÃO TRANSPARENTE --- 
                int yBase = -1;
                for (int y = pointerLength - 1; y >= 0 && yBase == -1; y--) 
                {
                    for (int x = 0; x < pointerWidth; x++) 
                    {
                        int a = (scaledPointer.getRGB(x, y) >>> 24) & 0xff;
                        if (a > 10) 
                        { 
                            yBase = y; break; 
                        }
                    }
                }
                if (yBase >= 0) 
                {
                    // pega a largura útil nessa linha e centraliza
                    int xMin = pointerWidth, xMax = -1;
                    for (int x = 0; x < pointerWidth; x++) 
                    {
                        int a = (scaledPointer.getRGB(x, yBase) >>> 24) & 0xff;
                        if (a > 10) 
                        { 
                            xMin = Math.min(xMin, x); xMax = Math.max(xMax, x); 
                        }
                    }
                    if (xMax >= xMin) 
                    { 
                        ax = (xMin + xMax) / 2; ay = yBase; 
                    }
                }

                // --- PONTEIRO ESQUERDO ---
                at.setToIdentity();
                at.translate(leftCenterX, leftCenterY); 
                at.rotate(leftAngle);                   
                at.translate(-ax, -ay);                
                g2d.drawImage(scaledPointer, at, null);

                // --- PONTEIRO DIREITO ---
                at.setToIdentity();
                at.translate(rightCenterX, rightCenterY);
                at.rotate(rightAngle);
                at.translate(-ax, -ay);
                g2d.drawImage(scaledPointer, at, null);

            } 

            else 
            {
                // fallback: linhas
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(pointerWidth));
                int leftEndX  = (int)(leftCenterX  + pointerLength * Math.cos(leftAngle));
                int leftEndY  = (int)(leftCenterY  + pointerLength * Math.sin(leftAngle));
                int rightEndX = (int)(rightCenterX + pointerLength * Math.cos(rightAngle));
                int rightEndY = (int)(rightCenterY + pointerLength * Math.sin(rightAngle));
                g2d.drawLine(leftCenterX,  leftCenterY,  leftEndX,  leftEndY);
                g2d.drawLine(rightCenterX, rightCenterY, rightEndX, rightEndY);
            }


            // --- LCD METROS CÚBICOS REDIMENSIONÁVEL ---
            int lcdX = (int) (imgW * 0.41); // posição horizontal relativa
            int lcdY = (int) (imgH * 0.405); // posição vertical relativa

            // --- DIMENSÕES DOS DÍGITOS PROPORCIONAIS AO TAMANHO DO LCD ---
            int digitWidth = imgW / 32;   // largura do dígito proporcional à largura da janela
            int digitHeight = imgH / 14;  // altura proporcional à altura da janela
            int spacing = digitWidth / 8; // espaço entre os dígitos

            // --- SELEÇÃO DA FONTE ---
            g2d.setFont(new Font("Monospaced", Font.BOLD, digitHeight / 2));
            FontMetrics fm = g2d.getFontMetrics();

            // --- FORMATA O VOLUME ---
            String volumeStr = String.format("%08.0f", fluxo.getVolume()).replace(".", "");
            volumeStr = volumeStr.substring(volumeStr.length() - 6);

            // --- DESENHA CADA DÍGITO ---
            for (int i = 0; i < 6; i++) 
            {
                int boxX = lcdX + i * (digitWidth + spacing);
                int boxY = lcdY - digitHeight + 5;

                // fundo do dígito
                g2d.setColor(Color.WHITE);
                g2d.fillRect(boxX, boxY, digitWidth, digitHeight);

                // borda do dígito
                g2d.setColor(Color.BLACK);
                g2d.drawRect(boxX, boxY, digitWidth, digitHeight);

                // dígito centralizado
                char digit = (i < volumeStr.length()) ? volumeStr.charAt(i) : '0';
                int digitX = boxX + (digitWidth - fm.stringWidth(String.valueOf(digit))) / 2;
                int digitY = boxY + ((digitHeight - fm.getHeight()) / 2) + fm.getAscent();

                g2d.drawString(String.valueOf(digit), digitX, digitY);
            }

        }
  
    }

    private DisplayPanel panel;

    public HidrometroSwing() 
    {
        // Inicialização do título da janela e tamanho
        setTitle("Display do Hidrômetro");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        
        // Carregamento das imagens
        loadImages();
        
        panel = new DisplayPanel();
        add(panel);
        
        // Adiciona uma barra de menu
        setupMenuBar();

        // Instancia os atributos para valores padrão
        fluxo = new FluxoAgua();
        diametroEntrada = 0.0;
        velmediaFluxoAgua = 0.0;
        hora = 0;
        tempoSimulacao = 1;
    }

    private void loadImages() 
    {
        try 
        {
            // Tenta carregar do sistema de arquivos (pasta images na raiz do projeto)

            File fundoFile = new File("images/Hidrometro.png");
            File ponteiroFile = new File("images/Ponteiro.png");
            
            if (fundoFile.exists()) 
            {
                fundo = ImageIO.read(fundoFile);
            } 
            else 
            {
                System.out.println("Warning: Background image not found at " + fundoFile.getAbsolutePath());
            }
            
            if (ponteiroFile.exists()) 
            {
                ponteiro = ImageIO.read(ponteiroFile);
            } 
            else 
            {
                System.out.println("Warning: Pointer image not found at " + ponteiroFile.getAbsolutePath());
            }
        } 

        catch (IOException e) 
        {
            // Exibição de erro caso as imagens não sejam carregadas
            System.err.println("Error loading images: " + e.getMessage());
        }
    }
    
    private void setupMenuBar() 
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Controles");
        
        JMenuItem reconnect = new JMenuItem("Reconectar");
        
        JMenuItem exit = new JMenuItem("Sair");
        exit.addActionListener(e -> System.exit(0));
        
        menu.add(reconnect);
        menu.addSeparator();
        menu.add(exit);
        menuBar.add(menu);
        
        setJMenuBar(menuBar);
    }

    public void applyConfig(HidrometroConfig cfg) 
    {
        // Método para aplicação das configurações do Hidrômetro e inicialização do Timer

        if (cfg == null) return;

        this.diametroEntrada = cfg.getDiametroEntrada();
        this.velmediaFluxoAgua = cfg.getVelmediaFluxoAgua();
        this.tempoSimulacao = cfg.getTempoSimulacao();

        int intervalo = 1000 * tempoSimulacao;

        if (timer != null) 
        {
            timer.stop();
        }

        timer = new Timer(intervalo, e -> simular());
        timer.start();
    }

    private void simular() 
    {
        // Método para iniciar a simulação 

        fluxo.atualizar(hora, diametroEntrada, velmediaFluxoAgua);
        SwingUtilities.invokeLater(panel::repaint);
        hora++;
    }

    public static void main(String[] args) 
    {
        // Detecta o sistema operacional
        String os = System.getProperty("os.name").toLowerCase();
        Interface uiImpl;

        // Define a implementação de acordo com o SO
        if (os.contains("win")) 
        {
            uiImpl = new WindowsUI();
        } 
        else if (os.contains("mac")) 
        {
            uiImpl = new MacUI();
        } 
        else 
        {
            uiImpl = new WindowsUI(); // padrão genérico
        }

        // Usa o bridge
        UIBridge ui = new UIBridge(uiImpl);

        // Entrada de dados via janela
        String entrada = ui.receberEntrada("Digite a quantidade de hidrômetros (1-5):");
        int quantidade = Integer.parseInt(entrada);

        // Mostra mensagem de confirmação
        ui.mostrarMensagem("Quantidade informada: " + quantidade);

        
        for (int i = 0; i < quantidade; i++) 
        {
            final int qtde = i + 1;
            final String configPath = "./config/config" + qtde + ".json";
            
            SwingUtilities.invokeLater(() -> {
                // Carrega config específica pra cada hidrômetro
                HidrometroConfig cfg = Config.lerConfig(configPath);
                
                // Constrói o hidrômetro propriamente dito
                HidrometroSwing tela = new HidrometroSwing();
                tela.setTitle("Hidrômetro " + qtde);
                tela.applyConfig(cfg);
                tela.setLocation(200 * qtde, 100 * qtde);
                tela.setVisible(true);
            });
        }
    
    }
}