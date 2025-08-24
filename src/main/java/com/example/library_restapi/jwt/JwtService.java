package com.example.library_restapi.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    public String generateToken(Map<String,Object> extractClaims, UserDetails userDetails){
        return Jwts.builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken,Claims::getExpiration);
    }

    private boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }
    //validate token
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);
        return (userDetails.getUsername().equals(username) && !isTokenExpired(jwtToken));
    }
}
