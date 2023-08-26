package com.pepperoni.prophunt;

import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PropHuntTwoPanel extends PluginPanel implements ActionListener {
    private static final String CREATE_BUTTON_TEXT = "Create party";
    private static final String JOIN_PARTY_TEXT = "Join party";
    private static final String CREATE_PARTY_SUCCESS = "Created a new party.";
    private static final String JOIN_PARTY_SUCCESS = "Joined the party.";
    private static final String EMPTY_PARTY_ID = "You have to enter a party id.";
    private static final String INVALID_PARTY_ID = "You entered an invalid party id.";
    private static final String COPY_SUCCESS = "Copied the party id to your clipboard.";
    private static final String NO_PARTY_JOINED = "No party joined.";
    private final PropHuntTwoPlugin plugin;
    private final JLabel currentPartyLabel = new JLabel(NO_PARTY_JOINED, SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel();
    private final JLabel copySuccessLabel = new JLabel();

    private final JTextField textFieldJoinParty = new JTextField();
    private final JButton leaveJoinGroupButton = new JButton("Join Group");
    private final JButton loginLogout = new JButton("Login");
    private final Client client;

    public PropHuntTwoPanel(PropHuntTwoPlugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        gridBagConstraints.insets = new Insets(0, 0, 8, 0);

        JButton buttonCreateParty = new JButton("Create Group");
        buttonCreateParty.addActionListener(e -> {
            plugin.createGroup(plugin.getJWT());
        });

        add(buttonCreateParty, gridBagConstraints);
        gridBagConstraints.gridy++;

        leaveJoinGroupButton.addActionListener(e -> {
            if (plugin.getGroupId() == null) {
                try {
                    plugin.joinGroup(textFieldJoinParty.getText());
                } catch (UnsupportedEncodingException ex) {
                    plugin.sendPrivateMessage("Could not join group. Was that a valid ID?");
                }
            } else {
                plugin.leaveGroup();
            }
        });

        add(textFieldJoinParty, gridBagConstraints);
        gridBagConstraints.gridy++;

        add(leaveJoinGroupButton, gridBagConstraints);
        gridBagConstraints.gridy++;

        add(messageLabel, gridBagConstraints);
        gridBagConstraints.gridy++;

        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new BoxLayout(partyPanel, BoxLayout.Y_AXIS));
        partyPanel.setBorder(new LineBorder(ColorScheme.DARKER_GRAY_COLOR));

        partyPanel.addMouseListener(new MouseAdapter() {

        });

        Border border = partyPanel.getBorder();
        Border margin = new EmptyBorder(10, 10, 10, 10);

        partyPanel.setBorder(new CompoundBorder(border, margin));

        JLabel copyLabel = new JLabel("Players", SwingConstants.CENTER);
        copyLabel.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.PLAIN, 25));
        copyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel currentGroup = new JLabel("Not in a group", SwingConstants.CENTER);
        currentGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        partyPanel.add(copyLabel);
        partyPanel.add(currentGroup);
        add(partyPanel, gridBagConstraints);
        gridBagConstraints.gridy++;


        loginLogout.addActionListener(e -> {
            try {
                if (plugin.getLoggedIn() == false) {
                    plugin.login();
                } else {
                    plugin.logout();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        addPlaceholder(textFieldJoinParty, "Group ID...");
        add(textFieldJoinParty, gridBagConstraints);
        gridBagConstraints.gridy++;

        add(leaveJoinGroupButton, gridBagConstraints);
        gridBagConstraints.gridy++;

        add(loginLogout, gridBagConstraints);
        gridBagConstraints.gridy++;
    }

    private static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    public void setGroupTextField(String groupId) {
        textFieldJoinParty.setText(groupId);
    }

    public void updateLoginLogoutButton() {
        if (plugin.getLoggedIn()) {
            this.loginLogout.setText("Logout");
        } else {
            this.loginLogout.setText("Login");
        }
    }

    public void updateLeaveJoinGroupButton() {
        if(plugin.getGroupId() != null) {
            this.leaveJoinGroupButton.setText("Leave Group");
        } else {
            this.leaveJoinGroupButton.setText("Join Group");
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
