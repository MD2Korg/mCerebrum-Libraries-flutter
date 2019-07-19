package org.md2k.core.cerebralcortex;

import android.util.Log;

import com.esotericsoftware.kryo.Registration;

import org.junit.Before;
import org.junit.Test;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.RegisterResponse;
import org.md2k.core.data.LoginInfo;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

public class CerebralCortexTest {
    private CerebralCortex cerebralCortex;
//    private String server = "http://md2k-hnat.memphis.edu";
    private String server = "https://odin.md2k.org";
/*
    private String username = "smh";
    private String password = "8b2efjwp";
*/
    private String username = "As97HmugJIWMXoPoLCMEY1XGpu92";
    private String password = "As97HmugJIWMXoPoLCMEY1XGpu92";
    Disposable d;
    @Before
    public void setUp() throws Exception {
        cerebralCortex = new CerebralCortex(server);
    }
    public void tearDown() throws Exception {
        if(d!=null && !d.isDisposed())
            d.dispose();
    }
    @Test
    public void login() {
        try {
            Boolean res = cerebralCortex.login(username, password).blockingFirst();
            assertTrue(res);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    public void registerDataSource() {
        try {
            Boolean res = cerebralCortex.login(username, password).blockingFirst();
            assertTrue(res);
            MCDataSource mcDataSource = MCDataSource.registerBuilder().doubleArray()
                    .setField("X", MCDataDescriptor.builder().setDescription("X axis").build())
                    .setField("Y", MCDataDescriptor.builder().setDescription("Y axis").build())
                    .setDataSourceType("ACCELEROMETER")
//                    .setApplicationType("org.md2k.cerebralcortex.test")
                    .setApplicationMetaData(MCApplicationMetaData.builder().setVersion("1.2.3").build())
                    .build();
            MCDataSourceResult mcDataSourceResult = new MCDataSourceResult(1, System.currentTimeMillis(), System.currentTimeMillis(), mcDataSource);
            RegisterResponse registerResponse = cerebralCortex.registerDataSource(mcDataSourceResult).blockingFirst();
            System.out.println("core");
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    public void insertData() {
        try {
            Boolean res = cerebralCortex.login(username, password).blockingFirst();
            assertTrue(res);
            MCDataSource mcDataSource = MCDataSource.registerBuilder().doubleArray().setField("X", MCDataDescriptor.builder().setDescription("X axis").build())
                    .setField("Y", MCDataDescriptor.builder().setDescription("Y axis").build())
                    .setField("Z", MCDataDescriptor.builder().setDescription("Z axis").build())
                    .setDataSourceType("ACCELEROMETER")
//                    .setApplicationType("org.md2k.cerebralcortex.test")
                    .setApplicationMetaData(MCApplicationMetaData.builder().setVersion("1.2.3").build())
                    .build();

            MCDataSourceResult mcDataSourceResult = new MCDataSourceResult(1, System.currentTimeMillis(), System.currentTimeMillis(), mcDataSource);
            MCRegistration mcRegistration = new MCRegistration(mcDataSourceResult);
            RegisterResponse registerResponse = cerebralCortex.registerDataSource(mcDataSourceResult).blockingFirst();
            ArrayList<MCData> d = new ArrayList<>();
            //TODO: d.add(MCData.create(mcRegistration, <DateTime with Timezone>, new double[] {0,0,0}));
            //TODO: Uploader should have UTC time in millis and Localtime in millis

            for (int i=0; i<100; i++) {
                d.add(MCData.create(mcRegistration, System.currentTimeMillis(), new double[]{Math.random(), Math.random(), i}));
            }
            boolean uploadDataResult = cerebralCortex.uploadData(registerResponse, mcDataSourceResult, d).blockingFirst();
            assertTrue(uploadDataResult);
        }catch (Exception e){
            assertTrue(false);
        }
    }
/*
    @Test
    public void getConfigurationList() {
        try {
            Boolean res = cerebralCortex.login(username, password).blockingFirst();
            assertTrue(res);
            List<FileInfo> fileInfoList = cerebralCortex.getConfigurationList().blockingFirst();
            assertTrue(fileInfoList.size()==0);
        }catch (Exception e){
            assertTrue(false);
        }
    }
*/

}