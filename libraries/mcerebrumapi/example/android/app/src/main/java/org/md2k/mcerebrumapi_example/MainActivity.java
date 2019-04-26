package org.md2k.mcerebrumapi_example;

import android.os.Bundle;
import android.util.Log;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.MCDataKitAPI;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.time.DateTime;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        MCDataKitAPI.init(this);

        MCDataSource mcDataSource = MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.OBJECT)
                .setColumnNames("X","Y")
                .setDataDescriptor(0, null).setDataSourceType("abc").build();
        MCDataSourceResult mcDataSourceResult = new MCDataSourceResult(1,0,0, mcDataSource);
        MCRegistration m = new MCRegistration(mcDataSourceResult);
        MyData data = new MyData();
        MCData mcData = MCData.create(m, DateTime.getCurrentTime(), data);
        MCData mcData1 = mcData.getSample(MyData.class);
        A a = new A();

        MCData ad=MCData.create(m, DateTime.getCurrentTime(), a);
        MCData add = ad.getSample(A.class);
        MCDataSource mcDataSource1 = MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.INT_ARRAY)
                .setColumnNames("X")
                .setDataDescriptor(0, null).setDataSourceType("abc").build();
        MCDataSourceResult mcDataSourceResult1 = new MCDataSourceResult(1,0,0, mcDataSource1);
        MCRegistration m1 = new MCRegistration(mcDataSourceResult1);

        MCData i = MCData.create(m1, DateTime.getCurrentTime(), new int[]{3,4});
        int[]  id = i.getSample();
        MCData idd = i.getSample(int[].class);

        Log.d("abc","abc");


    }
}
class A{
    int a=20;
    double b = 25.32;
    String x = "abc";
    A(){}
}