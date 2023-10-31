package com.security.spring_jwt.filter;

import com.security.spring_jwt.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {



     private final JwtService jwtService;

     private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
         final String authHeader=request.getHeader("Authorization");
         final String jwt;
         final String userEmail;
         if(authHeader==null || authHeader.startsWith("Bearer ")){
             filterChain.doFilter(request,response);
             return;
         }

         jwt=authHeader.substring(7);


         userEmail=jwtService.extractUsername(jwt);
         if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
             UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
             if(jwtService.isTokenValid(jwt,userDetails)){

                 SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
                 UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
securityContext.setAuthentication(authToken);
SecurityContextHolder.setContext(securityContext);
                // SecurityContextHolder.getContext().setAuthentication(authToken);

             }

         }

         filterChain.doFilter(request,response);



    }
}
