package org.example.ui;

import javax.swing.*;

import java.awt.*;


public class NavButton extends JButton {

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
        setFont(new Font(
                "Segoe UI",
                active ? Font.BOLD : Font.PLAIN,
                13
        ));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (active) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

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
