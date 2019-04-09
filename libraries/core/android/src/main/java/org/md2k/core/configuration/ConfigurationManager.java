package org.md2k.core.configuration;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.md2k.core.info.ConfigInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
public class ConfigurationManager {
    private Context context;

    public ConfigurationManager(Context context) {
        this.context = context;
        if (!hasConfiguration()) {
            setToDefault();
        }
    }
    public ConfigInfo getConfigInfo(){
        return ConfigInfo.get();
    }
    public void changeConfigurationFile(String filePath) throws Exception {
        File defaultFilePath = new File(context.getFilesDir().getAbsolutePath()+"/default_config");
        delete(defaultFilePath);
        unzipConfiguration(filePath, defaultFilePath.getAbsolutePath());
        delete(new File(filePath));
    }

    public String copyAssetsToInternalStorage(String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open("default_config/" + filename);
            String outDir = context.getCacheDir().getAbsolutePath();
            File outFile = new File(outDir, filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            return outFile.getAbsolutePath();

        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        }
        return null;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private boolean hasConfiguration() {
        String path = context.getFilesDir().getAbsolutePath();
        path += "/default_config";
        File f = new File(path);
        return f.exists();
    }

    private void unzipConfiguration(String zipFilePath, String unzipFilePath) throws Exception {
        try {
            InputStream is;
            ZipInputStream zis;
            String filename;
            is = new FileInputStream(zipFilePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();
                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(unzipFilePath + filename);
                    fmd.mkdirs();
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(unzipFilePath + filename);
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        }catch (Exception e){
            throw new Exception("zip file is corrupted");
        }
    }

    public ArrayList<ConfigInfo> getConfigFilesFromAsset() {
        ArrayList<ConfigInfo> configInfos = new ArrayList<>();
        try {
            String[] list = context.getAssets().list("default_config");
            assert list != null;
            for (String aList : list) {
                ConfigInfo c = new ConfigInfo(aList, false, -1, 0);
                configInfos.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configInfos;
    }

    private void delete(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                delete(child);
            }
        }
        fileOrDirectory.delete();
    }

    public void setToDefault() {
        String filePath = copyAssetsToInternalStorage("default_config.zip");
        try {
            changeConfigurationFile(filePath);
            org.md2k.core.info.ConfigInfo configInfo = new ConfigInfo("default_config.zip",false, 0,0);
            configInfo.save();
        } catch (Exception e) {
        }
    }
}
