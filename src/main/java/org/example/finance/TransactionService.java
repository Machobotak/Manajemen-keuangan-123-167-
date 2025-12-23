package org.example.finance;

import org.example.Login.Session;
import org.example.Login.UserService;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private static String getFilePath() {
        return UserService.getTransactionFile();
    }

    // ================= LOAD =================
    public static List<Transaction> loadTransactions() {
        List<Transaction> list = new ArrayList<>();

        if (Session.currentUser == null) return list;

        File file = new File(getFilePath());
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length == 5) {
                    list.add(new Transaction(
                            LocalDate.parse(d[0]),
                            d[1],
                            d[2],
                            Double.parseDouble(d[3]),
                            d[4]
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal membaca transaksi.");
        }

        return list;
    }

    // ================= ADD =================
    public static void addTransaction(Transaction t) {
        if (Session.currentUser == null) return;

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(getFilePath(), true))) {

            bw.write(
                    t.getDate() + "," +
                            t.getType() + "," +
                            t.getCategory() + "," +
                            t.getAmount() + "," +
                            t.getNote()
            );
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Gagal menyimpan transaksi.");
        }
    }
}
