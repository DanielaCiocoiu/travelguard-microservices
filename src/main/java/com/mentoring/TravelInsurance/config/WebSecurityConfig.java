package com.mentoring.TravelInsurance.config;

import com.mentoring.TravelInsurance.service.jwt.AuthEntryPointJwt;
import com.mentoring.TravelInsurance.service.jwt.AuthTokenFilter;
import com.mentoring.TravelInsurance.service.user.UserDetailsServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  @Value("${spring.h2.console.path}")
  private String h2ConsolePath;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))// Spring Security will create a session when it needs one //or STATELESS
                        .authorizeHttpRequests(auth ->auth
                    .requestMatchers(antMatcher("/**")).permitAll()
                    .requestMatchers(antMatcher("/api/auth/**")).permitAll()
                    .requestMatchers(antMatcher("/api/policies/**")).permitAll()
                    .requestMatchers(antMatcher("/api/policy/**")).permitAll()
                    .requestMatchers(antMatcher("/api/test/**")).permitAll()
                    .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(antMatcher("/v3/api-docs/**" )).permitAll()
                    .requestMatchers(antMatcher( "/swagger-ui.html" )).permitAll()
                    .requestMatchers(antMatcher( "/swagger-ui/**" )).permitAll()
                    .requestMatchers(antMatcher( "/api/numberOfPoliciesByEachPerson/**" )).permitAll()
                    .requestMatchers(antMatcher( "/api/numberOfPoliciesByPersonId/**" )).permitAll()
                    .requestMatchers(antMatcher( "/restTemplate/**" )).permitAll()
                    .requestMatchers(antMatcher( "/restTemplate/getAllPolicies" )).permitAll()
                    .requestMatchers(antMatcher( "/restTemplate/getPolicyById/{id}" )).permitAll()
                    .anyRequest().authenticated()
            );

    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
            .info(new Info().title("Travel insurance")
                        .license(new License().name("License of API")
                            .url("API license URL")));
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
  }


}
