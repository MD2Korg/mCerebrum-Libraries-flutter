package org.md2k.mcerebrumapi.datakitapi.datasource;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

import java.util.HashMap;

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
class DataSourceRegisterBuilder
        implements IDataSourceBuilder.IDataType
        , IDataSourceBuilder.IAppInfo
        , IDataSourceBuilder.IDataSourceType
        , IDataSourceBuilder.IField1
        , IDataSourceBuilder.IField2
        , IDataSourceBuilder.IRegister {
    private MCDataSource dataSource;

    DataSourceRegisterBuilder() {
        dataSource = new MCDataSource();
    }

    @Override
    public IDataSourceBuilder.IField1 booleanArray() {
        dataSource.dataType = MCDataType.BOOLEAN_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 byteArray() {
        dataSource.dataType = MCDataType.BYTE_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 intArray() {
        dataSource.dataType = MCDataType.INT_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 longArray() {
        dataSource.dataType = MCDataType.LONG_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 doubleArray() {
        dataSource.dataType = MCDataType.DOUBLE_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 stringArray() {
        dataSource.dataType = MCDataType.STRING_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 annotation() {
        dataSource.dataType = MCDataType.ANNOTATION.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 object() {
        dataSource.dataType = MCDataType.OBJECT.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField2 setField(@NonNull String name, @NonNull MCDataDescriptor mcDataDescriptor) {
        HashMap<String, String> hashMap = mcDataDescriptor.asHashMap();
        hashMap.put(MCDataDescriptor.NAME, name);
        dataSource.dataDescriptors.add(hashMap);
        return this;
    }


    @Override
    public IDataSourceBuilder.IRegister setDataSourceType(@NonNull String dataSourceType) {
        dataSource.dataSourceType = dataSourceType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceId(@NonNull String dataSourceId) {
        dataSource.dataSourceId = dataSourceId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformType(@NonNull String platformType) {
        dataSource.platformType = platformType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformId(@NonNull String platformId) {
        dataSource.platformId = platformId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceMetaData(@NonNull MCDataSourceMetaData dataSourceMetaData) {
        dataSource.dataSourceMetaData = dataSourceMetaData.asHashMap();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformMetaData(@NonNull MCPlatformMetaData platformMetaData) {
        dataSource.platformMetaData = platformMetaData.asHashMap();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationMetaData(@NonNull MCApplicationMetaData applicationMetaData) {
        HashMap<String, String> h = applicationMetaData.asHashMap();
        h.putAll(dataSource.applicationMetaData);
        dataSource.applicationMetaData = h;
        return this;
    }

    @Override
    public MCDataSource build() {
        return dataSource;
    }

    public MCDataSource getDataSource() {
        return dataSource;
    }

    @Override
    public IDataSourceBuilder.IDataType setApplicationInfo(String applicationId, String version) {
        dataSource.applicationType = MCerebrumAPI.getContext().getPackageName();
        dataSource.applicationId = applicationId;
        dataSource.applicationMetaData = MCApplicationMetaData.builder()
                .setVersion(version)
                .setName(getName())
                .build().asHashMap();
        return this;
    }
    private String getVersion(){
        try {
            return MCerebrumAPI.getContext().getPackageManager().getPackageInfo(MCerebrumAPI.getContext().getPackageName(), 0).versionName;
        }catch (Exception e){
            return "0.0.0";
        }
    }
    private String getName(){
        try {
            return MCerebrumAPI.getContext().getApplicationInfo().name;
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public IDataSourceBuilder.IDataType setDefaultApplicationInfo() {
        dataSource.applicationType = MCerebrumAPI.getContext().getPackageName();
        dataSource.applicationMetaData = MCApplicationMetaData.builder()
                .setVersion(getVersion())
                .setName(getName())
                .build().asHashMap();
        return this;
    }
}
