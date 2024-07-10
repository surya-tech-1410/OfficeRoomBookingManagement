package com.learn.Spring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.learn.Spring.Services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private UserService userDetailsService;

    @SuppressWarnings("deprecation")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(new  AntPathRequestMatcher( "/signupUser")).permitAll()
                    .requestMatchers(new  AntPathRequestMatcher( "/signup")).permitAll()
                    .requestMatchers(new  AntPathRequestMatcher( "/login")).permitAll() 
                    .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll() 
                    .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/Book/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/Room/**")).authenticated()
//                    .requestMatchers(new AntPathRequestMatcher("/User/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/Event/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/login/notification/google")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/Waitlist/**")).authenticated()
 //                   .requestMatchers(new AntPathRequestMatcher("/Office/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/home")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/")).authenticated()
                   
            )
           .formLogin(formLogin ->
                formLogin
                    .loginPage("/loginForm").permitAll()
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true).failureUrl("/loginForm?error=true").permitAll() 
                    
            ).csrf().disable() // Disable CSRF protection
            .logout(logout ->
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/loginForm?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            );
      
        return http.build();
    }

    @SuppressWarnings("deprecation")
	@Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
    	 return NoOpPasswordEncoder.getInstance();
    }
/*    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("Hi Auth Check....");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
      }
*/    
}
