package com.javanauta.usuario_recap.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Adicionados atributos da classe para guardar dependências
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Spring moderno já injeta automaticamente quando existe um único construtor
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cria uma instância do filtro JWT com JwtUtil e UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        return http
                // Desativa proteção CSRF para APIs REST
                // (normal em autenticação JWT stateless)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso ao login
                        .requestMatchers(HttpMethod.POST, "/usuario/login")
                        .permitAll()
                        // Permite criação de usuário
                        .requestMatchers(HttpMethod.POST, "/usuario")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuario")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuario/pesquisa")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuario/{email}")
                        .permitAll()
                        // Exige autenticação para rotas de usuário
                        .requestMatchers("/usuario/**")
                        .authenticated()
                        // Exige autenticação para todas as outras
                        .anyRequest()
                        .authenticated()
                )
                // Adiciona filtro JWT antes do filtro padrão
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Configura PasswordEncoder usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}