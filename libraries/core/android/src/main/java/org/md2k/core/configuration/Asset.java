package org.md2k.core.configuration;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
class Asset {

    static HashMap<String, Object> read(Context context, String fileDir, String filename) {
        HashMap<String, Object> h;
        BufferedReader reader = null;
        try {
            InputStream in = context.getAssets().open(fileDir+File.separator+filename);
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
    static long getFileSize(Context context, String fileDir, String filename){
        try {
            AssetFileDescriptor fd = context.getAssets().openFd(fileDir + File.separator + filename);
            return fd.getLength();
        }catch (Exception e){
            return -1;
        }
    }

    static ArrayList<HashMap<String, Object>> getList(Context context, String directory) {
        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        try {
            String[] s = context.getAssets().list(directory);
            assert s != null;
            for (String value : s) {
                HashMap<String, Object> c = new HashMap<>();
                c.put(ConfigId.core_config_id, value);
                c.put(ConfigId.core_config_title, value);
                c.put(ConfigId.core_config_description, value);
                c.put(ConfigId.core_config_from, "asset");
                c.put(ConfigId.core_config_filename, value);
                res.add(c);
            }

            return res;
        } catch (IOException e) {
            return res;
        }
    }

}
