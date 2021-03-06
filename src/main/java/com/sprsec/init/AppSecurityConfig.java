package com.sprsec.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@Import(WebAppConfig.class)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = Logger.getLogger(AppSecurityConfig.class.getName());

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery
                        ("SELECT u.login, r.rolesOfTheUser FROM users u " +
                                "LEFT OUTER JOIN user_rolesoftheuser r ON u.id = r.User_id where u.login = ?")
                .usersByUsernameQuery("SELECT login, password, true FROM users WHERE login =?");
                //.passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers("/sec/moderation.html").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/*").access("hasRole('ROLE_ADMIN')");

        http
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/profile")
                .failureUrl("/login?error")
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("password");

        http.logout()
                .permitAll()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);

    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(roleVoter(),
                webExpressionVoter(), (AccessDecisionVoter) authenticatedVoter()));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleVoter roleVoter() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        return roleVoter;
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
        return new WebExpressionVoter();
    }

    @Bean
    public AuthenticatedVoter authenticatedVoter() {
        return new AuthenticatedVoter();
    }
}
