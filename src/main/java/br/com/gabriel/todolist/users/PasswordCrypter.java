package br.com.gabriel.todolist.users;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordCrypter {
    
    public static String crypt(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
