package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class ReportFrame extends BaseFrame {

    public ReportFrame() {
        super("laporan");
        setTitle("Laporan");
    }

    @Override
    protected JPanel createContent() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Laporan Content"));
        return panel;
    }
}
