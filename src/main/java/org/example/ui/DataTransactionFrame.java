package org.example.ui;

import org.example.finance.Transaction;
import org.example.finance.TransactionService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.List;

public class DataTransactionFrame extends BaseFrame {

    private JTable table;
    private TransactionTableModel tableModel;
    private TableRowSorter<TransactionTableModel> sorter;
    private JTextField txtSearch;

    public DataTransactionFrame() {
        super("data");
        setTitle("Data Transaksi");
        initContent();
    }

    @Override
    protected JPanel createContent() {
        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setBackground(new Color(245, 246, 250));
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Data Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        main.add(title, BorderLayout.NORTH);
        main.add(createBody(), BorderLayout.CENTER);
        main.add(createBottomBar(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(15, 15));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        body.add(createTopBar(), BorderLayout.NORTH);
        body.add(createTableSection(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Cari:");
        txtSearch = new JTextField(20);
        txtSearch.setBorder(new RoundedBorder(20, new Color(34, 166, 112)));
        txtSearch.setPreferredSize(new Dimension(220, 36));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String key = txtSearch.getText().trim();

                if (key.isEmpty()) {
                    sorter.setRowFilter(null);
                    return;
                }

                sorter.setRowFilter(
                        RowFilter.regexFilter("(?i)" + Pattern.quote(key))
                );
            }

            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });

        panel.add(lbl);
        panel.add(txtSearch);

        return panel;
    }

    private JScrollPane createTableSection() {
        List<Transaction> list = TransactionService.loadTransactions();

        tableModel = new TransactionTableModel(list);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(220, 245, 235));

        sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(0, Comparator.comparing(o -> (LocalDate) o));
        sorter.setComparator(3, Comparator.comparingDouble(o -> (Double) o));
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new RoundedBorder(20, new Color(230, 230, 230)));

        return scroll;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(245, 246, 250));

        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnBack = new JButton("Kembali");

        styleRoundedButton(btnEdit, new Color(255, 193, 7));   // kuning
        styleRoundedButton(btnDelete, new Color(220, 53, 69)); // merah
        styleRoundedButton(btnBack, new Color(108, 117, 125)); // abu

        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnBack.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnBack);

        return panel;
    }

    private void editSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi terlebih dahulu");
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        Transaction t = tableModel.getAt(modelRow);

        new AddTransactionFrame(t).setVisible(true);
        dispose();
    }

    private void deleteSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi terlebih dahulu");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin hapus transaksi?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int modelRow = table.convertRowIndexToModel(viewRow);
        Transaction t = tableModel.getAt(modelRow);

        TransactionService.deleteTransaction(t);
        refreshTable();
    }

    private void refreshTable() {
        tableModel = new TransactionTableModel(
                TransactionService.loadTransactions()
        );

        sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(0, Comparator.comparing(o -> (LocalDate) o));
        sorter.setComparator(3, Comparator.comparingDouble(o -> (Double) o));

        table.setModel(tableModel);
        table.setRowSorter(sorter);
    }
}
