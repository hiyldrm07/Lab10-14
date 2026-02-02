package com.sowa.halil57493.config;

import com.sowa.halil57493.filter.RateLimitingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AuthenticationProvider authenticationProvider;
        private final RateLimitingFilter rateLimitingFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(org.springframework.security.config.Customizer.withDefaults())
                                .headers(headers -> {
                                        headers.contentTypeOptions(
                                                        org.springframework.security.config.Customizer.withDefaults());
                                        headers.xssProtection(xss -> xss.headerValue(
                                                        org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));
                                        headers.frameOptions(frameOptions -> frameOptions.deny());
                                        headers.contentSecurityPolicy(cps -> cps.policyDirectives(
                                                        "default-src 'self'; script-src 'self' https://cdn.jsdelivr.net; style-src 'self' https://cdn.jsdelivr.net;"));
                                        headers.referrerPolicy(referrer -> referrer.policy(
                                                        org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN));
                                        headers.httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true)
                                                        .maxAgeInSeconds(31536000).preload(true));
                                })
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/hello").permitAll()
                                                .requestMatchers("/api/lab10/**").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/notes", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
