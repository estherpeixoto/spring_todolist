package com.estherpeixoto.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.estherpeixoto.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Limitar o Filter ao TaskController
        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {

            // Recuperar usuário e senha
            var authorization = request.getHeader("Authorization");

            if (authorization == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            } else {
                var authEncoded = authorization.substring("Basic".length()).trim();
                byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

                var authString = new String(authDecoded);
                String[] credentials = authString.split(":");

                String username = credentials[0];
                String password = credentials[1];

                // Validar usuário
                var user = this.userRepository.findByUsername(username);

                if (user == null) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                } else {
                    // Validar senha
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                    if (passwordVerify.verified) {
                        // Definir o idUser baseado na autenticação de usuário
                        request.setAttribute("idUser", user.getId());

                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(HttpStatus.UNAUTHORIZED.value());
                    }
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
