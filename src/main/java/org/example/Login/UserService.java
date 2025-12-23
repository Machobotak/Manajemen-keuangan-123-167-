package org.example.Login;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String FILE_NAME = "users.csv";

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    users.add(new User(data[0], data[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file user.");
        }
        return users;
    }

    public static boolean register(String username, String password){
        if(username.isEmpty()||password.isEmpty()){
            throw new IllegalArgumentException("Username dan Password tidak boleh kosong");
        }
        for(User user : loadUsers()){
            if(user.getUsername().equals(username)){
                return false;
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME,true))) {
            bw.write(username + ","+password);
            bw.newLine();
            return true;
        }catch (IOException e){
            System.out.println("Gagal menyimpan user");
            return false;
        }
    }

    public static boolean login(String username,String password){
        for(User user : loadUsers()){
            if(user.getUsername().equals(username)&&user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
}
