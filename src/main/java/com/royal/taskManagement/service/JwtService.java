package com.royal.taskManagement.service;

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

/**
 * Сервис для работы с JWT токенами.
 * Предоставляет методы для генерации, извлечения информации и валидации JWT токенов.
 */
@Service
public class JwtService {

    @Value("${app.jwtSecret}")
    private String SECRET_KEY;

    /**
     * Получает секретный ключ для подписи токена.
     *
     * @return секретный ключ.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Извлекает имя пользователя из JWT токена.
     *
     * @param token JWT токен.
     * @return имя пользователя (subject).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает дату истечения токена.
     *
     * @param token JWT токен.
     * @return дата истечения токена.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает конкретное требуемое значение из токена.
     *
     * @param token          JWT токен.
     * @param claimsResolver функция для извлечения необходимого значения из claims.
     * @param <T>            тип возвращаемого значения.
     * @return требуемое значение.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все claims из JWT токена.
     *
     * @param token JWT токен.
     * @return claims, содержащие информацию о токене.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param token JWT токен.
     * @return true, если токен истек, иначе false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Генерирует JWT токен для указанного пользователя.
     *
     * @param userDetails объект {@link UserDetails}, содержащий информацию о пользователе.
     * @return JWT токен.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Создает JWT токен с указанными данными и временем действия.
     *
     * @param claims  дополнительные данные для токена.
     * @param subject субъект (например, имя пользователя).
     * @return JWT токен.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Проверяет, является ли JWT токен валидным для указанного пользователя.
     *
     * @param token       JWT токен.
     * @param userDetails объект {@link UserDetails}, содержащий информацию о пользователе.
     * @return true, если токен валиден, иначе false.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
