package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class DataTransactionFrame extends BaseFrame {

    public DataTransactionFrame() {
        super("data");
        setTitle("Data Transaksi");
    }

    @Override
    protected JPanel createContent() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Data Transaksi Content"));
        return panel;
    }
}
