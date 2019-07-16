package org.md2k.core.datakit.storage.msgpack;

import org.md2k.core.Utils;
import org.md2k.core.datakit.storage.IUploader;
import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MessagePack implements IUploader {

    @Override
    public boolean createMessagePack(MCDataSourceResult mcDataSourceResult, ArrayList<MCData> mcData) {
        String directory = MCerebrumAPI.getContext().getFilesDir().getAbsolutePath() + File.separator + "upload" + File.separator;
        File f = new File(directory);
        f.mkdirs();
        String filenameMessagePack = directory + System.currentTimeMillis() + "_" + mcDataSourceResult.getDataSource().toUUID() + ".gz";
        String filenameDataSourceResult = directory + System.currentTimeMillis() + "_" + mcDataSourceResult.getDataSource().toUUID() + ".json";
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
        String directory = MCerebrumAPI.getContext().getFilesDir().getAbsolutePath() + File.separator + "upload" + File.separator;
        File f = new File(directory);
        String[] list = f.list(filenameFilter);
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].replace(".json", "");
        }
        return f.list();
    }
}
