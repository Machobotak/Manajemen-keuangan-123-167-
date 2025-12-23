package org.example;

import org.example.Login.Session;
import org.example.Login.UserService;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Pilih: ");
        int pilih = sc.nextInt();
        sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (pilih == 1) {
            boolean success = UserService.register(username, password);
            System.out.println(success ? "Register berhasil" : "Username sudah ada");
        } else if (pilih == 2) {
            boolean success = UserService.login(username, password);
            if (success) {
                System.out.println("Login berhasil");
                System.out.println("User aktif: " + Session.currentUser);
                System.out.println("File transaksi: " + UserService.getTransactionFile());
            } else {
                System.out.println("Login gagal");
            }
        }
    }
}
