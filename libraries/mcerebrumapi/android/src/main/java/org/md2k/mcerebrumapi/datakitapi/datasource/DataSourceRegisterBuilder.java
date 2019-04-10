package org.md2k.mcerebrumapi.datakitapi.datasource;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCEnum;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

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
class DataSourceRegisterBuilder
        implements IDataSourceBuilder.IDataType
        , IDataSourceBuilder.ISample
        , IDataSourceBuilder.IDataDescriptor1
        , IDataSourceBuilder.IDataDescriptor2
        , IDataSourceBuilder.IDataDescriptorEnum
        , IDataSourceBuilder.IDataDescriptorObject
        , IDataSourceBuilder.IDataSourceType
        , IDataSourceBuilder.IRegister {
    private MCDataSource dataSource;

    DataSourceRegisterBuilder() {
        dataSource = new MCDataSource();
    }

    @Override
    public IDataSourceBuilder.ISample setDataType(MCDataType dataType) {
        dataSource.dataType = dataType.getValue();
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsBooleanArray(int size) {
        dataSource.sampleType = MCSampleType.BOOLEAN_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for(int i =0;i<size;i++){
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsByteArray(int size) {
        dataSource.sampleType = MCSampleType.BYTE_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsIntArray(int size) {
        dataSource.sampleType = MCSampleType.INT_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsLongArray(int size) {
        dataSource.sampleType = MCSampleType.LONG_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsDoubleArray(int size) {
        dataSource.sampleType = MCSampleType.DOUBLE_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor1 setSampleTypeAsStringArray(int size) {
        dataSource.sampleType = MCSampleType.STRING_ARRAY.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataSource.dataDescriptors.add(null);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptorEnum setSampleTypeAsEnum() {
        dataSource.sampleType = MCSampleType.ENUM.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        dataSource.dataDescriptors.add(null);
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptorObject setSampleTypeAsObject() {
        dataSource.sampleType = MCSampleType.OBJECT.getValue();
        dataSource.dataDescriptors = new ArrayList<>();
        dataSource.dataDescriptors.add(null);
        return this;
    }


    @Override
    public IDataSourceBuilder.IDataDescriptor2 setDataDescriptor(int index, MCDataDescriptor dataDescriptor) {
        if (dataDescriptor == null) {
            dataSource.dataDescriptors.set(index, MCDataDescriptor.builder("").build().asHashMap());
        } else
            dataSource.dataDescriptors.set(index, dataDescriptor.asHashMap());
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataSourceType setDataDescriptor(MCEnum[] mcEnums, MCDataDescriptor dataDescriptor) {
        HashMap<String, String> h = dataDescriptor.asHashMap();
        dataSource.dataDescriptors.set(0, MCDataDescriptor.builder(dataDescriptor.getName()).setDescriptor(h).setEnumValues(mcEnums).build().asHashMap());
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataSourceType setDataDescriptor(MCDataDescriptor dataDescriptor) {
        dataSource.dataDescriptors.set(0, dataDescriptor.asHashMap());
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceType(String dataSourceType) {
        dataSource.dataSourceType = dataSourceType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceId(String dataSourceId) {
        dataSource.dataSourceId = dataSourceId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformType(String platformType) {
        dataSource.platformType = platformType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformId(String platformId) {
        dataSource.platformId = platformId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationType(String applicationType) {
        dataSource.applicationType = applicationType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationId(String applicationId) {
        dataSource.applicationId = applicationId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setDataSourceMetaData(MCDataSourceMetaData dataSourceMetaData) {
        dataSource.dataSourceMetaData = dataSourceMetaData.getMetaData();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setPlatformMetaData(MCPlatformMetaData platformMetaData) {
        dataSource.platformMetaData = platformMetaData.getMetaData();
        return this;
    }

    @Override
    public IDataSourceBuilder.IRegister setApplicationMetaData(MCApplicationMetaData applicationMetaData) {
        dataSource.applicationMetaData = applicationMetaData.getMetaData();
        return this;
    }

    @Override
    public MCDataSourceRegister build() {
        return dataSource;
    }

    public MCDataSource getDataSource() {
        return dataSource;
    }

}
