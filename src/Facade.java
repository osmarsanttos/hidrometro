package src;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Facade 
{
    private final Map<Integer, HidrometroSwing> hidrometros = new ConcurrentHashMap<>();
    private final Map<Integer, Thread> threads = new ConcurrentHashMap<>();

    HidrometroConfig cfg = new HidrometroConfig();

    // Reaplica uma configuração completa
    public void configSimuladorSHA(int id, String caminhoArquivo, double dE, double vmFA, int tS) 
    {
        try 
        {
            // Lê e atualiza o JSON existente
            JsonObject root;

            try (FileReader fr = new FileReader(caminhoArquivo)) 
            {
                root = JsonParser.parseReader(fr).getAsJsonObject();
            }

            root.addProperty("diametroEntrada", dE);
            root.addProperty("velmediaFluxoAgua", vmFA);
            root.addProperty("tempoSimulacao", tS);

            try (FileWriter fw = new FileWriter(caminhoArquivo)) 
            {
                new GsonBuilder().setPrettyPrinting().create().toJson(root, fw);
            }

            // Lê a configuração atualizada
            HidrometroConfig cfg = Config.lerConfig(caminhoArquivo);
            if (cfg == null) 
            {
                System.err.println("Erro ao ler a configuração: " + caminhoArquivo);
                return;
            }

            // Cria e inicia o hidrômetro em uma thread separada
            Thread thread = new Thread(() -> {
                HidrometroSwing h = new HidrometroSwing();
                h.applyConfig(cfg);
                h.setTitle("Hidrômetro " + id);
                h.setVisible(true);
                hidrometros.put(id, h);
            });

            threads.put(id, thread);
            thread.start();

            System.out.println("Hidrômetro " + id + " iniciado.");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // Cria e inicializa o simulador a partir de um arquivo JSON
    public void criaSHA(int id) 
    {
        Thread thread = new Thread(() -> {
            HidrometroSwing h = new HidrometroSwing();
            cfg.configSetada(0.0246, 1.522, 5);
            h.applyConfig(cfg);
            h.setTitle("Hidrômetro " + id);
            h.setVisible(true);
            hidrometros.put(id, h);
        });

        threads.put(id, thread);
        thread.start();
        System.out.println("Hidrômetro " + id + " criado com configuração padrão.");
    }

    // Encerra a simulação
    public void finalizaSHA(int id) 
    {
        HidrometroSwing h = hidrometros.get(id);
        
        if (h != null) 
        {
            h.dispose();
            hidrometros.remove(id);
            threads.remove(id);
            System.out.println("Hidrômetro " + id + " finalizado.");
        } 
        else 
        {
            System.out.println("Nenhum hidrômetro ativo com ID " + id);
        }
    }

    // Modifica os parâmetros de simulação em execução
    public void modificaVazaoSHA(int id, float dE, float vmFA, int tS) 
    {
        HidrometroSwing h = hidrometros.get(id);

        if (h == null) 
        {
            System.err.println("Nenhum hidrômetro ativo com ID " + id);
            return;
        }

        // Atualiza a configuração usada por essa instância
        HidrometroConfig novaCfg = new HidrometroConfig();
        novaCfg.configSetada(dE, vmFA, tS);
        h.applyConfig(novaCfg);

        System.out.println("Configuração do hidrômetro " + id + " atualizada: diamêtro de Entrada =" + dE +
                ", velocidade média de Fluxo de Água =" + vmFA + ", tempo da Simulação =" + tS);
    }

    public void habilitaGeracaoImagemSHA(int id, boolean habilitar) 
    {
        HidrometroSwing h = hidrometros.get(id);
        if (h == null) 
        {
            System.err.println("Nenhum hidrômetro ativo com ID " + id);
            return;
        }

        try 
        {
            JPanel panel = h.getDisplayPanel();
            if (panel == null) 
            {
                System.err.println("Painel não encontrado no hidrômetro " + id);
                return;
            }

            if (habilitar) 
            {
                if (!h.isVisible()) 
                {
                    h.setVisible(true);
                    System.out.println("Janela do hidrômetro " + id + " reaberta.");
                }
            } 
            else 
            {
                if (h.isVisible()) 
                {
                    h.dispose();
                    System.out.println("Janela do hidrômetro " + id + " fechada (simulação continua rodando).");
                } 
                else 
                {
                    System.out.println("A janela do hidrômetro " + id + " já estava fechada.");
                }
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void finalizaTodosSHA() 
    {
        hidrometros.values().forEach(HidrometroSwing::dispose);
        hidrometros.clear();
        threads.clear();
        System.out.println("Todas as instâncias encerradas.");
    }
}
