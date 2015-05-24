package pl.matik;

import javax.swing.*;

public class Main {


    public static void main(String[] args)  {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Gui myGui = new Gui();
                myGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                myGui.centreWindow(myGui);
                myGui.setResizable(false);
                myGui.setVisible(true);
            }
        });
    }
}
