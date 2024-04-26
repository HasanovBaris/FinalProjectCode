package com.naa.trainingcenter.config;

import com.naa.trainingcenter.security.filter.AuthenticationFilter;
import com.naa.trainingcenter.security.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITE_LIST = {
            "/login/**",
            "/sign-in/**",
            "/token/refresh/**",
            "/monitor/**",
            "/user/save/**",
            "/registration/saveAll/**",
            "/registration/all/**",

    };

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter customAuthenticationFilter;
        customAuthenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(AUTH_WHITE_LIST).permitAll();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/planning/mrt/**", "/planning/bulk/mrt/**").hasAnyAuthority("Planner", "Registr")
                .antMatchers(HttpMethod.POST, "/planning/rentgen/**", "/planning/bulk/rentgen/**").hasAnyAuthority("Planner", "Registr")
                .antMatchers(HttpMethod.DELETE, "/planning/delete/mrt/**").hasAnyAuthority("Planner", "Registr")
                .antMatchers(HttpMethod.DELETE, "/planning/delete/rentgen/**").hasAnyAuthority("Planner", "Registr")
                .antMatchers(HttpMethod.PUT, "/planning/reserved-to-sale/mrt/**", "/planning/restore/mrt/**", "/planning/cancel/mrt/**", "/planning/again-booking/mrt/**", "/planning/change-simulator/mrt/**").hasAnyAuthority("Admin", "AirbusPlanner", "HeadPlannerRegistrar")
                .antMatchers(HttpMethod.PUT, "/planning/reserved-to-sale/rentgen/**", "/planning/restore/rentgen/**", "/planning/cancel/rentgen/**", "/planning/again-booking/rentgen/**", "/planning/change-simulator/rentgen/**").hasAnyAuthority("Admin", "BoengPlanner", "HeadPlannerRegistrar")
                .antMatchers(HttpMethod.PUT, "/planning/admin-edit/**").hasAnyAuthority("Admin", "HeadPlannerRegistrar")
                .antMatchers("/user/**", "/role/**", "/logs/**", "/metric/**").hasAuthority("Admin")
                .antMatchers("/reports/download/**").hasAnyAuthority("Admin", "HeadPlannerRegistrar")
                .anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5501", "http://192.168.202.20"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(List.of("content-type", "authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
