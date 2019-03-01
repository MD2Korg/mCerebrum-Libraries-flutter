package org.md2k.datakitexample;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import org.md2k.datakit.DataKitManager;
import org.md2k.datakit.exception.MCExceptionDataKitNotRunning;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCDataType;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceRegister;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.core.time.DateTime;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    DataKitManager d = DataKitManager.getInstance(this);
    d.start();
    MCDataSourceRegister r = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(3)
            .setDataDescriptor(0, MCDataDescriptor.builder("AcclX").build())
            .setDataDescriptor(1, MCDataDescriptor.builder("AcclY").build())
            .setDataDescriptor(2, MCDataDescriptor.builder("AcclZ").build())
            .setDataSourceType("abc")
            .build();
    try {
      MCDataSourceResult m = d.insertDataSource((MCDataSource) r);
        DataArray dataArray = new DataArray();
        dataArray.add(MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{0,0,0}));
        dataArray.add(MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{1,1,1}));
        SparseArray<DataArray> s = new SparseArray<>();
        s.put(m.getDsId(), dataArray);
        d.insertData(s);
    } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
      mcExceptionDataKitNotRunning.printStackTrace();
    }
  }
}
