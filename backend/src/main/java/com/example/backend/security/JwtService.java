package com.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // variables de entorno
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Cuánto tiempo dura el token (24 horas = 86400000 milisegundos)
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        // apply() ejecuta la función que recibimos
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        // Creamos un mapa vacío (sin información extra)
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                // setClaims: agrega información extra
                .setClaims(extraClaims)
                // setSubject: pone el username
                .setSubject(userDetails.getUsername())
                // setIssuedAt: cuándo se creó el token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // setExpiration: cuándo expira el token
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // signWith: firma el token con nuestra clave secreta
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        System.out.println(username);
        System.out.println("  - Username del token: " + username);
        System.out.println("  - Username del UserDetails: " + userDetails.getUsername());
        System.out.println("  - ¿Coinciden?: " + username.equals(userDetails.getUsername()));
        System.out.println("  - Token expirado?: " + isTokenExpired(token));

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // extractExpiration obtiene la fecha de expiración
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        // Claims::getExpiration es una lambda que dice "dame la expiración"
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                // setSigningKey: usa nuestra clave secreta para verificar
                .setSigningKey(getSignInKey())
                .build()
                // parseClaimsJws: lee y verifica el token
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
