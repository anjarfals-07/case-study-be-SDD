    package com.api.fitnescenter.utils;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import jakarta.servlet.http.HttpServletRequest;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;

    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Set;
    import java.util.concurrent.ConcurrentHashMap;
    import java.util.function.Function;

    @Component
    public class JwtTokenProvider {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private Long expiration;

        private Set<String> blacklist = ConcurrentHashMap.newKeySet();

        // Generate token
        public String generateToken(UserDetails userDetails) {
            Map<String, Object> claims = new HashMap<>();
            return createToken(claims, userDetails.getUsername());
        }

        // Create token
        private String createToken(Map<String, Object> claims, String subject) {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + expiration * 1000);
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }

        // Validate token
//        public boolean validateToken(String token, UserDetails userDetails) {
//            String username = extractUsername(token);
//            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//        }
        // Inside JwtTokenProvider class
        public boolean validateToken(String token, UserDetails userDetails) {
            String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            if (!isValid) {
                System.out.println("Invalid token: " + token);
            }
            return isValid;
        }
        public String resolveToken(HttpServletRequest request) {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7); // Menghapus "Bearer "
            }
            return null;
        }

        // Extract username from token
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        // Extract expiration date from token
        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }

        // Check if the token has expired
        private boolean isTokenExpired(String token) {
            final Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        }

        // Menghapus token dari daftar token yang aktif (daftar token yang di-blacklist)
        public void invalidateToken(String token) {
            blacklist.add(token);
        }

        // Memeriksa apakah token di-blacklist
        public boolean isTokenBlacklisted(String token) {
            return blacklist.contains(token);
        }
    }
