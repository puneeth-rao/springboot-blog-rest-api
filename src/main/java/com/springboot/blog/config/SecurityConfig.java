package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableMethodSecurity
//below annotation is for swagger-ui
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //below authenticationManager uses userDetailsService and passwordEncoder to interact with db
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
//                        authorize.anyRequest().authenticated()     //will authorise all requests irrespective of userRole
                        authorize
//                                 .requestMatchers(HttpMethod.GET, "/api/**").permitAll()   //permit get method to all users(public, even those who don't have account )
                                 .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                                 .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
//                                 .requestMatchers("/admin/**").hasRole("ADMIN")
                                 .requestMatchers("/api/**").hasAnyRole("ADMIN","USER")
                                 .anyRequest().authenticated()                                     // other than get methods put authentication
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint(this.authenticationEntryPoint)     // handle unauthorized exceptions
                ).sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)        //as jwt is stateless
                );

        //to execute jwtAuthFilter before usernamePasswordAuthFilter
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    //##############  for inMemory authentication without using DB auth
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails puneeth_user = User.builder().username("puneeth_user").password(passwordEncoder().encode("puneeth")).roles("USER").build();
//        UserDetails puneeth_admin = User.builder().username("puneeth_admin").password(passwordEncoder().encode("puneeth")).roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(puneeth_user, puneeth_admin);
//    }

}
