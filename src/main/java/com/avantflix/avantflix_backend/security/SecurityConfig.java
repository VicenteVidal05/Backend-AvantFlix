package com.avantflix.avantflix_backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/error",
                    "/health",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                .requestMatchers(HttpMethod.GET,
                       "/api/v1/productos/**",
                      "/api/v1/animes/**",
                       "/api/v1/categorias/**",
                      "/api/v1/tags/**",
                      "/api/v1/blogs/**",
                      "/api/v1/regiones/**",
                      "/api/v1/comunas/**"
                ).permitAll()

                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**", "/api/v1/animes/**", "/api/v1/categorias/**", "/api/v1/tags/**", "/api/v1/roles/**", "/api/v1/regiones/**", "/api/v1/comunas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**", "/api/v1/animes/**", "/api/v1/categorias/**", "/api/v1/tags/**", "/api/v1/roles/**", "/api/v1/regiones/**", "/api/v1/comunas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**", "/api/v1/animes/**", "/api/v1/categorias/**", "/api/v1/tags/**", "/api/v1/roles/**", "/api/v1/regiones/**", "/api/v1/comunas/**", "/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios", "/api/v1/roles/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
