/*
 * Name: Jinsoo Choi
 * NetID: jinsoo89
 */

import javax.swing.*;

/*
 * the main program to run the GUI for IISystem
 */
public class IISystemGUI {
    private static IISystem iis;
    private static JFrame iisframe;

    // main function to run the program
    public static void main(String[] args) throws Exception {
        iis = new IISystem();
        iisframe = new IISystemFrame(iis);

        iisframe.setLocationRelativeTo(null);
        iisframe.setVisible(true);
    }
}
