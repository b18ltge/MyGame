package org.example.utils;

import org.example.Settings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Session {
    public static boolean check() {
        try(FileInputStream reader = new FileInputStream(Settings.SESSION_FILE)) {
            var bytes = Base64.getDecoder().decode(reader.readAllBytes());
            String str = new String(bytes, StandardCharsets.UTF_8);
            String[] credentials = str.split("\n");
            var login = credentials[0];
            var password = credentials[1];
            return DBClass.containsUser(login, password);
        } catch (Exception ex) {
            return false;
        }
    }

    public static void update(String username, String password) {
        try(FileOutputStream writer = new FileOutputStream(Settings.SESSION_FILE)) {
            String str = username + "\n" + password;
            var bytes = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
            writer.write(bytes);
            writer.flush();

        } catch (IOException ex) {
            System.err.println("Cannot update a session file!");
            return;
        }
    }
}
