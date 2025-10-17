package src;

import javax.swing.JOptionPane;

public class WindowsUI implements Interface 
{

    @Override
    public void exibirMensagem(String mensagem) 
    {
        JOptionPane.showMessageDialog(null, mensagem, "Mensagem - Windows", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String lerEntrada(String mensagem) 
    {
        return JOptionPane.showInputDialog(null, mensagem, "Entrada de Dados - Windows", JOptionPane.QUESTION_MESSAGE);
    }
}
