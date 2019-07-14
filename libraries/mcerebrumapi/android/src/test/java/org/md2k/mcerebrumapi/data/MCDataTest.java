package org.md2k.mcerebrumapi.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MCDataTest {
    private MCRegistration mcInt;
    private MCRegistration mcObj;

    @Before
    public void setUp() throws Exception {
        MCDataSource mcDataSource = MCDataSource.registerBuilder().intArray().setField("abc", MCDataDescriptor.builder().build()).setField("def",MCDataDescriptor.builder().build()).setDataSourceType("abc").build();
        mcInt = new MCRegistration(new MCDataSourceResult(1,0,0, mcDataSource));
        MCDataSource mc1 = MCDataSource.registerBuilder().object().setField("abc", MCDataDescriptor.builder().build()).setDataSourceType("abc").build();
        mcObj = new MCRegistration(new MCDataSourceResult(1,0,0, mc1));

    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void createIntTest(){
        int[] actual=new int[]{1,2};
        MCData d= MCData.create(mcInt, System.currentTimeMillis(),actual);
        int[] received = d.getSample(int[].class);
        assertEquals(actual.length, received.length);
        assertEquals(actual[0], received[0]);
        assertEquals(actual[1], received[1]);
        try {
            d = MCData.create(mcInt, 0, new int[]{1, 2, 3});
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        try {
            d = MCData.create(mcInt, 0, new double[]{1, 2});
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
    }
    @Test
    public void createObjTest(){
        MyObj a1=new MyObj(1,1.1, "abc", new int[]{10,20});
        MCData d=MCData.create(mcObj, System.currentTimeMillis(), a1);
        JSONObject a2 = d.getSample();
        assertEquals(a2.getIntValue("a"),1);
        assertEquals(a2.getDoubleValue("b"),1.1, 0.0);
        assertEquals(a2.getString("c"),"abc");
        int[] dd = a2.getObject("d",int[].class);
        assertEquals(dd[0], 10);
        assertEquals(dd[1], 20);
        MyObj a3 = d.getSample(MyObj.class);
        assertEquals(a3.a,1);
        assertEquals(1.1, a3.b, 0.0);
        assertEquals(a3.c, "abc");
    }
}