package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class AddTransactionFrame extends BaseFrame {

    public AddTransactionFrame() {
        super("tambah");
        setTitle("Tambah Transaksi");
    }

    @Override
    protected JPanel createContent() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Tambah Transaksi Content"));
        return panel;
    }
}
