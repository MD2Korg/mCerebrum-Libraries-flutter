package org.md2k.core.plugin;

import android.content.Context;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.configuration.ConfigId;
import org.md2k.core.data.LoginInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
class PLogin implements IPluginExecute {
    public static final String METHOD_NAME = "LOGIN";
    private static final String ARG_SERVER = "server";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    @Override
    public void execute(final Context context, final MethodCall call, final MethodChannel.Result result) {
/*
        String server = call.argument(ARG_SERVER);
        String username = call.argument(ARG_USERNAME);
        String password = call.argument(ARG_PASSWORD);
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
        Core.cerebralCortex.login(loginInfo, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                LoginInfo l = (LoginInfo) obj;
                HashMap<String, Object> x = new HashMap<>();
                x.put(ConfigId.core_login_isLoggedIn, l.isLoggedIn());
                x.put(ConfigId.core_login_serverAddress, l.getServerAddress());
                x.put(ConfigId.core_login_userId, l.getUserId());
                x.put(ConfigId.core_login_password, l.getPassword());
                x.put(ConfigId.core_login_accessToken, l.getAccessToken());
                Core.configuration.append(x);
                result.success("SUCCESS");
            }

            @Override
            public void onError(Exception exception) {
                HashMap<String, Object> x = new HashMap<>();
                x.put(ConfigId.core_login_isLoggedIn, false);
                Core.configuration.append(x);
                result.error(exception.getMessage(), exception.getMessage(), null);
            }
        });

*/
    }
}
