package com.mytest.webapi.configuration;

import com.google.common.collect.ImmutableList;
import com.mytest.webapi.lisener.handler.CustomAuthenticationFailureHandler;
import com.mytest.webapi.security.encryption.userDetails.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MyUserDetailService userDetailService;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final BindAuthenticator bindAuthenticator;
    private final LdapAuthoritiesPopulator ldapAuthoritiesPopulator;
    private final UserDetailsContextMapper userContextMapper;

    @Value("${ldap.enabled}")
    private boolean isLdapEnabled;

    private static final List<String> permitAll = ImmutableList.of("/registration**","/forgot-password**","/reset-password**");

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers(getAntPatterns()).permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/",true)
                .failureHandler(failureHandler).permitAll().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").permitAll();
    }

    private String[] getAntPatterns(){
        return permitAll.stream().map(String::toString).toArray(String[]::new);
    }

    @Override
    public void configure(WebSecurity security){
        security.ignoring().antMatchers("/css/**","/fonts/**","/libs/**","/webjars/**");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(userDetailService);
            auth.setPasswordEncoder(argon2PasswordEncoder);
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        if(isLdapEnabled)
            auth.authenticationProvider(ldapAuthenticaionProvider());
        else
            auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticaionProvider(){
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(bindAuthenticator,
                ldapAuthoritiesPopulator){
            {
                setUserDetailsContextMapper(userContextMapper);
            }
        };
        return ldapAuthenticationProvider;
    }
}
