package org.md2k.core_example;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
class DefaultConfig {
    private static final String FILENAME = "default_config.json";

    private static HashMap<String, Object> read(Context context) {
        HashMap<String, Object> h;
        BufferedReader reader = null;
        try {
            InputStream in = context.getAssets().open(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            h = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            h = new HashMap<>();
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

    public static void write(Context context) {
        String fileDir = context.getFilesDir().getAbsolutePath() + File.separator + "config";
        String fileName = "default_config.json";
        HashMap<String, Object> config = read(context);

        FileWriter writer = null;
        try {
            new File(fileDir).mkdirs();
            Gson gson = new Gson();
            String json = gson.toJson(config);
            writer = new FileWriter(fileDir + File.separator + fileName);
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

}
