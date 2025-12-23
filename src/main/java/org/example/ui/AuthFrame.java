package org.example.ui;

import org.example.Login.UserService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class AuthFrame extends JFrame {

    // ===== FIELD GLOBAL (BIAR BISA DI-CLEAR) =====
    private JTextField loginUsername;
    private JPasswordField loginPassword;

    private JTextField registerUsername;
    private JPasswordField registerPassword;

    private CardLayout cardLayout;
    private JPanel formPanel;

    public AuthFrame() {
        setTitle("Login & Register");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // ================= PANEL KIRI =================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(34, 166, 112));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login with your personal info");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(welcome);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalGlue());

        // ================= PANEL KANAN =================
        cardLayout = new CardLayout();
        formPanel = new JPanel(cardLayout);

        formPanel.add(createLoginPanel(), "login");
        formPanel.add(createRegisterPanel(), "register");

        add(leftPanel);
        add(formPanel);

        cardLayout.show(formPanel, "login");
    }

    // ================= LOGIN PANEL =================
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(34, 166, 112));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginUsername = new JTextField();
        loginPassword = new JPasswordField();
        styleRoundedField(loginUsername);
        styleRoundedField(loginPassword);

        JButton btnLogin = new JButton("LOGIN");
        styleGreenButton(btnLogin);

        btnLogin.addActionListener(e -> {
            boolean success = UserService.login(
                    loginUsername.getText(),
                    new String(loginPassword.getPassword())
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Login berhasil!" : "Username atau password salah");

            if (success) {
                clearLoginField();
                // TODO: buka DashboardFrame
                dispose();
            }
        });

        JButton btnRegister = new JButton("REGISTER");
        styleRoundedOutlineButton(btnRegister);
        btnRegister.addActionListener(e -> {
            clearLoginField();
            cardLayout.show(formPanel, "register");
        });

        JPanel buttonGroup = new JPanel();
        buttonGroup.setBackground(Color.WHITE);
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));

        buttonGroup.add(btnLogin);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(btnRegister);

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(inputField("Username", loginUsername));
        panel.add(inputField("Password", loginPassword));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(buttonGroup);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ================= REGISTER PANEL =================
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(34, 166, 112));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerUsername = new JTextField();
        registerPassword = new JPasswordField();
        styleRoundedField(registerUsername);
        styleRoundedField(registerPassword);

        JButton btnSignUp = new JButton("SIGN UP");
        styleGreenButton(btnSignUp);

        btnSignUp.addActionListener(e -> {
            try {
                boolean success = UserService.register(
                        registerUsername.getText(),
                        new String(registerPassword.getPassword())
                );

                JOptionPane.showMessageDialog(this,
                        success ? "Register berhasil!" : "Username sudah digunakan");

                if (success) {
                    clearRegisterField();
                    cardLayout.show(formPanel, "login");
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        JButton btnToLogin = new JButton("Already have an account? Sign In");
        styleLinkButton(btnToLogin);
        btnToLogin.addActionListener(e -> {
            clearRegisterField();
            cardLayout.show(formPanel, "login");
        });

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(inputField("Username", registerUsername));
        panel.add(inputField("Password", registerPassword));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(btnSignUp);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnToLogin);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ================= CLEAR FIELD =================
    private void clearLoginField() {
        loginUsername.setText("");
        loginPassword.setText("");
    }

    private void clearRegisterField() {
        registerUsername.setText("");
        registerPassword.setText("");
    }

    // ================= HELPER =================
    private JPanel inputField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(300, 45));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // ================= STYLING =================
    private void styleGreenButton(JButton btn) {
        btn.setBackground(new Color(34, 166, 112));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);

                super.paint(g2, c); // ✅ FIX DI SINI
                g2.dispose();
            }
        });
    }

    private void styleGreenOutlineButton(JButton btn) {
        btn.setForeground(new Color(34, 166, 112));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(
                new Color(34, 166, 112), 2, true));
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleLinkButton(JButton btn) {
        btn.setBorder(null);
        btn.setForeground(new Color(34, 166, 112));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleRoundedOutlineButton(JButton btn) {
        btn.setForeground(new Color(34, 166, 112));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                // outline rounded
                g2.setColor(new Color(34, 166, 112));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(
                        1, 1,
                        c.getWidth() - 3,
                        c.getHeight() - 3,
                        30, 30
                );

                super.paint(g2, c);
                g2.dispose();
            }
        });

    }
    private void styleRoundedField(JTextField field) {
        field.setBorder(new RoundedBorder(
                20,
                new Color(34, 166, 112)
        ));
        field.setMaximumSize(new Dimension(300, 35));
    }


}
