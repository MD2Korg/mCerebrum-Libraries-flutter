package org.md2k.core_example;

import android.os.Bundle;
import android.util.Log;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.MCDataKitAPI;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.time.DateTime;

import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        MCDataKitAPI.init(this);
        MCDataSource r = MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.INT_ARRAY)
                .setColumnNames(new String[]{"abc", "def"})
                .setDataSourceType("abc")
                .build();
        MCDataSource a;
        MCDataSource q = MCDataSource.queryBuilder().build();
        MCDataKitAPI.connect(new MCConnectionCallback() {
            @Override
            public void onSuccess() {
                MCRegistration reg = MCDataKitAPI.registerDataSource(r);
                MCData mcData = MCData.create(reg, DateTime.getCurrentTime(), new int[]{3, 4});
                MCData mcData1 = MCData.create(reg, DateTime.getCurrentTime(), new int[]{3, 4});
                MCDataKitAPI.insertData(mcData);
                MCDataKitAPI.insertData(mcData1);
                ArrayList<MCDataSourceResult> m = MCDataKitAPI.queryDataSource(q);

                Log.d("abc", "abc");
            }

            @Override
            public void onError(int status) {
                Log.d("abc", "abc");
            }
        });
    }
}
