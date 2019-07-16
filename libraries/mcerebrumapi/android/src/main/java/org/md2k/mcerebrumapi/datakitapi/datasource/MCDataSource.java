package org.md2k.mcerebrumapi.datakitapi.datasource;
/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformAppMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

import java.util.ArrayList;
import java.util.HashMap;

public class MCDataSource implements Parcelable {
    private static final String SEPARATOR = "-";

    String dataSourceType = null;
    String dataSourceId = null;
    String platformType = null;
    String platformId = null;
    String platformAppType = null;
    String platformAppId = null;
    String applicationType = null;
    String applicationId = null;


    int dataType = MCDataType.INT_ARRAY.getValue();

    HashMap<String, String> dataSourceMetaData = new HashMap<>();
    HashMap<String, String> platformMetaData = new HashMap<>();
    private HashMap<String, String> platformAppMetaData = new HashMap<>();
    HashMap<String, String> applicationMetaData = new HashMap<>();
    ArrayList<HashMap<String, String>> dataDescriptors = new ArrayList<>();

    protected MCDataSource() {
    }

    protected MCDataSource(String uuid) {
        String[] splits = uuid.split(SEPARATOR);
        if (splits.length > 0 && splits[0] != null && splits[0].length() != 0)
            this.dataSourceType = splits[0];
        if (splits.length > 1 && splits[1] != null && splits[1].length() != 0)
            this.dataSourceId = splits[1];
        if (splits.length > 2 && splits[2] != null && splits[2].length() != 0)
            this.platformType = splits[2];
        if (splits.length > 3 && splits[3] != null && splits[3].length() != 0)
            this.platformId = splits[3];
        if (splits.length > 4 && splits[4] != null && splits[4].length() != 0)
            this.platformAppType = splits[4];
        if (splits.length > 5 && splits[5] != null && splits[5].length() != 0)
            this.platformAppId = splits[5];
        if (splits.length > 6 && splits[6] != null && splits[6].length() != 0)
            this.applicationType = splits[6];
        if (splits.length > 7 && splits[7] != null && splits[7].length() != 0)
            this.applicationId = splits[7];

    }

    public static final Creator<MCDataSource> CREATOR = new Creator<MCDataSource>() {
        @Override
        public MCDataSource createFromParcel(Parcel in) {
            return new MCDataSource(in);
        }

        @Override
        public MCDataSource[] newArray(int size) {
            return new MCDataSource[size];
        }
    };

    public static IDataSourceBuilder.IAppInfo registerBuilder() {
        return new DataSourceRegisterBuilder();
    }

    public static IDataSourceBuilder.IQuery queryBuilder() {
        return new DataSourceQueryBuilder();
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPlatformAppType() {
        return platformAppType;
    }

    public String getPlatformAppId() {
        return platformAppId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public MCDataType getDataType() {
        return MCDataType.getDataType(dataType);
    }

    public MCDataSourceMetaData getDataSourceMetaData() {
        return MCDataSourceMetaData.builder().setMetaData(dataSourceMetaData).build();
    }

    public MCPlatformMetaData getPlatformMetaData() {
        return MCPlatformMetaData.builder().setMetaData(platformMetaData).build();
    }

    public MCPlatformAppMetaData getPlatformAppMetaData() {
        return MCPlatformAppMetaData.builder().setMetaData(platformAppMetaData).build();
    }

    public MCApplicationMetaData getApplicationMetaData() {
        return MCApplicationMetaData.builder().setMetaData(applicationMetaData).build();
    }

    public ArrayList<MCDataDescriptor> getDataDescriptors() {
        ArrayList<MCDataDescriptor> dds = new ArrayList<>();
        for (int i = 0; i < dataDescriptors.size(); i++) {
            dds.add(MCDataDescriptor.builder().setDescriptor(dataDescriptors.get(i)).build());
        }
        return dds;
    }

    @NonNull
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    private MCDataSource(Parcel in) {
        dataSourceType = in.readString();
        dataSourceId = in.readString();
        platformType = in.readString();
        platformId = in.readString();
        platformAppType = in.readString();
        platformAppId = in.readString();
        applicationType = in.readString();
        applicationId = in.readString();

        dataType = in.readInt();
        dataType = in.readInt();

        dataSourceMetaData = readHashMapFromParcel(in);
        platformMetaData = readHashMapFromParcel(in);
        platformAppMetaData = readHashMapFromParcel(in);
        applicationMetaData = readHashMapFromParcel(in);
        dataDescriptors = readHashMapArrayFromParcel(in);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dataSourceType);
        parcel.writeString(dataSourceId);
        parcel.writeString(platformType);
        parcel.writeString(platformId);
        parcel.writeString(platformAppType);
        parcel.writeString(platformAppId);
        parcel.writeString(applicationType);
        parcel.writeString(applicationId);

        parcel.writeInt(dataType);
        parcel.writeInt(dataType);

        writeHashMapToParcel(parcel, dataSourceMetaData);
        writeHashMapToParcel(parcel, platformMetaData);
        writeHashMapToParcel(parcel, platformAppMetaData);
        writeHashMapToParcel(parcel, applicationMetaData);
        writeDataDescriptorToParcel(parcel, dataDescriptors);
    }

    private ArrayList<HashMap<String, String>> readHashMapArrayFromParcel(Parcel in) {
        ArrayList<HashMap<String, String>> dataDescriptors;
        int size = in.readInt();
        dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++)
            dataDescriptors.add(readHashMapFromParcel(in));
        return dataDescriptors;
    }

