package br.com.gabriel.todolist.filters;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabriel.todolist.users.UserModel;
import br.com.gabriel.todolist.users.UserRespository;
import br.com.gabriel.todolist.utils.ParseAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFIlter extends OncePerRequestFilter {

    @Autowired
    private UserRespository userRespository;
    
    private UserModel userModel;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String urlPath = request.getRequestURI().substring(request.getContextPath().length());
        
        if(urlPath.contains("tasks") == false) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] payloadUser = ParseAuthentication
                                .parseBase64(request.getHeader("Authorization"))
                                .split(":");
        userModel = userRespository.findByUsername(payloadUser[0]);
        
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
