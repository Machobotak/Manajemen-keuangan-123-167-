package org.example.finance;

import org.example.Login.Session;
import org.example.Login.UserService;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    // ================= FILE PATH =================
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
                String[] d = line.split(",", -1);
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
            System.out.println("Gagal membaca data transaksi.");
        }

        return list;
    }

    // ================= ADD =================
    public static void addTransaction(Transaction t) {

        if (Session.currentUser == null) {
            throw new IllegalStateException("User belum login");
        }

        validateTransaction(t);

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
            throw new RuntimeException("Gagal menyimpan transaksi");
        }
    }

    // ================= DELETE =================
    public static void deleteTransaction(Transaction target) {

        if (Session.currentUser == null) {
            throw new IllegalStateException("User belum login");
        }

        List<Transaction> list = loadTransactions();
        list.removeIf(t ->
                t.getDate().equals(target.getDate()) &&
                        t.getType().equals(target.getType()) &&
                        t.getCategory().equals(target.getCategory()) &&
                        t.getAmount() == target.getAmount() &&
                        t.getNote().equals(target.getNote())
        );

        rewriteFile(list);
    }

    // ================= REWRITE FILE =================
    private static void rewriteFile(List<Transaction> list) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(getFilePath(), false))) {

            for (Transaction t : list) {
                bw.write(
                        t.getDate() + "," +
                                t.getType() + "," +
                                t.getCategory() + "," +
                                t.getAmount() + "," +
                                t.getNote()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Gagal menghapus transaksi");
        }
    }

    // ================= UPDATE =================
    public static void updateTransaction(
            Transaction oldT,
            Transaction newT
    ) {
        if (Session.currentUser == null) {
            throw new IllegalStateException("User belum login");
        }

        List<Transaction> list = loadTransactions();

        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);
            if (sameTransaction(t, oldT)) {
                list.set(i, newT);
                break;
            }
        }

        rewriteFile(list);
    }

    private static boolean sameTransaction(Transaction a, Transaction b) {
        return a.getDate().equals(b.getDate()) &&
                a.getType().equals(b.getType()) &&
                a.getCategory().equals(b.getCategory()) &&
                a.getAmount() == b.getAmount() &&
                a.getNote().equals(b.getNote());
    }



    // ================= VALIDATION =================
    private static void validateTransaction(Transaction t) {

        if (t.getDate() == null) {
            throw new IllegalArgumentException("Tanggal tidak boleh kosong");
        }

        if (!"IN".equals(t.getType()) && !"OUT".equals(t.getType())) {
            throw new IllegalArgumentException("Tipe transaksi tidak valid");
        }

        if (t.getCategory() == null || t.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori wajib diisi");
        }

        if (t.getAmount() <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0");
        }
    }
}
