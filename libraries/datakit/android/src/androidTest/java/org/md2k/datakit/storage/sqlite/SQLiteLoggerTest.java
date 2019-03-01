package org.md2k.datakit.storage.sqlite;

import android.util.SparseArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCDataType;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceRegister;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.core.time.DateTime;

import java.util.ArrayList;

import androidx.test.runner.AndroidJUnit4;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@RunWith(AndroidJUnit4.class)
public class SQLiteLoggerTest {
    private SQLiteLogger sqLiteLogger;

    @Before
    public void setUp() throws Exception {
        sqLiteLogger = new SQLiteLogger(getTargetContext());
        sqLiteLogger.start();
    }

    @After
    public void tearDown() throws Exception {
        sqLiteLogger.stop();
        sqLiteLogger.delete();
    }

    @Test
    public void createDatabase() {
        assertNotNull(sqLiteLogger);
        boolean flag = false;
        for(int i =0;i<getTargetContext().databaseList().length;i++){
            if(getTargetContext().databaseList()[i].equals("sqlite.db"))
                flag=true;
        }
        assertTrue(flag);
    }
    @Test
    public void fileSize() {
        sqLiteLogger.stop();
        sqLiteLogger.delete();
        long size = sqLiteLogger.size();
        assertEquals(size, 0);
        sqLiteLogger.start();
        size = sqLiteLogger.size();
        assertNotEquals(size, 0);
        for(int i = 0;i<1000;i++) {
            MCDataSourceRegister d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0, MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A"+String.valueOf(i)).build();
            sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        }
        sqLiteLogger.stop();
        long size1 = sqLiteLogger.size();
        assertTrue(size<size1);


    }


