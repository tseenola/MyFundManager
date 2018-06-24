package com.tseenola.jijin.myjijing.biz.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by lenovo on 2018/6/24.
 * 描述：
 */

public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;
    public MyAuthenticator() {
    }
    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}