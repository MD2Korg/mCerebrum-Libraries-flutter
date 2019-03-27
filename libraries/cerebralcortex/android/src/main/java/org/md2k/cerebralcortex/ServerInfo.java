package org.md2k.cerebralcortex;

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
class ServerInfo {
    private static final String USER_NAME = "USER_NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SERVER_ADDRESS = "SERVER_ADDRESS";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String FILE_INFO = "FILE_INFO";

    protected static void setUserName(String userName) {
        Hawk.put(USER_NAME, userName);
    }
    protected static String getUserName(){
        return Hawk.get(USER_NAME);
    }

    protected static void setPassword(String password) {
        Hawk.put(PASSWORD, password);
    }
    protected static String getPassword(){
        return Hawk.get(PASSWORD);
    }

    protected static void setServerAddress(String serverAddress) {
        Hawk.put(SERVER_ADDRESS, serverAddress);
    }

    protected static void setAccessToken(String accessToken) {
        Hawk.put(ACCESS_TOKEN,accessToken);
    }

    protected static void setLoggedIn(boolean b) {
        Hawk.put(LOGGED_IN,true);
    }

    protected static boolean getLoggedIn() {
        if(Hawk.contains(LOGGED_IN)){
            return Hawk.get(LOGGED_IN);
        }else return false;
    }

    protected static String getServerAddress() {
        return Hawk.get(SERVER_ADDRESS);
    }
    protected static String getAccessToken(){
        return Hawk.get(ACCESS_TOKEN);
    }

    protected static void setFileInfo(FileInfo fileInfo) {
        Hawk.put(FILE_INFO, fileInfo);
    }

    protected static FileInfo getFileInfo(){
        return Hawk.get(FILE_INFO);
    }
}
