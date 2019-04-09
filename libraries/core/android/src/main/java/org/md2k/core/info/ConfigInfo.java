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
public class ConfigInfo {
    private boolean isValidConfig;
    private String fileName;
    private double fileSize;
    private boolean isFromServer;
    private long serverPublishedTime;
    private long createTimestamp;
    private static final String CONFIG_INFO = "CONFIG_INFO";

    public ConfigInfo(String fileName, boolean isFromServer, double fileSize, long serverPublishedTime) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.isFromServer = isFromServer;
        this.serverPublishedTime = serverPublishedTime;
        this.createTimestamp = System.currentTimeMillis();
        this.isValidConfig = true;
    }
    public ConfigInfo(){
        isValidConfig = false;
    }

    public void setValidConfig(boolean validConfig) {
        isValidConfig = validConfig;
    }

    public boolean isValidConfig() {
        return isValidConfig;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFromServer() {
        return isFromServer;
    }

    public void setFromServer(boolean fromServer) {
        isFromServer = fromServer;
    }

    public long getServerPublishedTime() {
        return serverPublishedTime;
    }

    public void setServerPublishedTime(long serverPublishedTime) {
        this.serverPublishedTime = serverPublishedTime;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public void save(){
        Hawk.put(CONFIG_INFO, this);
    }

    public static void save(ConfigInfo configInfo){
        Hawk.put(CONFIG_INFO, configInfo);
    }
    public static ConfigInfo get(){
        ConfigInfo configInfo = Hawk.get(CONFIG_INFO);
        if(configInfo==null)
            configInfo = new ConfigInfo();
        return configInfo;
    }
    public static void clear(){
        Hawk.delete(CONFIG_INFO);
    }

}
