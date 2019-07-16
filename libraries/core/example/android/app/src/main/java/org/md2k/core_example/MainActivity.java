package org.md2k.core_example;

import android.os.Bundle;
import android.util.Log;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.util.HashMap;

import io.flutter.app.FlutterActivity;

/*
import org.md2k.core.info.LoginInfo;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.MCDataKitAPI;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.Utils.DateTime;
*/

public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MCerebrumAPI.init(this, MCExtensionAPI.builder().asLibrary().setId("core").setName("Core").setDescription("abc").setVersion(0, "1").noPermissionRequired().noConfiguration().build());
        MCerebrumAPI.connect(new MCConnectionCallback() {
            @Override
            public void onSuccess() {
                HashMap<String, Object> x = MCerebrumAPI.getConfiguration("core");
                Log.d("abc", "abc");

            }

            @Override
            public void onError(MCStatus status) {
                Log.d("abc", "error");

            }
        });
//        GeneratedPluginRegistrant.registerWith(this);

/*
        DefaultConfig.write(this);
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
                HashMap<String, Object> x = MCDataKitAPI.getConfiguration("phonesensor_");
                MCRegistration reg = MCDataKitAPI.registerDataSource(r);
                MCData mcData = MCData.create(reg, DateTime.getCurrentTime(), new int[]{3, 4});
                MCData mcData1 = MCData.create(reg, DateTime.getCurrentTime(), new int[]{3, 4});
                MCDataKitAPI.insertData(mcData);
                MCDataKitAPI.insertData(mcData1);
                ArrayList<MCDataSourceResult> m = MCDataKitAPI.queryDataSource(q);


                CerebralCortexManager CCM = new CerebralCortexManager(getApplicationContext());

                LoginInfo login = new LoginInfo();
                login.setServerAddress("http://md2k-hnat.memphis.edu/");
                login.setUserId("md2k");

                String password = "twh";
                String passwordHash;
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                    md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
                    byte[] digest = md.digest();
                    passwordHash = String.format("%064x", new java.math.BigInteger(1, digest));

                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
                    Log.d("CC_Debug", ignored.getMessage());
                }

                passwordHash = "06d5c1ddb7698b4331970f47dd7d8a49d78b3c5bc60eea20f3705902b8f192d2";
                login.setPassword(passwordHash);

                CCM.registerUser(login, new ReceiveCallback() {
                    @Override
                    public void onReceive(Object obj) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

*/


//                Log.d("CC_Debug", passwordHash);
//                CCM.login(login, new ReceiveCallback() {
//                    @Override
//                    public void onReceive(Object obj) {
//                        LoginInfo l = (LoginInfo) obj;
//                        Log.d("CC_Debug", "Login SUCCESS");
//                    }
//
//                    @Override
//                    public void onError(Exception exception) {
//
//                        Log.d("CC_Debug", exception.getMessage());
//                    }
//                });
//
//                CCM.getConfigurationList(login, new ReceiveCallback() {
//                    @Override
//                    public void onReceive(Object obj) {
//                        Log.d("CC_Debug", "Configuration List");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.d("CC_Debug", e.getMessage());
//                    }
//                });


/*
                Log.d("abc", "abc");
            }

            @Override
            public void onError(int status) {
                Log.d("abc", "abc");
            }
        });
*/
    }
}
