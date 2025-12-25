package org.example.ui;

import org.example.finance.ChartService;
import org.example.finance.DashboardService;
import org.example.finance.Transaction;
import org.example.finance.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends BaseFrame {

    public DashboardFrame() {
        super("dashboard");
        setTitle("Dashboard Keuangan");
        initContent();
    }

    // ================= MAIN CONTENT =================
    @Override
    protected JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 246, 250));
        panel.setLayout(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        panel.add(createSummaryCards(), BorderLayout.NORTH);
        panel.add(createBottomSection(), BorderLayout.CENTER);

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

        JLabel lblValue = new JLabel("Rp " + value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(new Color(34, 166, 112));

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);

        return card;
    }

    // ================= BOTTOM SECTION =================
    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);

        panel.add(createChartBox());
        panel.add(createSideInfo());

        return panel;
    }

    // ================= CHART =================
    private JPanel createChartBox() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Grafik Pemasukan vs Pengeluaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        panel.add(title, BorderLayout.NORTH);
        panel.add(new BarChartPanel(), BorderLayout.CENTER);

        return panel;
    }

    // ================= RIGHT SIDE =================
    private JPanel createSideInfo() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 20));
        panel.setOpaque(false);

        panel.add(createRecentTransactionBox());
        panel.add(createMonthlyTotalBox());

        return panel;
    }

    // ================= RECENT TRANSACTIONS =================
    private JPanel createRecentTransactionBox() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Transaksi Terakhir");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(34,166,122));
        title.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        title.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new DataTransactionFrame().setVisible(true);
                dispose();
            }

        });


        panel.add(title,BorderLayout.NORTH);
        String[] coloums = {"Tanggal","kategori","Tipe","Jumlah"};
        Object[][] data = getLastThreeTransaction();

        JTable table = new JTable(data,coloums);
        table.setEnabled(false);
        table.setRowHeight(22);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        panel.add(scrollPane,BorderLayout.CENTER);


        return panel;
    }

    // ================= MONTHLY TOTAL =================
    private JPanel createMonthlyTotalBox() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Total Transaksi Bulan Ini");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        long total = TransactionService.loadTransactions()
                .stream()
                .filter(t ->
                        YearMonth.from(t.getDate())
                                .equals(YearMonth.now()))
                .count();

        JLabel value = new JLabel(String.valueOf(total));
        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(new Color(34, 166, 112));

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(value);

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

                // WARNA
                if (entry.getKey().equalsIgnoreCase("Pengeluaran")) {
                    g2.setColor(new Color(220, 53, 69)); // merah
                } else {
                    g2.setColor(new Color(34, 166, 112)); // hijau
                }

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
    private Object[][] getLastThreeTransaction(){
        List<Transaction>list = TransactionService.loadTransactions();
        int size = Math.min(3,list.size());
        Object[][] data = new Object[size][4];
        for(int i =0;i<size;i++){
            Transaction t = list.get(list.size()-1-i);
            data[i][0]=t.getDate();
            data[i][1]=t.getCategory();
            data[i][2]=t.getType();
            data[i][3]= "Rp "+t.getAmount();
        }
        return data;
    }
}
