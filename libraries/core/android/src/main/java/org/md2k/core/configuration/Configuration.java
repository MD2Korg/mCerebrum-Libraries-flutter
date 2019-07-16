package org.md2k.core.configuration;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
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
    private HashMap<String, Object> config = null;
    static Configuration create(String fileDir, String fileName, HashMap<String, Object> hashMap, boolean isNew){
        Configuration c = new Configuration();
        c.fileDir = fileDir;
        c.fileName = fileName;
        c.config = hashMap;
        if(isNew) c.write();
        return c;
    }

    void add(String key, Object value){
        config.put(key, value);
        write();
    }

    void add(HashMap<String, Object> h){
        config.putAll(h);
        write();
    }
    void removeByKey(String key){
        config.remove(key);
        write();
    }

    void removeById(String id){
        Iterator<String> iterator = config.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            if(key.startsWith(id+"_")){
                iterator.remove();
            }
        }
        write();
    }
    Object getByKey(String key){
        return config.get(key);
    }
    HashMap<String, Object> getById(String id){
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
    static void delete(String fileDir, String fileName){
        File f = new File(fileDir+File.separator+fileName);
        f.delete();
    }
    private static boolean exists(String fileDir, String fileName) {
        String path = fileDir+File.separator+fileName;
        File f = new File(path);
        return f.exists();
    }

    static HashMap<String, Object> readFromAsset(Context context) {
        HashMap<String, Object> h;
        BufferedReader reader = null;
        try {
            InputStream in = context.getAssets().open("default_config.json");
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            h = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>(){}.getType());
        } catch (Exception e) {
            h=new HashMap<>();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return h;
    }
    static HashMap<String, Object> readFromFile(String fileDir, String fileName)  {
        HashMap<String, Object> config=null;
        if(!exists(fileDir, fileName)) return null;
        BufferedReader reader = null;
        try {
            InputStream in = new FileInputStream(fileDir+File.separator+fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            config = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>(){}.getType());
        } catch (Exception e) {
            delete(fileDir, fileName);
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
}
