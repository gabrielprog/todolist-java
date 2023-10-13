package br.com.gabriel.todolist.Filters;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabriel.todolist.users.UserModel;
import br.com.gabriel.todolist.users.UserRespository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFIlter extends OncePerRequestFilter {

    @Autowired
    private UserRespository userRespository;

    private UserModel userModel;

    public String[] parseAuthentication(String passwordEncode) {
        String valuePasswordInBase64 = passwordEncode.substring("Basic".length()).trim();
        byte[] decodedValuePassword = Base64.getDecoder().decode(valuePasswordInBase64);
        String password = new String(decodedValuePassword);
        return password.split(":");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String urlPath = request.getRequestURI().substring(request.getContextPath().length());
        if(urlPath.contains("tasks") == false) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = request;
        String[] payloadUser = parseAuthentication(httpRequest.getHeader("Authorization"));

        // TODO: Problema na verificação de usuário por meio do header
        payloadUser[0] = "example";
        List<UserModel> users = userRespository.findAll();
        for(UserModel user : users) {

            System.out.println("Valor de user.getUsername(): " + user.getUsername());
            System.out.println("Valor de payloadUser[0]: " + payloadUser[0]);
            System.out.println(payloadUser[0].equals(user.getUsername()));
            
            if(user.getUsername().equals(payloadUser[0])) 
                userModel = user;
        }
        
        
        if(userModel == null) {
            response.sendError(404, "Usuário não encontrado");
            return;
        }

            
        boolean isLoginValid = BCrypt
                            .verifyer()
                            .verify(payloadUser[1].toCharArray(), userModel.getPassword())
                            .verified;

        if(isLoginValid) {
            request.setAttribute("userId", userModel.getId());
            filterChain.doFilter(request, response);
            return;
        }

        response.sendError(404, "Senha incorreta");
    }
    
}
