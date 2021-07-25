package com.mytest.webapi.util;

import com.mytest.webapi.model.dto.UserRegistrationDto;
import org.owasp.esapi.ESAPI;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

public class AttributeUtil {
    public static Attributes createAttribute(UserRegistrationDto userRegistrationDto){
        Attributes atts = new BasicAttributes();
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("inetOrgPerson");
        atts.put(attribute);
        Attribute sn = new BasicAttribute("sn");
        sn.add(userRegistrationDto.getEmail().toLowerCase());
        Attribute cn = new BasicAttribute("cn");
        cn.add(userRegistrationDto.getEmail().toLowerCase());
        atts.put(sn);
        atts.put(cn);
        atts.put("userPassword", PasswordUtils.digestSHA(ESAPI.encoder().encodeForLDAP(userRegistrationDto.getPassword())));
    return atts;
    }
}
