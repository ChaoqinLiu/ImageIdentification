package com.example.sqlbrite.utils;

import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final String str_phone = "^[1][3578]\\d{9}$";
    private static final String str_email = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static boolean isPhone(String phone){
        return Pattern.compile(str_phone).matcher(phone).matches();
    }

    public static boolean isEmail(String email){
        return Pattern.compile(str_email).matcher(email).matches();
    }
}
