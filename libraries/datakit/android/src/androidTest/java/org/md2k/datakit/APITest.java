package org.md2k.datakit;

import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCDataType;
import org.md2k.mcerebrumapi.core.data.MCSampleType;
import org.md2k.mcerebrumapi.core.datakitapi.MCDataKitAPI;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceRegister;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.core.status.MCStatus;
import org.md2k.mcerebrumapi.core.time.DateTime;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
public class APITest {
    private DataKitManager d;
    @Before
    public void setUp() throws Exception {
        MCDataKitAPI.init(getTargetContext());
        d.delete();
        d.start();
    }

    @After
    public void tearDown() throws Exception {
        d.delete();
    }
/*
    @Test
    public void connectTest() {
        final boolean[] connect = {false};
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                latch.countDown();
            }

            @Override
            public void onError(int status) {
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(connect[0]);
        assertTrue(MCerebrumAPI.isConnected());
        MCerebrumAPI.disconnect(callback);
        assertTrue(!MCerebrumAPI.isConnected());
    }
    @Test
    public void disconnectAllTest() {
        final boolean[] connect = {false};
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                latch.countDown();

            }

            @Override
            public void onError(int status) {
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(connect[0]);
        assertTrue(MCerebrumAPI.isConnected());
        MCerebrumAPI.disconnectAll();
        assertTrue(!MCerebrumAPI.isConnected());
    }
    @Test
    public void autoDisconnectTest() {
        final boolean[] connect = {false};
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                d.stop();

            }

            @Override
            public void onError(int status) {
                assertEquals(status, Status.DATAKIT_STOPPED);
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }
        assertTrue(!connect[0]);
        assertTrue(!MCerebrumAPI.isConnected());
    }
*/
    @Test
    public void insertData() {
        final boolean[] connect = {false};
//        MCerebrumAPI.disconnectAll();
        final CountDownLatch latch = new CountDownLatch(1);
        MCConnectionCallback callback = new MCConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                MCDataSourceRegister d = MCDataSource.registerBuilder().setDataType(MCDataType.POINT).setSampleTypeAsIntArray(3).setDataDescriptor(0,MCDataDescriptor.builder("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
                MCRegistration r1 = MCDataKitAPI.registerDataSource(d);
                MCData data1=MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{0,0,0});
                MCDataKitAPI.insertData(r1, data1);
                ArrayList<MCDataSourceResult> ds = MCDataKitAPI.queryDataSource(MCDataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 1);
                ArrayList<MCData> dx = MCDataKitAPI.queryData(ds.get(0), 5);
                assertEquals(dx.size(), 1);
                assertEquals(data1.hashCode(), dx.get(0).hashCode());
                MCData data2=MCData.createPointIntArray(DateTime.getCurrentTime(), new int[]{1,1,1});
                MCDataKitAPI.insertData(r1, data2);
                dx = MCDataKitAPI.queryData(ds.get(0), 5);
                assertEquals(dx.size(), 2);
                assertEquals(data1.hashCode(), dx.get(0).hashCode());
                assertEquals(data2.hashCode(), dx.get(1).hashCode());

                latch.countDown();
            }

            @Override
            public void onError(int status) {
                assertEquals(status, MCStatus.DATAKIT_STOPPED);
                connect[0]=false;
            }
        };
        MCDataKitAPI.connect(callback);
        try {
            latch.await(100000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(connect[0]);
        assertTrue(MCDataKitAPI.isConnected());
    }

/*
    @Test
    public void insertOrUpdateDataSource() {
        final boolean[] connect = {false};
        MCerebrumAPI.disconnectAll();
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                ArrayList<DataSourceResult> ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
                assertEquals(ds.size(), 0);

                DataSourceRegister d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A1").build();
                Registration r1 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                assertEquals(ds.get(0).getCreationTime(), ds.get(0).getLastUpdateTime());
                assertEquals(ds.get(0).getDataSource().getDataDescriptors().size(),1);
                assertNull(ds.get(0).getDataSource().getDataDescriptors().get(0).getName());

                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().setName("abc").build()).setDataSourceType("D1").setApplicationType("A1").build();
                r1 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                assertTrue(ds.get(0).getCreationTime()< ds.get(0).getLastUpdateTime());
                assertEquals(ds.get(0).getDataSource().getDataDescriptors().size(),1);
                assertEquals(ds.get(0).getDataSource().getDataDescriptors().get(0).getName(),"abc");

                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().setName("def").build()).setDataSourceType("D1").setApplicationType("A1").build();
                r1 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                assertTrue(ds.get(0).getCreationTime()< ds.get(0).getLastUpdateTime());
                assertEquals(ds.get(0).getDataSource().getDataDescriptors().size(),1);
                assertEquals(ds.get(0).getDataSource().getDataDescriptors().get(0).getName(),"def");

                latch.countDown();
            }

            @Override
            public void onError(int status) {
                assertEquals(status, Status.DATAKIT_STOPPED);
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(100000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(connect[0]);
        assertTrue(MCerebrumAPI.isConnected());
    }

    @Test
    public void queryDataSourceTest() {
        final boolean[] connect = {false};
        MCerebrumAPI.disconnectAll();
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                ArrayList<DataSourceResult> ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
                assertEquals(ds.size(), 0);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 0);

                DataSourceRegister d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A1").build();
                Registration r1 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());

                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").build();
                Registration r2 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 2);
                assertTrue(ds.get(0).getDsId()==r1.getDsId()|| ds.get(0).getDsId()==r2.getDsId());
                assertTrue(ds.get(1).getDsId()==r1.getDsId()|| ds.get(1).getDsId()==r2.getDsId());
                assertNotEquals(ds.get(0).getDsId(), ds.get(1).getDsId());

                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A2").build();
                Registration r3 = MCerebrumAPI.registerDataSource(d);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").setApplicationType("A1").build());
                assertEquals(ds.size(), 1);
                assertEquals(ds.get(0).getDsId(), r1.getDsId());
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setDataSourceType("D1").build());
                assertEquals(ds.size(), 3);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setApplicationType("A1").build());
                assertEquals(ds.size(), 1);
                ds = MCerebrumAPI.queryDataSource(DataSource.queryBuilder().setApplicationType("A2").build());
                assertEquals(ds.size(), 1);
                latch.countDown();
            }

            @Override
            public void onError(int status) {
                assertEquals(status, Status.DATAKIT_STOPPED);
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(100000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(connect[0]);
        assertTrue(MCerebrumAPI.isConnected());
    }

    @Test
    public void subscribeDataSourceTest() {
        final boolean[] connect = {false};
        final CountDownLatch latch = new CountDownLatch(1);
        final int[] count={0};
        ConnectionCallback callback = new ConnectionCallback() {
            @Override
            public void onSuccess() {
                connect[0] = true;
                MCerebrumAPI.subscribeDataSourceAsync(DataSource.queryBuilder().build(), new SubscribeDataSourceCallback() {
                    @Override
                    public void onReceive(DataSourceResult dataSourceResult) {
                        Log.d("abc", "subscribed: "+dataSourceResult.getDsId()+" "+dataSourceResult.getDataSource().toUUID());
                        count[0]++;
                    }

                    @Override
                    public void onError(int status) {
                        Log.d("abc","error");
                    }
                });
                DataSourceRegister d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A1").build();
                Registration r = MCerebrumAPI.registerDataSource(d);
                Log.d("abc", "registered: "+r.getDsId()+" "+r.getDataSource().toUUID());
                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A2").build();
                r = MCerebrumAPI.registerDataSource(d);
                Log.d("abc", "registered: "+r.getDsId()+" "+r.getDataSource().toUUID());
                d = DataSource.registerBuilder().setDataType(DataType.POINT).setSampleType(SampleType.INT_ARRAY).addDataDescriptor(DataDescriptor.builder().build()).setDataSourceType("D1").setApplicationType("A2").build();
                r = MCerebrumAPI.registerDataSource(d);
            }

            @Override
            public void onError(int status) {
                assertEquals(status, Status.DATAKIT_STOPPED);
                connect[0]=false;
            }
        };
        MCerebrumAPI.connect(callback);
        try {
            latch.await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }
        assertEquals(2, count[0]);
        assertTrue(connect[0]);
        assertTrue(MCerebrumAPI.isConnected());
    }

*/
}

