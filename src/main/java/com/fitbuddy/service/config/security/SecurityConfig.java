package com.fitbuddy.service.config.security;

import com.fitbuddy.service.config.security.jwt.JwtAccessDenialHandler;
import com.fitbuddy.service.config.security.jwt.JwtAuthenticationEntryPoint;
import com.fitbuddy.service.config.security.jwt.JwtFilter;
import com.fitbuddy.service.config.security.jwt.TokenProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDenialHandler jwtAccessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final Properties properties;


    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDenialHandler jwtAccessDeniedHandler,
                          TokenProvider provider, CorsConfigurationSource corsConfigurationSource, Properties properties) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.corsConfigurationSource = corsConfigurationSource;
        this.properties = properties;
        this.jwtFilter = new JwtFilter(provider, properties);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.debug(true)
                .ignoring()
                .requestMatchers(properties.getAll().toArray(String[]::new));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return   httpSecurity
                .cors( cnf -> cnf.configurationSource(corsConfigurationSource))
                .csrf( cnf -> cnf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .exceptionHandling(
                        handler -> {
                            handler.authenticationEntryPoint(jwtAuthenticationEntryPoint); // 자격 증명 관련 에러 핸들링
                            handler.accessDeniedHandler(jwtAccessDeniedHandler); // 관련 권한 에러 핸들링
                        }
                )
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT 사용으로 stateless여도 상관이 없다.
                .authorizeRequests()
                .requestMatchers(properties.getAll().toArray(String[]::new)).permitAll()
//                .antMatchers(path.getSuperPath()).hasAuthority(UserCode.Role.ROLE_SUPER.name())
//                .antMatchers(path.getAdminPath()).hasAuthority(UserCode.Role.ROLE_ADMIN.name())
//                .antMatchers(path.getUserPath()).hasAuthority(UserCode.Role.ROLE_USER.name())
                .and()

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}


