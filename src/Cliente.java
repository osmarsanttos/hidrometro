package src;

import java.util.Scanner;

public class Cliente 
{
    public static void main(String[] args) 
    {
        Facade sistema = new Facade();
        Scanner sc = new Scanner(System.in);
        boolean executando = true;

        System.out.println("=== Simulador de Hidrômetros ===");
        exibirComandos();

        while (executando) 
        {
            System.out.print("\n> ");
            String comando = sc.nextLine().trim().toLowerCase();
            String[] partes = comando.split("\\s+");

            switch (partes[0]) 
            {
                case "criar":
                    if (partes.length < 2) 
                    {
                        System.out.println("Uso: criar <id>");
                        break;
                    }
                    int idCriar = Integer.parseInt(partes[1]);
                    sistema.criaSHA(idCriar);
                    break;

                case "config":
                    if (partes.length < 5) 
                    {
                        System.out.println("Uso: config <id> <diametro> <velocidade> <tempo>");
                        break;
                    }
                    int idCfg = Integer.parseInt(partes[1]);
                    String caminho = "./config/config.json";
                    double dE = Double.parseDouble(partes[2]);
                    double vmFA = Double.parseDouble(partes[3]);
                    int tS = Integer.parseInt(partes[4]);
                    sistema.configSimuladorSHA(idCfg, caminho, dE, vmFA, tS);
                    break;

                case "modificar":
                    if (partes.length < 5) 
                    {
                        System.out.println("Uso: modificar <id> <diametro> <velocidade> <tempo>");
                        break;
                    }
                    int idMod = Integer.parseInt(partes[1]);
                    float novoDE = Float.parseFloat(partes[2]);
                    float novaVel = Float.parseFloat(partes[3]);
                    int novoTempo = Integer.parseInt(partes[4]);
                    sistema.modificaVazaoSHA(idMod, novoDE, novaVel, novoTempo);
                    break;

                case "imagem":
                    if (partes.length < 3) 
                    {
                        System.out.println("Uso: imagem <id> <on/off>");
                        break;
                    }
                    int idImg = Integer.parseInt(partes[1]);
                    boolean habilitar = partes[2].equalsIgnoreCase("on");
                    sistema.habilitaGeracaoImagemSHA(idImg, habilitar);
                    break;

                case "finalizar":

                    if (partes.length < 2) 
                    {
                        System.out.println("Uso: finalizar <id>");
                        break;
                    }
                    int idFin = Integer.parseInt(partes[1]);
                    sistema.finalizaSHA(idFin);
                    break;

                case "ajuda":
                    exibirComandos();
                    break;

                case "sair":
                    executando = false;
                    sistema.finalizaTodosSHA();
                    break;

                default:
                    System.out.println("Comando não reconhecido. Digite 'ajuda' para ver a lista de comandos.");
            }
        }

        sc.close();
        System.out.println("Programa encerrado.");
    }

    private static void exibirComandos() 
    {
        System.out.println("""
            -----------------------------------------
            COMANDOS DISPONÍVEIS:
            -----------------------------------------
            criar <id>                            - Cria hidrômetro com config padrão
            config <id> <dE> <vFA> <tS>           - Cria hidrômetro lendo/atualizando JSON
            modificar <id> <dE> <vFA> <tS>        - Modifica vazão de um hidrômetro ativo
            imagem <id> <on/off>                  - Habilita/desabilita geração de imagem
            finalizar <id>                        - Finaliza uma simulação específica
            ajuda                                 - Exibe esta tabela novamente
            sair                                  - Finaliza tudo e encerra o programa
            -----------------------------------------
        """);
    }
}
