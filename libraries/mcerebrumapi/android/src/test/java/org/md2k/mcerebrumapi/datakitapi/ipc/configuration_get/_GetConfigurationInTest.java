package org.md2k.mcerebrumapi.datakitapi.ipc.configuration_get;

import org.junit.Before;
import org.junit.Test;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;

public class _GetConfigurationInTest {

    @Before
    public void setUp() throws Exception {
    }
    @Test
    public void readConfig(){
        MCDataSource.registerBuilder().setApplicationInfo(
/*
        Gson gson = new Gson();
        String str1 = Config.get1();
        String str2 = Config.get2();
        String str3 = Config.get3();
*/
/*
        HashMap<String, Object> h1 = JSON.parseObject(str1, HashMap.class);
        HashMap<String, Object> h2 = JSON.parseObject(str2, HashMap.class);
        HashMap<String, Object> h3 = JSON.parseObject(str3, HashMap.class);
*//*

        HashMap<String, Object> h1 = gson.fromJson(str1, HashMap.class);
        HashMap<String, Object> h2 = gson.fromJson(str2, HashMap.class);
        HashMap<String, Object> h3 = gson.fromJson(str3, HashMap.class);
*/
/*
        String str11 = JSON.toJSONString(h1, SerializerFeature.MapSortField);
        String str22 = JSON.toJSONString(h2, SerializerFeature.MapSortField);
        String str33 = JSON.toJSONString(h3, SerializerFeature.MapSortField);
        HashMap<String, Object> h11 = JSON.parseObject(str11, HashMap.class);
        HashMap<String, Object> h22 = JSON.parseObject(str22, HashMap.class);
        HashMap<String, Object> h33 = JSON.parseObject(str33, HashMap.class);
*/

/*
        boolean b1=h1.equals(h2);
        boolean b2 = h1.equals(h3);
        boolean b3 = h2.equals(h3);
*/
/*
        boolean b11=h11.equals(h22);
        boolean b22 = h11.equals(h33);
        boolean b33 = h22.equals(h33);
*/

/*
        Object o1 = h1.get("motionsense1_devices");
        Object o2 = h2.get("motionsense1_devices");
        Object o3 = h3.get("motionsense1_devices");
        boolean bb1 = o1.equals(o2);
        boolean bb2 = o1.equals(o3);
        boolean bb3 = o2.equals(o3);
        int a=1;
*/

/*
        Gson gson = new Gson();
        HashMap<String,Object> hg = gson.fromJson(str, HashMap.class);
        JSONArray a = (JSONArray) h.get("motionsense2_devices");
        JSONObject b = (JSONObject) a.get(0);
        b.put
        String str1 = JSON.toJSONString(h, SerializerFeature.SortField);
        String str2 = JSON.toJSONString(h, SerializerFeature.MapSortField);
        HashMap<String, Object> h1 = JSON.parseObject(str1, HashMap.class);
        h.equals(h1);
        assertEquals(1,1);
*/

    }
}