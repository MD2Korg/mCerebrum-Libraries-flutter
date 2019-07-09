package org.md2k.core.controller;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.data.LoginInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {
    public void execute(String server, String username, String password, final ReceiveCallback receiveCallback) {
        String passwordHash = "";

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            passwordHash = String.format("%064x", new java.math.BigInteger(1, digest));

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setServerAddress(server);
        loginInfo.setUserId(username);
        loginInfo.setPassword(passwordHash);
/*
        Core.cerebralCortex.login(server, username, password, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                LoginInfo l = (LoginInfo) obj;
                receiveCallback.onReceive(l);
            }

            @Override
            public void onError(Exception exception) {
                receiveCallback.onError(exception);
            }
        });
*/

    }
}
