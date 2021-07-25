package com.mytest.webapi.LdapClient;

import com.mytest.webapi.mapper.UserMapper;
import com.mytest.webapi.model.User;
import com.mytest.webapi.model.dto.UserDto;
import com.mytest.webapi.model.dto.UserRegistrationDto;
import com.mytest.webapi.repository.UserRepository;
import com.mytest.webapi.util.AttributeUtil;
import com.mytest.webapi.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.esapi.ESAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LdapClient {
    @Value("${ldap.init_ctx}")
    private String INIT_CTX;
    @Value("${ldap.host}")
    private String LDAP_HOST;
    @Value("${ldap.manager_dn}")
    private String MGR_DN;
    @Value("${ldap.manager_password}")
    private String MGR_PW;
    @Value("${ldap.base_dn}")
    private String BASE_DN;

private final UserRepository userRepository;
private final UserMapper userMapper;

private static Hashtable<String, String> env = new Hashtable<>();
public void create(UserRegistrationDto userRegistrationDto){
    String MY_ENTRY="cn=" + userRegistrationDto.getEmail().toLowerCase()
            +","+BASE_DN;
    try {
        DirContext context = null;
        context = new InitialDirContext(env);
        Attributes attributes = AttributeUtil.createAttribute(userRegistrationDto);
        context.createSubcontext(MY_ENTRY,attributes);

    }
    catch (NamingException e){
        log.error("Create user on LdapDB failed");
        throw new RuntimeException();
    }
}
public void modify(String username, String password) {
    String MY_ENTRY = "cn=" + ESAPI.encoder().encodeForLDAP(username)
            + "," + BASE_DN;
    DirContext context = null;
    try {
        context = new InitialDirContext(env);
        ModificationItem[] mods = new ModificationItem[1];
        Attribute mod1 = new BasicAttribute("userPassword",
                PasswordUtils.digestSHA(ESAPI.encoder().encodeForLDAP(password)));
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);
        context.modifyAttributes(MY_ENTRY, mods);
    } catch (NamingException e) {
        log.error("modify user failed.");
    }
}
    public boolean search(UserRegistrationDto userRegistrationDto){
    DirContext context = null;
    SearchResult searchResult = null;
    String commonName = null;
    List<String> userList = new ArrayList<>();
    try {
        context = new InitialDirContext(env);
        String searchFilter="(objectClass=inetOrgPerson)";
        String[] requiredAttributes = {"sn", "cn"};
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(requiredAttributes);
        NamingEnumeration users = context.search(BASE_DN,
                searchFilter, controls);
        while (users.hasMore()){
            searchResult = (SearchResult) users.next();
            Attributes attributes =  searchResult.getAttributes();
            commonName = attributes.get("cn").get(0).toString();
            userList.add(commonName);
        }
    }
    catch (NamingException e){
        log.error("context creation failed");
    }
    boolean foundUser = userList.contains(ESAPI.encoder()
                        .encodeForLDAP(userRegistrationDto.getEmail().toLowerCase()));

    return foundUser;
    }

    public void deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);
        UserDto userDto = userMapper.toUserDto(user.get());
        String MY_ENTRY = "cn=" + userDto.getEmail().toLowerCase()
                + "," + BASE_DN;
        DirContext context = null;
        try {
            context = new InitialDirContext(env);
            context.destroySubcontext(MY_ENTRY);
            log.info("user" + userDto.getUsername() + "successfully deleted.") ;
        }catch (NamingException e){
            log.error("user delete failed");
        }
    }
    @PostConstruct
    public void initialize(){
    env.put(Context.INITIAL_CONTEXT_FACTORY, INIT_CTX);
    env.put(Context.PROVIDER_URL, LDAP_HOST);
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL, MGR_DN);
    env.put(Context.SECURITY_CREDENTIALS, MGR_PW);
    }
}
