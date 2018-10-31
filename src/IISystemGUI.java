import javax.swing.*;

public class IISystemGUI {
    private static IISystem iis;
    private static JFrame iisframe;

    public static void main(String[] args) throws Exception {
        iis = new IISystem();
        iisframe = new IISystemFrame(iis);

        iisframe.setLocationRelativeTo(null);
        iisframe.setVisible(true);
    }
}
