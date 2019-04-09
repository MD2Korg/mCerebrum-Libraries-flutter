package org.md2k.core.plugin;

import android.content.Context;

import com.google.gson.Gson;

import org.md2k.core.Core;
import org.md2k.core.datakit.DataKitManager;
import org.md2k.core.datakit.exception.MCExceptionDataKitNotRunning;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.time.DateTime;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

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
public class DataSourceInfo implements IPluginExecute {
    public static final String METHOD_NAME = "DATASOURCE_INFO";

    @Override
    public void execute(final Context context, final MethodCall call, final MethodChannel.Result result) {
        result.success(getDataSources(context));
    }
    private String getDataSources(Context context){
        Gson gson = new Gson();
        ArrayList<org.md2k.core.info.DataSourceInfo> dataSourceInfos=new ArrayList<>();
        DataKitManager d = Core.dataKit;
        long curTime = DateTime.getCurrentTime();
        int dataCount;
        int dataCountLastHour;
        try {
            ArrayList<MCDataSourceResult> r = d.queryDataSource((MCDataSource) MCDataSource.queryBuilder().build());
            for(int i =0;i<r.size();i++){
                dataCount = d.queryDataCount(r.get(i).getDsId(), 0, curTime);
                dataCountLastHour = d.queryDataCount(r.get(i).getDsId(), curTime-60*60*1000, curTime);
                org.md2k.core.info.DataSourceInfo dataSourceInfo = new org.md2k.core.info.DataSourceInfo(r.get(i).getDataSource().toUUID(), r.get(i).getDataSource().toString(), dataCount, dataCountLastHour, r.get(i).getLastDataTime());
                dataSourceInfos.add(dataSourceInfo);
            }
            return gson.toJson(dataSourceInfos);
        } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
            return null;
        }
    }

}
