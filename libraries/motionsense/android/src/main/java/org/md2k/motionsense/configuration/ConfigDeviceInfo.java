package org.md2k.motionsense.configuration;

import java.util.HashMap;

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
public class ConfigDeviceInfo {
    private static final String motionsense_deviceId ="motionsense_deviceId";
    private static final String motionsense_device_enable="motionsense_device_enable";
    private static final String motionsense_device_platformType="motionsense_device_platformType";
    private static final String motionsense_device_platformId="motionsense_device_platformId";
    private static final String motionsense_device_version="motionsense_device_version";
    private HashMap<String, Object> map;
    ConfigDeviceInfo(HashMap<String, Object> map){
        this.map = map;
    }

    public static ConfigDeviceInfo create(String platformType, String platformId, String deviceId, String version, boolean enable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(motionsense_device_platformType, platformType);
        map.put(motionsense_device_platformId, platformId);
        map.put(motionsense_deviceId, deviceId);
        map.put(motionsense_device_version, version);
        map.put(motionsense_device_enable, enable);
        return new ConfigDeviceInfo(map);
    }

    String getDeviceId(){
        return (String) map.get(motionsense_deviceId);
    }
    boolean getEnable(){
        return (boolean) map.get(motionsense_device_enable);
    }
    String getVersion(){
        return (String) map.get(motionsense_device_version);
    }
    String getPlatformType(){
        return (String) map.get(motionsense_device_platformType);
    }
    String getPlatformId(){
        return (String) map.get(motionsense_device_platformId);
    }


}
