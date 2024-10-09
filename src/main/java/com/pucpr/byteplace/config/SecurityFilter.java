package com.pucpr.byteplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pucpr.byteplace.enums.Permission;

@Configuration
@EnableWebSecurity
public class SecurityFilter {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.OPTIONS, "/auth/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/auth/{id}/**").hasAuthority(Permission.VISUALIZAR_USUARIO.name());
                    authConfig.requestMatchers(HttpMethod.PUT, "/auth/{id}/**").hasAuthority(Permission.EDITAR_USUARIO.name());
                    authConfig.requestMatchers(HttpMethod.DELETE, "/auth/{id}/**").hasAuthority(Permission.EXCLUIR_USUARIO.name());
                    authConfig.requestMatchers("/error").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET, "/product").hasAuthority(Permission.VISUALIZAR_PRODUTO.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/product/cadastrar").hasAuthority(Permission.CADASTRAR_PRODUTO.name());
                    authConfig.anyRequest().denyAll();

                });

        return http.build();

    }
}


