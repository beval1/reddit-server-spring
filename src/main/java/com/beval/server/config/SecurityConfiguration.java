package com.beval.server.config;

import com.beval.server.security.JwtAuthenticationEntryPoint;
import com.beval.server.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static com.beval.server.config.AppConstants.CROSS_ORIGIN_DOMAINS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(CROSS_ORIGIN_DOMAINS);
                    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                }).and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/subreddits/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/get-feed").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //could be used for bypassing filters
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
//                .antMatchers("/api/v1/auth/**")
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                .antMatchers("/actuator/prometheus");
    }
}
