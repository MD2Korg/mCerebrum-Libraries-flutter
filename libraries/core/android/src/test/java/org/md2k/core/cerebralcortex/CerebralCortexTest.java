package org.md2k.core.cerebralcortex;

public class CerebralCortexTest {
/*    private CerebralCortex cerebralCortex;
//    private String server = "http://md2k-hnat.memphis.edu";
    private String server = "https://odin.md2k.org";
*//*
    private String username = "smh";
    private String password = "8b2efjwp";
*//*
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
    public void myPack(){
        DataPack.myPack();
        assertTrue(false);
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
    public void createMessagePack(){
        MCDataSource da = MCDataSource.registerBuilder().setApplicationInfo("a","1.0.0").stringArray().setField("x", MCDataDescriptor.builder().build()).setDataSourceType("test1").build();
        ArrayList<MCData> data = new ArrayList<>();
        MCDataSourceResult d = new MCDataSourceResult(1, 2,3,da);
        data.add(MCData.create(d, 10000000, new String[]{"a"}));
        DataPack.createMessagePack(d, data, "test.gzip");

    }*/
/*
    @Test
    public void registerDataSource() {
        try {
            Boolean res = cerebralCortex.login(username, password).blockingFirst();
            assertTrue(res);
            MCDataSource mcDataSource = MCDataSource.registerBuilder().setDefaultApplicationInfo().doubleArray()
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

*/
    //TODO: Fix the uploadData function to accept a filename instead of data due to Monowar update the codebase
//    @Test
//    public void insertData() {
//        try {
//            Boolean res = cerebralCortex.login(username, password).blockingFirst();
//            assertTrue(res);
//            MCDataSource mcDataSource = MCDataSource.registerBuilder().setDefaultApplicationInfo().doubleArray().setField("X", MCDataDescriptor.builder().setDescription("X axis").build())
//                    .setField("Y", MCDataDescriptor.builder().setDescription("Y axis").build())
//                    .setField("Z", MCDataDescriptor.builder().setDescription("Z axis").build())
//                    .setDataSourceType("ACCELEROMETER")
////                    .setApplicationType("org.md2k.cerebralcortex.test")
//                    .setApplicationMetaData(MCApplicationMetaData.builder().setVersion("1.2.3").build())
//                    .build();
//
//            MCDataSourceResult mcDataSourceResult = new MCDataSourceResult(1, System.currentTimeMillis(), System.currentTimeMillis(), mcDataSource);
//            MCRegistration mcRegistration = new MCRegistration(mcDataSourceResult);
//
//            ArrayList<MCData> d = new ArrayList<>();
//            //TODO: d.add(MCData.create(mcRegistration, <DateTime with Timezone>, new double[] {0,0,0}));
//            //TODO: Uploader should have UTC time in millis and Localtime in millis
//
//            for (int i=0; i<100; i++) {
//                d.add(MCData.create(mcRegistration, System.currentTimeMillis(), new double[]{Math.random(), Math.random(), i}));
//            }
//            boolean uploadDataResult = cerebralCortex.uploadData(mcDataSourceResult, d).blockingFirst();
//            assertTrue(uploadDataResult);
//        }catch (Exception e){
//            assertTrue(false);
//        }
//    }
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