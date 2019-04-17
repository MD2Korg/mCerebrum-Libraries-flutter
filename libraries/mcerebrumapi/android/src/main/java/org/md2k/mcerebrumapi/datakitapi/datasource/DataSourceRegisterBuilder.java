package org.md2k.mcerebrumapi.datakitapi.datasource;

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
        , IDataSourceBuilder.IDataDescriptor
        , IDataSourceBuilder.IDataSourceType
        , IDataSourceBuilder.IColumnName
        , IDataSourceBuilder.IRegister {
    private MCDataSource dataSource;

    DataSourceRegisterBuilder() {
        dataSource = new MCDataSource();
    }

    @Override
    public IDataSourceBuilder.IColumnName setDataType(MCDataType dataType, MCSampleType sampleType) {
        dataSource.dataType = dataType.getValue();
        dataSource.sampleType =sampleType.getValue();

        return this;
    }
    @Override
    public IDataSourceBuilder.IDataDescriptor setColumnNames(String[] columnNames) {
        dataSource.dataDescriptors = new ArrayList<>();
        for (String columnName : columnNames) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("NAME", columnName);
            dataSource.dataDescriptors.add(hashMap);
        }
        return this;
    }

    @Override
    public IDataSourceBuilder.IDataDescriptor setDataDescriptor(int index, MCDataDescriptor dataDescriptor) {
        if (dataDescriptor != null) {
            String name = dataSource.dataDescriptors.get(index).get("NAME");
            HashMap<String, String> res = dataDescriptor.asHashMap();
            res.put("NAME",name);
            dataSource.dataDescriptors.set(index, res);
        }
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
