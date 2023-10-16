package br.com.gabriel.todolist.utils;

import java.util.Base64;

public class ParseAuthentication {
    
    public static String parseBase64(String passwordEncode) {
        String valuePasswordInBase64 = passwordEncode.substring("Basic".length()).trim();
        byte[] decodedValuePassword = Base64.getDecoder().decode(valuePasswordInBase64);
        String password = new String(decodedValuePassword);
        return password;
    }

}
