package org.example.Login;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final String DATA_FOLDER = "src/main/java/org/example/data";
    private static final String USER_FILE = DATA_FOLDER + "/users.csv";

    private static void ensureDataFolder(){
        File folder = new File(DATA_FOLDER);
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    public static List<User> loadUsers() {
        ensureDataFolder();
        List<User> users = new ArrayList<>();

        File file = new File(USER_FILE);
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
        ensureDataFolder();
        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username dan password tidak boleh kosong");
        }
        for(User user : loadUsers()){
            if(user.getUsername().equals(username)){
                return false;
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE,true))) {
            bw.write(username + ","+password);
            bw.newLine();
            return true;
        }catch (IOException e){
            System.out.println("Gagal menyimpan user");
            return false;
        }
    }

    public static boolean login(String username,String password){
        for (User user : loadUsers()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                Session.currentUser = username;
                return true;
            }
        }
        return false;
    }



    public static String getTransactionFile() {
        if (Session.currentUser == null) return null;
        return DATA_FOLDER + "/transactions_" + Session.currentUser + ".csv";
    }


}
