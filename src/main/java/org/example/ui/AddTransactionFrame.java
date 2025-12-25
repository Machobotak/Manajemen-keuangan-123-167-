package org.example.ui;

import org.example.finance.Transaction;
import org.example.finance.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddTransactionFrame extends BaseFrame {

    private JTextField txtDate;
    private JComboBox<String> cbType;
    private JTextField txtCategory;
    private JTextField txtAmount;
    private JTextArea txtNote;

    private boolean editMode = false;
    private Transaction oldTransaction;

    // ================= CONSTRUCTOR ADD =================
    public AddTransactionFrame() {
        super("tambah");
        setTitle("Tambah Transaksi");
        initContent();
    }

    // ================= CONSTRUCTOR EDIT =================
    public AddTransactionFrame(Transaction t) {
        super("tambah");
        this.editMode = true;
        this.oldTransaction = t;
        setTitle("Edit Transaksi");
        initContent();
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
        panel.setMaximumSize(new Dimension(520, 500));

        // ---- INIT FIELD (WAJIB DULU) ----
        txtDate = new JTextField(LocalDate.now().toString());
        txtCategory = new JTextField();
        txtAmount = new JTextField();
        cbType = new JComboBox<>(new String[]{"IN", "OUT"});

        txtNote = new JTextArea(3, 20);
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);

        // ---- STYLE ----
        styleRoundedField(txtDate);
        styleRoundedField(txtCategory);
        styleRoundedField(txtAmount);
        styleRoundedComboBox(cbType);
        styleRoundedArea(txtNote);

        // ---- ISI DATA EDIT (SETELAH FIELD ADA) ----
        if (editMode && oldTransaction != null) {
            txtDate.setText(oldTransaction.getDate().toString());
            cbType.setSelectedItem(oldTransaction.getType());
            txtCategory.setText(oldTransaction.getCategory());
            txtAmount.setText(String.valueOf(oldTransaction.getAmount()));
            txtNote.setText(oldTransaction.getNote());
        }

        JButton btnSave = new JButton(editMode ? "Simpan Perubahan" : "Simpan Transaksi");
        styleGreenRoundedButton(btnSave);
        btnSave.addActionListener(e -> saveTransaction());

        panel.add(input("Tanggal (YYYY-MM-DD)", txtDate));
        panel.add(input("Tipe Transaksi", cbType));
        panel.add(input("Kategori", txtCategory));
        panel.add(input("Jumlah", txtAmount));
        panel.add(inputArea("Catatan", txtNote));

        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(btnSave);

        return panel;
    }

    // ================= SAVE =================
    private void saveTransaction() {
        try {
            Transaction t = new Transaction(
                    LocalDate.parse(txtDate.getText()),
                    cbType.getSelectedItem().toString(),
                    txtCategory.getText(),
                    Double.parseDouble(txtAmount.getText()),
                    txtNote.getText()
            );

            if (editMode) {
                TransactionService.updateTransaction(oldTransaction, t);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil diubah");
            } else {
                TransactionService.addTransaction(t);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan");
            }

            new DataTransactionFrame().setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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
        scroll.setMaximumSize(new Dimension(420, 90));
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

    private void styleGreenRoundedButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(34, 166, 112));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

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
