package src;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class AboutMeDialog extends JDialog {

    public AboutMeDialog(JFrame parent) {
        super(parent, "About the Developer", true);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Create a panel with GridBagLayout to center the content vertically and horizontally
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Create the label with the developer information
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Developer: Your Name<br/>"
                + "Version: 1.0<br/>"
                + "This tool fetches VPN configurations."
                + "</div></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the button to open the website
        JButton linkButton = new JButton("Visit my GitHub Profile");
        linkButton.addActionListener(e -> openWebpage("https://github.com/Danialsamadi"));

        // Add the label and button to the content panel
        contentPanel.add(infoLabel, gbc);
        contentPanel.add(linkButton, gbc);

        // Add the content panel to the center of the dialog
        add(contentPanel, BorderLayout.CENTER);

        // Add a close button at the bottom
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel closePanel = new JPanel();
        closePanel.add(closeButton);

        add(closePanel, BorderLayout.SOUTH);
    }

    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
