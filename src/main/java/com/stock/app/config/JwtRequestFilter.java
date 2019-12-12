package com.stock.app.config;

import com.stock.app.service.StockUserDetailsService;
import com.stock.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter used for access token validation in JSON web token format, it checks once per request for the Bearer token, and
 * grants authorization if the Authorization header exists plus the token is a valid one.
 *
 * @author rolland.schnell
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private StockUserDetailsService stockUserDetailsService;

    private JwtUtils jwtUtils;

    @Autowired
    public JwtRequestFilter(StockUserDetailsService stockUserDetailsService, JwtUtils jwtUtils) {
        this.stockUserDetailsService = stockUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * The filter method that checks each request for jwt token validation using a UsernamePasswordAuthenticationToken check,
     * user Principal credentials, in this case the username is the user email.
     * @param httpServletRequest - request object
     * @param httpServletResponse - response object
     * @param filterChain - the filter chain to be continued
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.stockUserDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.
                        setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
