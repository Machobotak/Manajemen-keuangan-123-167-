# Sistem Manajemen Keuangan

Aplikasi **Sistem Manajemen Keuangan** berbasis **Java Swing** yang digunakan untuk mencatat dan mengelola transaksi keuangan pribadi.  
Aplikasi ini mendukung fitur login, pencatatan transaksi pemasukan dan pengeluaran, serta visualisasi data dalam bentuk dashboard.

---

## 📌 Fitur Utama

- **Login & Register**
  - Autentikasi pengguna menggunakan data yang disimpan dalam file CSV
- **Dashboard**
  - Menampilkan saldo saat ini
  - Total pemasukan
  - Total pengeluaran
  - Grafik pemasukan vs pengeluaran
  - Ringkasan transaksi terakhir
- **Tambah Transaksi**
  - Input transaksi pemasukan (IN) dan pengeluaran (OUT)
  - Kategori dinamis sesuai jenis transaksi
- **Edit & Hapus Transaksi**
  - Mengubah data transaksi yang sudah tersimpan
  - Menghapus transaksi tertentu
- **Data Transaksi**
  - Menampilkan seluruh transaksi dalam bentuk tabel
  - Fitur pencarian (search)
  - Sorting data berdasarkan kolom
- **Penyimpanan Data**
  - Data disimpan secara lokal menggunakan file `.csv`

---

## 🗂️ Struktur Folder
```text
src/
└── main/
    └── java/
        └── org/
            └── example/
                ├── App.java
                │
                ├── data/
                │   ├── users.csv
                │   └── transactions.csv
                │
                ├── finance/
                │   ├── Transaction.java
                │   ├── TransactionService.java
                │   ├── DashboardService.java
                │   └── ChartService.java
                │
                ├── login/
                │   ├── User.java
                │   ├── UserService.java
                │   └── Session.java
                │
                └── ui/
                    ├── AuthFrame.java
                    ├── DashboardFrame.java
                    ├── AddTransactionFrame.java
                    ├── DataTransactionFrame.java
                    ├── ReportFrame.java
                    ├── BaseFrame.java
                    ├── NavButton.java
                    ├── RoundedBorder.java
                    └── TransactionTableModel.java

```
---

## ▶️ Cara Menjalankan Program

1. Pastikan **Java JDK 8 atau lebih baru** sudah terpasang
2. Clone repository ini:
   ```bash
   git clone https://github.com/Machobotak/Manajemen-keuangan-123-167-.git
Buka project menggunakan IntelliJ IDEA

Jalankan file:

Salin kode
App.java
Aplikasi akan terbuka pada halaman Login

