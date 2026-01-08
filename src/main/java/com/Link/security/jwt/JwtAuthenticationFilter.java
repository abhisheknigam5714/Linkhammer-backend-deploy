package com.Link.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Execute at once for every new request---abstract interface
    @Autowired
    private JwtUtils util;

    @Autowired
    private UserDetailsService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{

          //  1.getjwt header
            String jwt = util.getJwtFromHeader(request);

            //  2.ValidateToken
            if(jwt!=null && util.validateToken(jwt)){
                String username = util.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                /*  3.if Valid getUser Details
                --get Username ,load user ,set auth context */
                if(userDetails!=null){
                    UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        }
        catch(Exception e){
    e.printStackTrace();
        }
    //if we not add this it will break the chain
        filterChain.doFilter(request,response);
    }
}
