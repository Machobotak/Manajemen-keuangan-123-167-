package org.example.finance;

public class DashboardService {

    // Total pemasukan
    public static double getTotalPemasukan() {
        double total = 0;
        for (Transaction t : TransactionService.loadTransactions()) {
            if ("IN".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // Total pengeluaran
    public static double getTotalPengeluaran() {
        double total = 0;
        for (Transaction t : TransactionService.loadTransactions()) {
            if ("OUT".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // Saldo saat ini
    public static double getSaldo() {
        return getTotalPemasukan() - getTotalPengeluaran();
    }
}
