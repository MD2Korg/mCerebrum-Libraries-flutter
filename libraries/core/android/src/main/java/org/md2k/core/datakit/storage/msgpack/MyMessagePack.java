package org.md2k.core.datakit.storage.msgpack;

import android.util.Log;

import org.md2k.core.Utils;
import org.md2k.core.datakit.storage.IUploader;
import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MyMessagePack implements IUploader {
    private String directory;

    public MyMessagePack() {
        directory = MCerebrumAPI.getContext().getFilesDir().getAbsolutePath() + File.separator + "upload" + File.separator;
        File f = new File(directory);
        f.mkdirs();
    }

    @Override
    public boolean createMessagePack(MCDataSourceResult mcDataSourceResult, ArrayList<MCData> mcData) {
        long curTime = System.currentTimeMillis();
        String filenameMessagePack = directory + curTime + "_" + mcDataSourceResult.getDataSource().toUUID() + ".gz";
        String filenameDataSourceResult = directory + curTime + "_" + mcDataSourceResult.getDataSource().toUUID() + ".json";
        boolean result = Utils.writeJson(filenameDataSourceResult, mcDataSourceResult);
        return DataPack.createMessagePack(mcDataSourceResult, mcData, filenameMessagePack);
    }

    @Override
    public String[] getFileList() {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".json")) return true;
                return false;
            }
        };

        File f = new File(directory);
        String[] list = null;
        try {
            list = f.list(filenameFilter);
        } catch (Exception e) {
            Log.e("abc", "abc");
        }
        Log.d("core", "filecount=" + list.length);
        for (int i = 0; i < list.length; i++) {
            Log.d("core", "file=" + list[i]);
        }
        for (int i = 0; list != null && i < list.length; i++) {
            list[i] = list[i].replace(".json", "");
            list[i] = directory + list[i];
        }
        return list;
    }
}
