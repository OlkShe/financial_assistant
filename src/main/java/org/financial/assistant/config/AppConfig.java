package org.financial.assistant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.financial.assistant.services.UserDetailsService;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Auth
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/login", "/auth/registration", "/error", "/").permitAll()
                .antMatchers("/dashboard/**","/dashboard", "/category/**", "/category").authenticated()
//                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/auth/login?error")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login");
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**"); // Allow access to static resources
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Fragment inclusion
        registry.addViewController("/fragments/navigation").setViewName("fragments/navigation");
        // Errors
        registry.addViewController("/error").setViewName("error/default");
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/500").setViewName("error/500");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
