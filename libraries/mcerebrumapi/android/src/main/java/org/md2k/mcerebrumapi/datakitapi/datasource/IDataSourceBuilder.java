package org.md2k.mcerebrumapi.datakitapi.datasource;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

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
interface IDataSourceBuilder {
    interface IDataType {
        IField1 booleanArray();
        IField1 byteArray();
        IField1 intArray();
        IField1 longArray();
        IField1 doubleArray();
        IField1 stringArray();
        IField1 annotation();
        IField1 object();

    }
    interface IAppInfo{
        IDataType setApplicationInfo(String applicationId, String version);
        IDataType setDefaultApplicationInfo();
    }

    interface IField1 {
        IField2 setField(@NonNull String name, @NonNull MCDataDescriptor dataDescriptor);
    }

    interface IField2 {
        IField2 setField(String name, MCDataDescriptor dataDescriptor);
        IRegister setDataSourceType(String dataSourceType);
    }

    interface IDataSourceType {
        IRegister setDataSourceType(String dataSourceType);
    }


    interface IQuery {
        IQuery setDataSourceType(String dataSourceType);

        IQuery setDataSourceId(String dataSourceId);

        IQuery setPlatformType(String platformType);

        IQuery setPlatformId(String platformId);

        IQuery setPlatformAppType(String platformAppType);

        IQuery setPlatformAppId(String platformAppId);

        IQuery setApplicationType(String applicationType);

        IQuery setApplicationId(String applicationId);

        IQuery fromUUID(String uuid);

        MCDataSource build();
    }

    interface IRegister {
        IRegister setDataSourceId(String dataSourceId);

        IRegister setPlatformType(String platformType);

        IRegister setPlatformId(String platformId);

        IRegister setDataSourceMetaData(MCDataSourceMetaData dataSourceMetaData);

        IRegister setPlatformMetaData(MCPlatformMetaData platformMetaData);

        IRegister setApplicationMetaData(MCApplicationMetaData applicationMetaData);

        MCDataSource build();
    }
}