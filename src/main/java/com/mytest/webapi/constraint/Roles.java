package com.mytest.webapi.constraint;

public class Roles {
    public static final String AUTHORITY_ADMIN="hasAuthority('ROLE_ADMIN')";
    public static final String AUTHORITY_USER="hasAuthority('ROLE_USER')";
    public static final String AUTHORITY_SELLER="hasAuthority('ROLE_SELLER')";
}
