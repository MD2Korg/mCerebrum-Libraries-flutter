package org.md2k.mcerebrumapi.datakitapi.datasource;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

import java.util.ArrayList;
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
        , IDataSourceBuilder.ISampleType
        , IDataSourceBuilder.IDataSourceType
        , IDataSourceBuilder.IField1
        , IDataSourceBuilder.IField2
        , IDataSourceBuilder.IRegister {
    private MCDataSource dataSource;

    DataSourceRegisterBuilder() {
        dataSource = new MCDataSource();
    }

    @Override
    public IDataSourceBuilder.ISampleType point() {
        dataSource.dataType = MCDataType.POINT.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.ISampleType annotation() {
        dataSource.dataType = MCDataType.ANNOTATION.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 booleanArray() {
        dataSource.sampleType =MCSampleType.BOOLEAN_ARRAY.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 byteArray() {
        dataSource.sampleType =MCSampleType.BYTE_ARRAY.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 intArray() {
        dataSource.sampleType =MCSampleType.INT_ARRAY.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 longArray() {
        dataSource.sampleType =MCSampleType.LONG_ARRAY.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IField1 doubleArray() {
        dataSource.sampleType =MCSampleType.DOUBLE_ARRAY.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 stringArray() {
        dataSource.sampleType =MCSampleType.STRING_ARRAY.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField1 object() {
        dataSource.sampleType =MCSampleType.OBJECT.getValue();
        return this;
    }
    @Override
    public IDataSourceBuilder.IField2 setField(@NonNull String name, @NonNull MCDataDescriptor mcDataDescriptor) {
        dataSource.dataDescriptors = new ArrayList<>();
        HashMap<String, String> hashMap = mcDataDescriptor.asHashMap();
        hashMap.put("NAME",name);
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
    public IDataSourceBuilder.IRegister setApplicationType(@NonNull String applicationType) {
        dataSource.applicationType = applicationType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationId(@NonNull String applicationId) {
        dataSource.applicationId = applicationId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceMetaData(@NonNull MCDataSourceMetaData dataSourceMetaData) {
        dataSource.dataSourceMetaData = dataSourceMetaData.getMetaData();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformMetaData(@NonNull MCPlatformMetaData platformMetaData) {
        dataSource.platformMetaData = platformMetaData.getMetaData();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationMetaData(@NonNull MCApplicationMetaData applicationMetaData) {
        dataSource.applicationMetaData = applicationMetaData.getMetaData();
        return this;
    }

    @Override
    public MCDataSource build() {
        return dataSource;
    }

    public MCDataSource getDataSource() {
        return dataSource;
    }

}
