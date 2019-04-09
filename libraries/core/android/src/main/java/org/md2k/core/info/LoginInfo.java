package org.md2k.core.info;

import com.orhanobut.hawk.Hawk;

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
public class LoginInfo {
    private String userId;
    private String serverAddress;
    private String password;
    private String accessToken;
    private boolean isLoggedIn;
    private long createTimestamp;
    private static final String LOGIN_INFO = "LOGIN_INFO";

    public LoginInfo(String userId, String serverAddress, String password, String accessToken, boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        this.userId = userId;
        this.serverAddress = serverAddress;
        this.password = password;
        this.accessToken = accessToken;
        this.createTimestamp = System.currentTimeMillis();
    }
    public LoginInfo(){
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getUserId() {
        return userId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    public void save(){
        Hawk.put(LOGIN_INFO, this);
    }

    public static void save(LoginInfo loginInfo){
        Hawk.put(LOGIN_INFO, loginInfo);
    }
    public static LoginInfo get(){
        LoginInfo loginInfo = Hawk.get(LOGIN_INFO);
        if(loginInfo==null)
            loginInfo = new LoginInfo();
        return loginInfo;
    }
    public static void clear(){
        Hawk.delete(LOGIN_INFO);
    }

}
