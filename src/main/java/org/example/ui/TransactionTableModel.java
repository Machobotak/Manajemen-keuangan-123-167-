package org.example.ui;

import org.example.finance.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {

    private final String[] columns = {
            "Tanggal", "Jenis", "Kategori", "Nominal", "Keterangan"
    };

    private final List<Transaction> data;

    public TransactionTableModel(List<Transaction> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction t = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> t.getDate();
            case 1 -> t.getType();
            case 2 -> t.getCategory();
            case 3 -> t.getAmount();
            case 4 -> t.getNote();
            default -> null;
        };
    }

    public Transaction getAt(int row) {
        return data.get(row);
    }
}
