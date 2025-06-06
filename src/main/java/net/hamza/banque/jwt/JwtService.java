package net.hamza.banque.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String SECRET="5b05b1b40c8adef884dada01efd69003c3d358027e86ab89458f71181d2699f4ff611dbb91079719379c18aec99906fc4fd3753df022e4e9e5951348e0e3f66a544b657b59fe3405b182cf11580318f22cecedac82a161027ce754065212caa8c0b615424d187c11169d0f78cdbbeeb39afb9b02d3b63c782605d63092d6f7faaa75d904ab0e2fe2290da802209d196e2bcdebb716f7fda10b13ddc35475d656bc735a8024196201503acf375257b49d24a2646f2654723d42b64ee16b2328c8bf08c7b39fb38ead1b4b9dadd5168d2c4fcf69305fbf74da068d855f2f8a6e126593314f6ac914b056dd8a96a23fbd5627f8120dda40d7fe46bc2d3c735175f4";
    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);

    }
    public <T> T getClaim(String token, Function<Claims,T> claimsTClass) {
        final  Claims claims=extractAllClaims(token);
        return claimsTClass.apply(claims);
    }
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims,userDetails);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getClaim(token, Claims::getSubject);
        final Date expiration = getClaim(token, Claims::getExpiration);
        return username.equals(userDetails.getUsername())&& !expiration.before(new Date());
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .setSubject(userDetails.getUsername())
                .signWith(getSigningKey())
                .compact();


    }
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
