package com.mytest.webapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Arrays;

@Configuration
public class LdapConfig {

    @Value("${ldap.host}")
    private String LDAP_HOST;
    @Value("${ldap.manager_dn}")
    private String MGR_DN;
    @Value("${ldap.manager_password}")
    private String MGR_PW;
    @Value("${ldap.base_dn}")
    private String BASE_DN;
    @Value("${ldap.search.filter}")
    private String LDAP_SEARCH_FILTER;
    @Value("${ldap.group.search.base}")
    private String LDAP_GROUP_SEARCH;

    @Bean
    public BindAuthenticator bindAuthenticator(FilterBasedLdapUserSearch userSearch){
        return new BindAuthenticator(contextSource()){{
            setUserSearch(userSearch);
        }};
    }

    @Bean
    public FilterBasedLdapUserSearch filterBasedLdapUserSearch(){
        return new FilterBasedLdapUserSearch("",LDAP_SEARCH_FILTER, contextSource());
    }
    @Bean
    public DefaultSpringSecurityContextSource contextSource(){
        return new DefaultSpringSecurityContextSource(
                Arrays.asList(LDAP_HOST + "/"),BASE_DN){{
                    setUserDn(MGR_DN);
                    setPassword(MGR_PW);
        }};
    }
    @Bean
    public LdapAuthoritiesPopulator authoritiesPopulator(){
        return new DefaultLdapAuthoritiesPopulator(contextSource(),LDAP_GROUP_SEARCH){{
            setGroupSearchFilter(LDAP_SEARCH_FILTER);
        }};
    }

    @Bean
    public UserDetailsContextMapper userDetailsContextMapper(){
        return new InetOrgPersonContextMapper();
    }
}
