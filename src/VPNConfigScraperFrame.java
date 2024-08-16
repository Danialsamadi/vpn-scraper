package src;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VPNConfigScraperFrame extends JFrame {
    private static final int INITIAL_COUNTDOWN_SECONDS = 600;

    private JTextField urlField;
    private JButton fetchButton;
    private JTabbedPane tabbedPane;
    private ScheduledExecutorService scheduler;
    private int countdownSeconds;

    public VPNConfigScraperFrame() {
        setupFrame();
        initializeComponents();
        setupScheduler();
    }

    private void setupFrame() {
        setTitle("VPN Config Scraper");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createMenuBar();
    }

    private void initializeComponents() {
        countdownSeconds = INITIAL_COUNTDOWN_SECONDS;

        urlField = new JTextField("https://raw.githubusercontent.com/ALIILAPRO/v2rayNG-Config/main/server.txt");
        fetchButton = new JButton("Fetch and Show Configs (" + formatTime(countdownSeconds) + ")");
        fetchButton.setPreferredSize(new Dimension(300, 40));
        fetchButton.setMinimumSize(new Dimension(300, 40));  // Prevent shrinking below this size

        tabbedPane = new JTabbedPane();
        JPanel vmessPanel = createProtocolPanel("VMess");
        JPanel vlessPanel = createProtocolPanel("VLess");
        JPanel trojanPanel = createProtocolPanel("Trojan");
        JPanel ssPanel = createProtocolPanel("Shadowsocks");

        tabbedPane.addTab("VMess", new JScrollPane(vmessPanel));
        tabbedPane.addTab("VLess", new JScrollPane(vlessPanel));
        tabbedPane.addTab("Trojan", new JScrollPane(trojanPanel));
        tabbedPane.addTab("Shadowsocks", new JScrollPane(ssPanel));

        fetchButton.addActionListener(e -> {
            fetchAndShowConfigs();
            resetCountdown();
        });

        // Use GridBagLayout for flexible layout management
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tabbedPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(fetchButton, gbc);

        add(mainPanel);

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
                    JPanel panel = (JPanel) scrollPane.getViewport().getView();
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
    }

    private JPanel createProtocolPanel(String protocol) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName(protocol);
        panel.setPreferredSize(new Dimension(700, 500));
        panel.setMinimumSize(new Dimension(700, 500));  // Prevent shrinking below this size
        return panel;
    }

    private void setupScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            fetchAndShowConfigs();
            resetCountdown();
        }, 0, 10, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(this::updateCountdown, 1, 1, TimeUnit.SECONDS);
    }

    private void fetchAndShowConfigs() {
        String url = urlField.getText();
        try {
            List<String> vpnConfigs = Fetching.fetchServerTxt(url);
            updateConfigPanels(vpnConfigs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateConfigPanels(List<String> vpnConfigs) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
                JPanel panel = (JPanel) scrollPane.getViewport().getView();
                panel.removeAll();
            }

            int[] protocolIndices = new int[4];
            for (int i = 0; i < protocolIndices.length; i++) {
                protocolIndices[i] = 1;
            }

            for (String config : vpnConfigs) {
                JPanel panel = getProtocolPanel(config);
                if (panel != null) {
                    int index = protocolIndices[getProtocolIndex(panel)];
                    addConfigToPanel(config, panel, index);
                    protocolIndices[getProtocolIndex(panel)]++;
                }
            }

            updatePanelWithMessage((JPanel) ((JScrollPane) tabbedPane.getComponentAt(0)).getViewport().getView(), "No VMess configurations found.");
            updatePanelWithMessage((JPanel) ((JScrollPane) tabbedPane.getComponentAt(1)).getViewport().getView(), "No VLess configurations found.");
            updatePanelWithMessage((JPanel) ((JScrollPane) tabbedPane.getComponentAt(2)).getViewport().getView(), "No Trojan configurations found.");
            updatePanelWithMessage((JPanel) ((JScrollPane) tabbedPane.getComponentAt(3)).getViewport().getView(), "No Shadowsocks configurations found.");

            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
                JPanel panel = (JPanel) scrollPane.getViewport().getView();
                panel.revalidate();
                panel.repaint();
            }
        });
    }

    private void updatePanelWithMessage(JPanel panel, String message) {
        if (panel.getComponentCount() == 0) {
            panel.add(new JLabel(message));
        }
    }

    private JPanel getProtocolPanel(String config) {
        if (config.matches("(?i).*vmess://.*")) {
            return (JPanel) ((JScrollPane) tabbedPane.getComponentAt(0)).getViewport().getView();
        } else if (config.matches("(?i).*vless://.*")) {
            return (JPanel) ((JScrollPane) tabbedPane.getComponentAt(1)).getViewport().getView();
        } else if (config.matches("(?i).*trojan://.*")) {
            return (JPanel) ((JScrollPane) tabbedPane.getComponentAt(2)).getViewport().getView();
        } else if (config.matches("(?i).*ss://.*")) {
            return (JPanel) ((JScrollPane) tabbedPane.getComponentAt(3)).getViewport().getView();
        }
        return null;
    }

    private int getProtocolIndex(JPanel panel) {
        int index = -1;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (panel == ((JScrollPane) tabbedPane.getComponentAt(i)).getViewport().getView()) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void addConfigToPanel(String config, JPanel panel, int index) {
        JPanel configContainer = new JPanel(new BorderLayout());

        JButton configButton = new JButton(index + ". " + config);
        configButton.setPreferredSize(new Dimension(600, 30));
        configButton.setMinimumSize(new Dimension(600, 30));  // Prevent shrinking below this size
        configButton.setHorizontalAlignment(SwingConstants.LEFT);

        JButton copyButton = new JButton("Copy");
        copyButton.setPreferredSize(new Dimension(100, 30));
        copyButton.setMinimumSize(new Dimension(100, 30));  // Prevent shrinking below this size

        copyButton.addActionListener(e -> {
            copyToClipboard(config);
            JOptionPane.showMessageDialog(this, "Config copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        configContainer.add(configButton, BorderLayout.CENTER);
        configContainer.add(copyButton, BorderLayout.WEST);

        panel.add(configContainer);
    }

    private void copyToClipboard(String config) {
        StringSelection stringSelection = new StringSelection(config);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    private void updateCountdown() {
        SwingUtilities.invokeLater(() -> {
            countdownSeconds--;
            fetchButton.setText("Fetch and Show Configs (" + formatTime(countdownSeconds) + ")");
            if (countdownSeconds <= 0) {
                resetCountdown();
            }
        });
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void resetCountdown() {
        countdownSeconds = INITIAL_COUNTDOWN_SECONDS;
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu configsMenu = new JMenu("Configs");
        JMenuItem fetchConfigsItem = new JMenuItem("Fetch Now");
        fetchConfigsItem.addActionListener(e -> {
            fetchAndShowConfigs();
            if (countdownSeconds <= 0) {
                resetCountdown();
            }
        });
        configsMenu.add(fetchConfigsItem);

        JMenu aboutMenu = new JMenu("About Me");
        JMenuItem aboutMeItem = new JMenuItem("About the Developer");
        aboutMeItem.addActionListener(e -> {
            AboutMeDialog dialog = new AboutMeDialog(this);
            dialog.setVisible(true);
        });
        aboutMenu.add(aboutMeItem);

        menuBar.add(configsMenu);
        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }
}
