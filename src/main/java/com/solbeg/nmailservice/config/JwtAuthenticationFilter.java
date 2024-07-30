package com.solbeg.nmailservice.config;

import com.solbeg.nmailservice.exception.AppException;
import com.solbeg.nmailservice.security.UserDetailsImpl;
import com.solbeg.nmailservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    @Value("${jwt.bearer}")
    private String bearer;
    @Value("${jwt.begin-index}")
    private int beginIndex;
    @Value("${service.user-domain}")
    private String userDomain;

    private void setAuthenticationIfTokenValid(String jwt) {
        UserDetails userDetails = findUserByToken(jwt);
        Claims claims = Jwts.claims()
                .add(jwtService.extractClaims(jwt))
                .build();
        if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    claims
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!authHeader.startsWith(bearer)) {
            throw new AppException("Header should be started with 'Bearer'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        jwt = authHeader.substring(beginIndex);
        setAuthenticationIfTokenValid(jwt);
        filterChain.doFilter(request, response);
    }

    private UserDetailsImpl findUserByToken(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer + jwt);
        UserDetailsImpl user = restTemplate.exchange(userDomain + "/account", HttpMethod.GET,
                new HttpEntity<>(headers), UserDetailsImpl.class).getBody();
        Objects.requireNonNull(user);
        user.setAuthorities(extractClaimAuthority(jwt));
        return user;
    }
    private Set<SimpleGrantedAuthority> extractClaimAuthority(String jwt){
        var claim = jwtService.extractClaims(jwt).get("authorities");
        if(claim instanceof List){
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claim;
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        throw new AppException("No claim with name authorities", HttpStatus.BAD_REQUEST);
    }
}
