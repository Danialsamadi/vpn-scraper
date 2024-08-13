package src;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            VPNConfigScraperFrame frame = new VPNConfigScraperFrame();
            frame.setVisible(true);
        });
    }
}