package org.example.ui;

import org.example.finance.Transaction;
import org.example.finance.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransactionFrame extends BaseFrame {

    private JTextField txtDate;
    private JComboBox<String> cbType;
    private JComboBox<String> cbCategory;
    private JTextField txtAmount;
    private JTextArea txtNote;

    private boolean editMode = false;
    private Transaction oldTransaction;

    // ===== KATEGORI =====
    private static final String[] CATEGORY_IN = {
            "-- Pilih Kategori --",
            "Gaji",
            "Bonus",
            "Hadiah",
            "Penjualan",
            "Lainnya"
    };

    private static final String[] CATEGORY_OUT = {
            "-- Pilih Kategori --",
            "Makan",
            "Transport",
            "Belanja",
            "Hiburan",
            "Pendidikan",
            "Kesehatan",
            "Lainnya"
    };

    // ================= CONSTRUCTOR ADD =================
    public AddTransactionFrame() {
        super("tambah");
        setTitle("Tambah Transaksi");
        initContent();
    }

    // ================= CONSTRUCTOR EDIT =================
    public AddTransactionFrame(Transaction t) {
        super("edit");
        this.editMode = true;
        this.oldTransaction = t;
        setTitle("Edit Transaksi");
        initContent();   // ⬅️ INI WAJIB
    }

    // ================= CONTENT =================
    @Override
    protected JPanel createContent() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(245, 246, 250));
        container.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(editMode ? "Edit Transaksi" : "Tambah Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        container.add(title, BorderLayout.NORTH);
        container.add(createForm(), BorderLayout.CENTER);

        return container;
    }

    // ================= FORM =================
    private JPanel createForm() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panel.setMaximumSize(new Dimension(520, 520));

        txtDate = new JTextField(LocalDate.now().toString());
        cbType = new JComboBox<>(new String[]{"IN", "OUT"});
        cbCategory = new JComboBox<>();
        txtAmount = new JTextField();

        txtNote = new JTextArea(3, 20);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);

        // ===== STYLE =====
        styleRoundedField(txtDate);
        styleRoundedComboBox(cbType);
        styleRoundedComboBox(cbCategory);
        styleRoundedField(txtAmount);
        styleRoundedArea(txtNote);

        // ===== LISTENER TYPE =====
        cbType.addActionListener(e -> updateCategoryByType());

        // ===== INIT CATEGORY =====
        updateCategoryByType();

        // ===== EDIT MODE =====
        if (editMode && oldTransaction != null) {
            txtDate.setText(oldTransaction.getDate().toString());
            cbType.setSelectedItem(oldTransaction.getType());
            updateCategoryByType();
            cbCategory.setSelectedItem(oldTransaction.getCategory());
            txtAmount.setText(String.valueOf(oldTransaction.getAmount()));
            txtNote.setText(oldTransaction.getNote());
        }

        JButton btnSave = new JButton(editMode ? "Simpan Perubahan" : "Simpan Transaksi");
        JButton btnBatal = new JButton("Batal");
        styleGreenRoundedButton(btnSave);
        styleRedRoundedButton(btnBatal);
        btnSave.addActionListener(e -> saveTransaction());
        btnBatal.addActionListener(e -> {
            new DataTransactionFrame().setVisible(true);
            dispose();
        });

        panel.add(input("Tanggal (YYYY-MM-DD)", txtDate));
        panel.add(input("Tipe Transaksi", cbType));
        panel.add(input("Kategori", cbCategory));
        panel.add(input("Jumlah", txtAmount));
        panel.add(inputArea("Catatan", txtNote));

        panel.add(Box.createRigidArea(new Dimension(0, 38)));

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.WHITE);
        if (!editMode) {
            // ===== MODE TAMBAH =====
            actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            actionPanel.add(btnSave);

        } else {
            // ===== MODE EDIT =====
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));

            actionPanel.add(Box.createHorizontalGlue());
            actionPanel.add(btnBatal);
            actionPanel.add(Box.createHorizontalStrut(20));
            actionPanel.add(btnSave);
            btnSave.setPreferredSize(new Dimension(220, 42));
            btnBatal.setPreferredSize(new Dimension(160, 42));
            actionPanel.add(Box.createHorizontalGlue());
        }

        panel.add(actionPanel);

        return panel;
    }

    // ================= UPDATE CATEGORY =================
    private void updateCategoryByType() {
        cbCategory.removeAllItems();

        String type = cbType.getSelectedItem().toString();
        String[] categories = type.equals("IN") ? CATEGORY_IN : CATEGORY_OUT;

        for (String c : categories) {
            cbCategory.addItem(c);
        }

        cbCategory.setSelectedIndex(0);
    }

    // ================= SAVE =================
    private void saveTransaction() {
        try {
            if (cbCategory.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Kategori harus dipilih");
                return;
            }

            if (txtAmount.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Jumlah harus diisi");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(txtAmount.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka");
                return;
            }

            Transaction t = new Transaction(
                    LocalDate.parse(txtDate.getText()),
                    cbType.getSelectedItem().toString(),
                    cbCategory.getSelectedItem().toString(),
                    amount,
                    txtNote.getText()
            );

            if (editMode) {
                TransactionService.updateTransaction(oldTransaction, t);
            } else {
                TransactionService.addTransaction(t);
            }

            new DataTransactionFrame().setVisible(true);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Periksa kembali input");
        }
    }

    // ================= INPUT HELPERS =================
    private JPanel input(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(420, 65));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel inputArea(String label, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(420, 110));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new RoundedBorder(20, new Color(34, 166, 112)));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ================= STYLING =================
    private void styleRoundedField(JTextField field) {
        field.setBorder(new RoundedBorder(20, new Color(34, 166, 112)));
        field.setMaximumSize(new Dimension(420, 36));
    }

    private void styleRoundedArea(JTextArea area) {
        area.setBorder(null);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void styleRoundedComboBox(JComboBox<?> combo) {
        combo.setBorder(new RoundedBorder(20, new Color(34, 166, 112)));
        combo.setMaximumSize(new Dimension(420, 36));
        combo.setBackground(Color.WHITE);
    }

    private void styleRedRoundedButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(220, 53, 69));
        styleRoundedButton(btn);
    }

    private void styleGreenRoundedButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(34, 166, 112));
        styleRoundedButton(btn);
    }

    private void styleRoundedButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(
                        0, 0,
                        c.getWidth(),
                        c.getHeight(),
                        30, 30
                );
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }
}
