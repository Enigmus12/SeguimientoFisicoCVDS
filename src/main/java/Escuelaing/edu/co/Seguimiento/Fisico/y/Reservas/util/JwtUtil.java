package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET = "mi-clave-secreta-super-segura-123456";
  private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
  private final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 horas

  public String extractUserId(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parserBuilder()
      .setSigningKey(SECRET_KEY)
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(String userId, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    return createToken(claims, userId);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts
      .builder()
      .setClaims(claims)
      .setSubject(subject)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
      .signWith(SECRET_KEY)
      .compact();
  }

  public Boolean validateToken(String token, String userId) {
    final String extractedUserId = extractUserId(token);
    return (extractedUserId.equals(userId) && !isTokenExpired(token));
  }

  // Añadir en JwtUtil
  public boolean isSignatureValid(String token) {
    try {
      Jwts
        .parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String extractRole(String token) {
    final Claims claims = extractAllClaims(token);
    return claims.get("role", String.class);
  }
}
