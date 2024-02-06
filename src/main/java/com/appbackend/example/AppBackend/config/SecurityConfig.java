    package com.appbackend.example.AppBackend.config;

    import com.appbackend.example.AppBackend.security.JwtAuthenticationEntryPoint;
    import com.appbackend.example.AppBackend.security.JwtAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    public class SecurityConfig {

        @Autowired
        public JwtAuthenticationEntryPoint point;

        @Autowired
        public JwtAuthenticationFilter filter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/home/**").authenticated()
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/KYC/data").authenticated()
                            .requestMatchers("/KYC/docData").authenticated()
                            .requestMatchers("/KYC/submitData").authenticated()
                            .requestMatchers("/dashboard/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated())
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }
