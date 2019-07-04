package org.md2k.mcerebrumapi_example;

import android.os.Bundle;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.extensionapi.IBackgroundProcess;
import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        MCExtensionAPI mL = MCExtensionAPI.builder()
                .asLibrary()
                .setId("abc")
                .setName("abc").setDescription("abc")
                .setVersion(1,"100")
                .noPermissionRequired()
                .noConfiguration().setBackgroundExecutionInterface(new IBackgroundProcess() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void stop() {

                    }

                    @Override
                    public boolean isRunning() {
                        return false;
                    }

                    @Override
                    public long getRunningTime() {
                        return 0;
                    }
                }).build();
        MCExtensionAPI mA = MCExtensionAPI.builder().asApp(this)
                .setDescription("abc").noPermissionRequired().noConfiguration().build();
        MCerebrumAPI.init(this, null);
        MCDataSource.registerBuilder()
                .point()
                .booleanArray()
                .setField("x",null)
                .setField("y",null)
                .setDataSourceType("abc")
                .build();
//        MCExtensionAPI.builder(this).asLibrary();
/*
        MCerebrumAPI.init(this, null);
        MCDataKitAPI.init(this);
        MCExtensionAPI.builderApp(this).setName("abc").setDescription("abc").noPermissionRequired().noConfiguration().addAction("abc", "abc", "abc", new MCAction() {
            @Override
            public void run(Object obj, ExtensionCallback extensionCallback) {

            }
        }).build();
        MCExtensionAPI mm = MCExtensionAPI.builderLibrary().setId("core").setName("Core Library").setDescription("Core Library").setIcon(null).setVersion(2, "2").noPermissionRequired().setConfiguration(new IConfigure() {
            @Override
            public ConfigState getConfigurationState() {
                return null;
            }

            @Override
            public void setConfiguration(Object object, ExtensionCallback extensionCallback) {

            }
        }).addAction("ab", "ab", "ab", new MCAction() {
            @Override
            public void run(Object obj, ExtensionCallback extensionCallback) {

            }
        }).build();

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
*/


    }
}
/*
class A{
    int a=20;
    double b = 25.32;
    String x = "abc";
    A(){}
}*/
