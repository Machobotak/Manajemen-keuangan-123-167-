package org.example.ui;

import org.example.finance.ChartService;
import org.example.finance.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardFrame extends BaseFrame {

    public DashboardFrame() {
        super("dashboard"); // menu aktif
        setTitle("Dashboard Keuangan");
    }

    // ================= MAIN CONTENT =================
    @Override
    protected JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 246, 250));
        panel.setLayout(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        panel.add(createSummaryCards(), BorderLayout.NORTH);
        panel.add(createChartPanel(), BorderLayout.CENTER);

        return panel;
    }

    // ================= SUMMARY CARDS =================
    private JPanel createSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);

        panel.add(summaryCard("Saldo", DashboardService.getSaldo()));
        panel.add(summaryCard("Pemasukan", DashboardService.getTotalPemasukan()));
        panel.add(summaryCard("Pengeluaran", DashboardService.getTotalPengeluaran()));

        return panel;
    }

    private JPanel summaryCard(String title, double value) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.DARK_GRAY);

        JLabel lblValue = new JLabel("Rp " + value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(new Color(34, 166, 112));

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);

        return card;
    }

    // ================= CHART =================
    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Grafik Pemasukan vs Pengeluaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        panel.add(title, BorderLayout.NORTH);
        panel.add(new BarChartPanel(), BorderLayout.CENTER);

        return panel;
    }

    // ================= BAR CHART =================
    private static class BarChartPanel extends JPanel {

        Map<String, Double> data = ChartService.getIncomeExpenseChart();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int barWidth = 100;
            int maxBarHeight = height - 100;

            double maxValue = data.values()
                    .stream()
                    .max(Double::compare)
                    .orElse(1.0);

            int x = width / 2 - barWidth - 40;

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int)
                        ((entry.getValue() / maxValue) * maxBarHeight);

                g2.setColor(new Color(34, 166, 112));
                g2.fillRoundRect(
                        x,
                        height - barHeight - 40,
                        barWidth,
                        barHeight,
                        20, 20
                );

                g2.setColor(Color.BLACK);
                g2.drawString(entry.getKey(), x + 15, height - 15);
                g2.drawString(
                        "Rp " + entry.getValue(),
                        x + 5,
                        height - barHeight - 50
                );

                x += barWidth + 80;
            }
        }
    }
}
