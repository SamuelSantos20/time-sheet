package io.github.samuelsantos20.time_sheet.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final RSAPrivateKey privateKey; // Chave privada RSA
    private final RSAPublicKey publicKey;   // Chave pública RSA


    @Value(value = "${jwt.secret}")
    private String secret;

    @Value(value = "${jwt.expiration}")
    private long expiration;


    public JwtUtil()  throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);//Tamanho da chave RSA

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();

        this.publicKey = (RSAPublicKey) keyPair.getPublic();


    }


    //Gera a chave de assinatura
    private Key getSigninKey(){

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    //Gera o token JWT
    public String generateToken(UserDetails userDetails){

        Map<String,Object> claims = new HashMap<>();

        return createToken(claims, userDetails.getUsername());

    }

    //Cria o token JWT
    private String createToken(Map<String, Object> claims, String username) {

        claims.put("sub", username);

        return Jwts.builder().claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey)
                .compact();

    }

    public Boolean validateToken(String token, UserDetails userDetails){

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    //Extrai o nome do usuário do token
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    //Extrai o tempo de expiração do token
    public Date extractExpiration(String token){

        return extractClaim(token, Claims::getExpiration);
    }

    //Verifica se o Token é valido
    private Boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractALLClaims(token);

        return claimsResolver.apply(claims);

    }

    private Claims extractALLClaims(String token){

        return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();

    }

}
