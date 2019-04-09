package org.md2k.core.datakit.privacy;

import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCDataType;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.constants.MCDataSourceType;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.constants.MCPlatformType;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.metadata.MCDataDescriptor;

import java.util.ArrayList;
import java.util.HashMap;

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
public class PrivacyManager {
    private long startTimestamp;
    private long endTimestamp;
    private ArrayList<String> privacyList;
    private HashMap<String, Boolean> hashMapPermission;

    public void start(MCData data) {
        this.startTimestamp = data.getStartTimestamp();
        this.endTimestamp = data.getEndTimestamp();
        PrivacyData privacyData = data.getSample();
        this.privacyList =privacyData.getDataSources();
        hashMapPermission.clear();
    }
    public void stop(){
        startTimestamp = 0;
        endTimestamp = 0;
        privacyList.clear();
        hashMapPermission.clear();
    }
    public PrivacyManager(){
        startTimestamp = 0;
        endTimestamp = 0;
        privacyList=new ArrayList<>();
        hashMapPermission =new HashMap<>();
    }
    public boolean hasPermission(String uuid, long timeStamp){
        if(privacyList.size()==0 || timeStamp<startTimestamp || timeStamp>endTimestamp ) return true;
        if(!hashMapPermission.containsKey(uuid)) {
            if (isMatch(uuid)) {
                hashMapPermission.put(uuid, false);
            } else hashMapPermission.put(uuid, true);
        }
        return hashMapPermission.get(uuid);
    }
    private boolean isMatch(String uuid){
        MCDataSource d = (MCDataSource) MCDataSource.queryBuilder().fromUUID(uuid).build();
        for (String aPrivacyList : privacyList) {
            MCDataSource compare = (MCDataSource) MCDataSource.queryBuilder().fromUUID(aPrivacyList).build();
            if (d.isSubset(compare)) return true;
        }
        return false;
    }
    public static MCDataSource getDataSource(){
        return (MCDataSource) MCDataSource.registerBuilder()
                .setDataType(MCDataType.ANNOTATION)
                .setSampleTypeAsObject()
                .setDataDescriptor(MCDataDescriptor.builder("Privacy List").setDescription("List of data sources").build())
                .setDataSourceType(MCDataSourceType.PRIVACY)
                .setPlatformType(MCPlatformType.PHONE)
                .build();
    }
    public static MCData createData(long startTimestamp, long endTimestamp, ArrayList<String> dataSources){
        PrivacyData privacyData=new PrivacyData(dataSources);
        return MCData.createAnnotationObject(startTimestamp, endTimestamp, privacyData);
    }
}
