package src;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ButtonHandler {
    public static void addConfigToPanel(String config, JPanel configPanel) {
        JPanel configContainer = new JPanel(new BorderLayout());

        JButton copyButton = new JButton("Copy");
        JTextField configField = new JTextField(config);
        configField.setEditable(false);

        copyButton.addActionListener(e -> {
            copyToClipboard(config);
            JOptionPane.showMessageDialog(null, "Config copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        configContainer.add(copyButton, BorderLayout.WEST);
        configContainer.add(configField, BorderLayout.CENTER);

        configPanel.add(configContainer);
    }

    private static void copyToClipboard(String config) {
        StringSelection stringSelection = new StringSelection(config);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }
}