    @Test
    public void insertDataSource() {
        assertEquals(0, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(0, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        MCDataSourceRegister d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(1, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(1, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(2, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(3, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(3, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(4, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(4, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));

    }

    @Test
    public void updateDataSource() {
        assertEquals(0, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(0, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        MCDataSourceRegister d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(1, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(1, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(2, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abcd").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(3, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("def").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("def").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));

    }

    @Test
    public void readDataSource() {
        ArrayList<MCDataSourceResult> dq;
        assertEquals(0, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(0, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        MCDataSourceRegister d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(1, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(1, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        dq=sqLiteLogger.queryDataSource((MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
        assertEquals(1, dq.size());
        assertEquals("D1", dq.get(0).getDataSource().getDataSourceType());
        assertEquals("A1", dq.get(0).getDataSource().getApplicationType());

        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(2, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        dq=sqLiteLogger.queryDataSource((MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A2").build());
        assertEquals(1, dq.size());
        assertEquals("D1", dq.get(0).getDataSource().getDataSourceType());
        assertEquals("A2", dq.get(0).getDataSource().getApplicationType());


        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abcd").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(3, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        dq=sqLiteLogger.queryDataSource((MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
        assertEquals(1, dq.size());
        assertEquals("D1", dq.get(0).getDataSource().getDataSourceType());
        assertEquals("A1", dq.get(0).getDataSource().getApplicationType());
        assertEquals(1, dq.get(0).getDataSource().getDataDescriptors().size());
        assertEquals("abcd", dq.get(0).getDataSource().getDataDescriptors().get(0).getName());


        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("def").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        dq=sqLiteLogger.queryDataSource((MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        assertEquals(1, dq.size());
        assertEquals("D1", dq.get(0).getDataSource().getDataSourceType());
        assertEquals("A1", dq.get(0).getDataSource().getApplicationType());
        assertEquals(1, dq.get(0).getDataSource().getDataDescriptors().size());
        assertEquals("def", dq.get(0).getDataSource().getDataDescriptors().get(0).getName());


        d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("def").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
        dq=sqLiteLogger.queryDataSource((MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(4, sqLiteLogger.size(TableMetaData.TABLE_NAME));
        assertEquals(1, dq.size());
        assertEquals("D1", dq.get(0).getDataSource().getDataSourceType());
        assertEquals("A1", dq.get(0).getDataSource().getApplicationType());
        assertEquals(1, dq.get(0).getDataSource().getDataDescriptors().size());
        assertEquals("def", dq.get(0).getDataSource().getDataDescriptors().get(0).getName());

    }

    @Test
    public void queryDataSource() {
        MCDataSourceRegister d;
        MCDataSource dq;
        ArrayList<MCDataSourceResult> ds;
        assertEquals(0, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        assertEquals(0, sqLiteLogger.size(TableMetaData.TABLE_NAME));
         d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0, MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
         d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0, MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
         d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0, MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A1").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
         d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0, MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A2").build();
        sqLiteLogger.insertOrUpdateDataSource((MCDataSource) d);
         dq = (MCDataSource) MCDataSource.queryBuilder().setDataSourceType("AA").build();
        ds = sqLiteLogger.queryDataSource(dq);
        assertEquals(0, ds.size());
        dq = (MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build();
        ds = sqLiteLogger.queryDataSource(dq);
        assertEquals(1, ds.size());
        dq = (MCDataSource) MCDataSource.queryBuilder().setDataSourceType("D1").build();
        ds = sqLiteLogger.queryDataSource(dq);
        assertEquals(2, ds.size());
        dq = (MCDataSource) MCDataSource.queryBuilder().setApplicationType("A2").build();
        ds = sqLiteLogger.queryDataSource(dq);
        assertEquals(2, ds.size());
        dq= (MCDataSource) MCDataSource.queryBuilder().build();
        ds = sqLiteLogger.queryDataSource(dq);
        assertEquals(4, ds.size());
    }


    @Test
    public void queryDataByN() {
        MCDataSource d = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        MCDataSource d1 = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A1").build();
        MCDataSourceResult ds = sqLiteLogger.insertOrUpdateDataSource(d);
        MCDataSourceResult ds1 = sqLiteLogger.insertOrUpdateDataSource(d1);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        SparseArray<DataArray> sa=new SparseArray<>();
        MCData dp1 = MCData.createPointIntArray(DateTime.getCurrentTime()-5*60*1000L, new int[]{5,0,0});
        MCData dp2 = MCData.createPointIntArray(DateTime.getCurrentTime()-4*60*1000L, new int[]{4,0,0});
        MCData dp3 = MCData.createPointIntArray(DateTime.getCurrentTime()-3*60*1000L, new int[]{3,0,0});
        MCData dp4 = MCData.createPointIntArray(DateTime.getCurrentTime()-2*60*1000L, new int[]{1,0,0});
        MCData dp5 = MCData.createPointIntArray(DateTime.getCurrentTime()-1*60*1000L, new int[]{1,0,0});
        MCData dp6 = MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{0,0,0});
        DataArray dataArray1 = new DataArray();
        DataArray dataArray2 = new DataArray();
        dataArray1.add(new MCData[]{dp1, dp2, dp3, dp4, dp5, dp6});
        dataArray2.add(new MCData[]{dp1, dp3, dp5});
        sa.put(ds.getDsId(), dataArray1);
        sa.put(ds1.getDsId(), dataArray2);
        sqLiteLogger.insertData(sa);
        ArrayList<MCData> data = sqLiteLogger.queryData(ds.getDsId(), 10);
        assertEquals(6, data.size());
        assertTrue(data.get(0).getTimestamp()<data.get(1).getTimestamp());

        int[] a = (int[]) data.get(0).getSample();
        assertEquals(3, a.length);
        assertEquals(5, a[0]);
        data = sqLiteLogger.queryData(ds.getDsId(), 1);
        assertEquals(1, data.size());
        data = sqLiteLogger.queryData(ds.getDsId(), 2);
        assertEquals(2, data.size());
        data = sqLiteLogger.queryData(ds.getDsId(), 6);
        assertEquals(6, data.size());
    }

    @Test
    public void queryDataByTime() {
        MCDataSource _d = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        MCDataSource _d1 = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A1").build();

        MCDataSourceResult d = sqLiteLogger.insertOrUpdateDataSource(_d);
        MCDataSourceResult d1 = sqLiteLogger.insertOrUpdateDataSource(_d1);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));
        SparseArray<DataArray> sa=new SparseArray<>();
        MCData dp1 = MCData.createPointIntArray(DateTime.getCurrentTime()-5*60*1000L, new int[]{5,0,0});
        MCData dp2 = MCData.createPointIntArray(DateTime.getCurrentTime()-4*60*1000L, new int[]{4,0,0});
        MCData dp3 = MCData.createPointIntArray(DateTime.getCurrentTime()-3*60*1000L, new int[]{3,0,0});
        MCData dp4 = MCData.createPointIntArray(DateTime.getCurrentTime()-2*60*1000L, new int[]{1,0,0});
        MCData dp5 = MCData.createPointIntArray(DateTime.getCurrentTime()-1*60*1000L, new int[]{1,0,0});
        MCData dp6 = MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{0,0,0});
        DataArray dataArray=new DataArray();
        dataArray.add(new MCData[]{dp1, dp2, dp3, dp4, dp5, dp6});
        sa.put(d.getDsId(), dataArray);
        sqLiteLogger.insertData(sa);
        ArrayList<MCData> ds = sqLiteLogger.queryData(d.getDsId(), DateTime.getCurrentTime()-2*60*1000L-30*1000L, DateTime.getCurrentTime());
        assertEquals(3, ds.size());
        assertTrue(ds.get(0).getTimestamp()<ds.get(1).getTimestamp());
        ds = sqLiteLogger.queryData(d.getDsId(), DateTime.getCurrentTime()-10*60*1000L-30*1000L, DateTime.getCurrentTime());
        assertEquals(6, ds.size());
        ds = sqLiteLogger.queryData(d.getDsId(), DateTime.getCurrentTime()-30*1000L, DateTime.getCurrentTime());
        assertEquals(1, ds.size());
        ds = sqLiteLogger.queryData(d.getDsId(), DateTime.getCurrentTime()-1, DateTime.getCurrentTime());
        assertEquals(0, ds.size());
    }

    @Test
    public void countData() {
        SparseArray<DataArray> sa=new SparseArray<>();
        MCData dp1 = MCData.createPointIntArray(DateTime.getCurrentTime()-2*60*1000L, new int[]{5,0,0});
        MCData dp2 = MCData.createPointIntArray(DateTime.getCurrentTime()- 60 * 1000L, new int[]{4,0,0});
        MCData dp3 = MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{3,0,0});
        MCDataSource _d = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
        MCDataSource _d1 = (MCDataSource) MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(1).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D2").setApplicationType("A1").build();

        MCDataSourceResult d = sqLiteLogger.insertOrUpdateDataSource(_d);
        MCDataSourceResult d1 = sqLiteLogger.insertOrUpdateDataSource(_d1);
        assertEquals(2, sqLiteLogger.size(TableDataSource.TABLE_NAME));

        int count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getYesterday(), DateTime.getCurrentTime());
        assertEquals(0, count);
        DataArray dataArray = new DataArray();
        dataArray.add(new MCData[]{dp1});

        sa.put(d.getDsId(), dataArray);
        sqLiteLogger.insertData(sa);
        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getYesterday(), DateTime.getCurrentTime());
        assertTrue(count==1);

         dataArray = new DataArray();
        dataArray.add(new MCData[]{dp1, dp2});

        sa.put(d.getDsId(), dataArray);
        sqLiteLogger.insertData(sa);
        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getYesterday(), DateTime.getCurrentTime());
        assertTrue(count==3);

        dataArray = new DataArray();
        dataArray.add(new MCData[]{dp1, dp2,dp3});


        sa.put(d.getDsId(), dataArray);
        sqLiteLogger.insertData(sa);
        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getYesterday(), DateTime.getCurrentTime());
        assertTrue(count==6);

        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getCurrentTime()-2*60*1000, DateTime.getCurrentTime());
        assertTrue(count==3);

        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getCurrentTime()-5*60*1000, DateTime.getCurrentTime());
        assertTrue(count==6);

        count = sqLiteLogger.queryDataCount(d.getDsId(), DateTime.getCurrentTime()-10, DateTime.getCurrentTime());
        assertTrue(count==0);
    }
}

