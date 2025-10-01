package src;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;


// Definição da classe para leitura do arquivo .json de configuração 
public class Config 
{
    private static final Gson GSON = new Gson();

    public static HidrometroConfig lerConfig(String caminho) 
    {
        try (FileReader fr = new FileReader(caminho)) 
        {
            return GSON.fromJson(fr, HidrometroConfig.class);
        } 

        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
}