    private void writeDataDescriptorToParcel(Parcel parcel, ArrayList<HashMap<String, String>> dataDescriptors) {
        int size = dataDescriptors.size();
        parcel.writeInt(size);
        for (HashMap<String, String> dataDescriptor : dataDescriptors)
            writeHashMapToParcel(parcel, dataDescriptor);
    }

    private HashMap<String, String> readHashMapFromParcel(Parcel in) {
        HashMap<String, String> metaData = new HashMap<>();
        int size = in.readInt();
        for (int j = 0; j < size; j++) {
            metaData.put(in.readString(), in.readString());
        }
        return metaData;
    }

    private void writeHashMapToParcel(Parcel parcel, HashMap<String, String> metaData) {
        int size = metaData.size();
        parcel.writeInt(size);
        for (HashMap.Entry<String, String> entry : metaData.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MCDataSource))
            return false;
        MCDataSource d = (MCDataSource) obj;
        return toUUID().equals(d.toUUID())
                && dataSourceMetaData.equals(d.dataSourceMetaData)
                && platformMetaData.equals(d.platformMetaData)
                && platformAppMetaData.equals(d.platformAppMetaData)
                && applicationMetaData.equals(d.applicationMetaData)
                && dataDescriptors.equals(d.dataDescriptors)
                && dataType == d.dataType;
    }

    public String toUUID() {
        return prepString(dataSourceType) + SEPARATOR
                + prepString(dataSourceId) + SEPARATOR
                + prepString(platformType) + SEPARATOR
                + prepString(platformId) + SEPARATOR
                + prepString(platformAppType) + SEPARATOR
                + prepString(platformAppId) + SEPARATOR
                + prepString(applicationType) + SEPARATOR
                + prepString(applicationId);

    }

    private String prepString(String s) {
        if (s == null) return "";
        else return s;
    }

    public boolean isSubset(MCDataSource master) {
        return (this.dataSourceType == null || this.dataSourceType.equals(master.dataSourceType))
                && (this.dataSourceId == null || this.dataSourceId.equals(master.dataSourceId))
                && (this.platformType == null || this.platformType.equals(master.platformType))
                && (this.platformId == null || this.platformId.equals(master.platformId))
                && (this.platformAppType == null || this.platformAppType.equals(master.platformAppType))
                && (this.platformAppId == null || this.platformAppId.equals(master.platformAppId))
                && (this.applicationType == null || this.applicationType.equals(master.applicationType))
                && (this.applicationId == null || this.applicationId.equals(master.applicationId));
    }

    public boolean isEqualUUID(MCDataSource d) {
        return toUUID().equals(d.toUUID());
    }

}
