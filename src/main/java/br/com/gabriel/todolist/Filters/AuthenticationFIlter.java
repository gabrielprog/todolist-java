package br.com.gabriel.todolist.Filters;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabriel.todolist.users.UserModel;
import br.com.gabriel.todolist.users.UserRespository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFIlter extends OncePerRequestFilter {

    @Autowired
    private UserRespository userRespository;

    public String[] parsePassword(String passwordEncode) {
        String valuePasswordInBase64 = passwordEncode.substring("Basic".length()).trim();
        byte[] decodedValuePassword = Base64.getDecoder().decode(valuePasswordInBase64);
        String password = new String(decodedValuePassword);
        return password.split(":");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO: Valida rota para apenas na criação de usuário
        HttpServletRequest httpRequest = request;
        String[] payloadUser = parsePassword(httpRequest.getHeader("Authorization"));
        UserModel user = userRespository.findByUsername(payloadUser[0]);
        
        if(user == null)
            response.sendError(404);
            
        boolean isLoginValid = BCrypt
                            .verifyer()
                            .verify(payloadUser[0].toCharArray(), user.getPassword())
                            .verified;

        if(isLoginValid)
            filterChain.doFilter(request, response);

        response.sendError(404);
    }
    
}
