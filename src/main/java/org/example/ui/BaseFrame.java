package org.example.ui;

import org.example.Login.Session;


import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public abstract class BaseFrame extends JFrame {

    protected NavButton btnDashboard;
    protected NavButton btnTambah;
    protected NavButton btnData;

    public BaseFrame(String activeMenu) {
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopBar(activeMenu), BorderLayout.NORTH);
    }

    // ================= TOP BAR =================
    private JPanel createTopBar(String activeMenu) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 166, 112));
        panel.setPreferredSize(new Dimension(0, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        menuPanel.setOpaque(false);

        btnDashboard = new NavButton("Dashboard");
        btnTambah = new NavButton("Tambah Transaksi");
        btnData = new NavButton("Data Transaksi");

        menuPanel.add(btnDashboard);
        menuPanel.add(btnTambah);
        menuPanel.add(btnData);

        setActiveMenu(activeMenu);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        userPanel.setOpaque(false);

        JLabel user = new JLabel(Session.currentUser);
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnLogout = new JButton("Logout");
        styleLogoutButton(btnLogout);
        btnLogout.addActionListener(e -> {
            Session.currentUser = null;
            dispose();
            new AuthFrame().setVisible(true);
        });

        userPanel.add(user);
        userPanel.add(btnLogout);

        panel.add(menuPanel, BorderLayout.WEST);
        panel.add(userPanel, BorderLayout.EAST);

        // NAVIGATION
        btnDashboard.addActionListener(e -> switchFrame(new DashboardFrame()));
        btnTambah.addActionListener(e -> switchFrame(new AddTransactionFrame()));
        btnData.addActionListener(e -> switchFrame(new DataTransactionFrame()));

        return panel;
    }

    protected void switchFrame(JFrame frame) {
        frame.setVisible(true);
        dispose();
    }

    protected abstract JPanel createContent();

    // ================= ACTIVE MENU =================
    private void setActiveMenu(String menu) {
        btnDashboard.setActive(menu.equals("dashboard"));
        btnTambah.setActive(menu.equals("tambah"));
        btnData.setActive(menu.equals("data"));
    }

    // ================= NAV BUTTON =================
    protected class NavButton extends JButton {

        private boolean active = false;

        public NavButton(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        public void setActive(boolean active) {
            this.active = active;
            setFont(new Font("Segoe UI",
                    active ? Font.BOLD : Font.PLAIN,
                    13));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (active) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(
                        10,
                        getHeight() - 5,
                        getWidth() - 20,
                        3,
                        10,
                        10
                );
            }
        }
    }

    protected void initContent() {
        add(createContent(), BorderLayout.CENTER);
    }

    // ================= LOGOUT STYLE =================
    private void styleLogoutButton(JButton btn) {
        btn.setBackground(new Color(220, 53, 69));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(90, 35));

        btn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(
                        0, 0,
                        c.getWidth(), c.getHeight(),
                        30, 30
                );
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }
}
