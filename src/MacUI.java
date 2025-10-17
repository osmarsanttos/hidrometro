package src;

import javax.swing.JOptionPane;

public class MacUI implements Interface 
{

    @Override
    public void exibirMensagem(String mensagem) 
    {
        JOptionPane.showMessageDialog(null, mensagem, "Mensagem - macOS", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public String lerEntrada(String mensagem) 
    {
        return JOptionPane.showInputDialog(null, mensagem, "Entrada de Dados - macOS", JOptionPane.PLAIN_MESSAGE);
    }
}
