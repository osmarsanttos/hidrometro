package src;

public class UIBridge 
{

    private Interface implementor;

    public UIBridge(Interface implementor) 
    {
        this.implementor = implementor;
    }

    public void mostrarMensagem(String mensagem) 
    {
        implementor.exibirMensagem(mensagem);
    }

    public String receberEntrada(String mensagem) 
    {
        return implementor.lerEntrada(mensagem);
    }
}
