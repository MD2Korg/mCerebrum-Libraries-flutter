package org.md2k.core.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
class Configuration {
    private String fileDir;
    private String fileName;
    private HashMap<String, Object> config;


    Configuration(String fileDir, String fileName) {
        this.fileDir = fileDir;
        this.fileName = fileName;
        config = new HashMap<>();
        if(exists()) {
            try {
                config = read(fileDir+File.separator+fileName);
            } catch (MCException ignored) {

            }
        }
    }

    private HashMap<String, Object> read(String filepath) throws MCException {
        HashMap<String, Object> config;
        BufferedReader reader = null;
        try {
            InputStream in = new FileInputStream(filepath);
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            config = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>(){}.getType());
        } catch (Exception e) {
            throw new MCException(MCStatus.INVALID_CONFIG_FILE);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return config;
    }
    void append(String key, Object id){
        config.put(key, id);
        write();
    }

    public void remove(String key) {
        config.remove(key);
        write();
    }

    void append(HashMap<String, Object> d){
        config.putAll(d);
        write();
    }
    void set(HashMap<String, Object> d){
        config = d;
        write();
    }

    Object getValue(String key){
        return config.get(key);
    }
     HashMap<String, Object> get(String id){
        HashMap<String, Object> res = new HashMap<>();
        for(Map.Entry<String, Object> entry: config.entrySet()){
            if(entry.getKey().startsWith(id))
                res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void write(){
        FileWriter writer = null;
        try {
            new File(fileDir).mkdirs();
            Gson gson = new Gson();
            String json = gson.toJson(config);
            writer = new FileWriter(fileDir+File.separator+ fileName);
            writer.write(json);
        } catch (Exception ignored) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ignored) {
            }
        }
    }
    boolean exists() {
        String path = fileDir+File.separator+fileName;
        File f = new File(path);
        return f.exists();
    }
    void delete(){
        File f = new File(fileDir+File.separator+fileName);
        f.delete();
        config = new HashMap<>();
    }

}
