package com.example.ShopApp.config;

import com.example.ShopApp.filter.JwtTokenFilter;
import com.example.ShopApp.models.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                            String.format("%s/users/register",apiPrefix),
                            String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(GET,String.format("%s/categories**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(GET,String.format("%s/products**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(GET,String.format("%s/order_detail**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/order_detail/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT,String.format("%s/order_detail/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/order_detail/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(GET,String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(POST,String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER)
                            .requestMatchers(PUT,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)// cái này sau này phải chỉnh lại
                            // vì người dùng cũng có quyền được huỷ đơn hàng nếu đơn hàng chưa duyệt và giao , admin cx sẽ được huỷ đơn hàng


                            .anyRequest().authenticated();
                });

        return httpSecurity.build();
    }
}
