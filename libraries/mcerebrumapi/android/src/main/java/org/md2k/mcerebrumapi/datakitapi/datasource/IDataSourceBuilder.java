package org.md2k.mcerebrumapi.datakitapi.datasource;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCEnum;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCApplicationMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;

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
interface IDataSourceBuilder {
    interface IDataType {
        ISample setDataType(MCDataType dataType);
    }

    interface ISample {
        IDataDescriptor1 setSampleTypeAsBooleanArray(int size);
        IDataDescriptor1 setSampleTypeAsByteArray(int size);
        IDataDescriptor1 setSampleTypeAsIntArray(int size);
        IDataDescriptor1 setSampleTypeAsLongArray(int size);
        IDataDescriptor1 setSampleTypeAsDoubleArray(int size);
        IDataDescriptor1 setSampleTypeAsStringArray(int size);
        IDataDescriptorEnum setSampleTypeAsEnum();
        IDataDescriptorObject setSampleTypeAsObject();
    }

    interface IDataDescriptor1 {
        IDataDescriptor2 setDataDescriptor(int index, MCDataDescriptor dataDescriptor);
    }
    interface IDataDescriptorEnum {
        IDataSourceType setDataDescriptor(MCEnum[] mcEnums, MCDataDescriptor dataDescriptor);
    }
    interface IDataDescriptorObject {
        IDataSourceType setDataDescriptor(MCDataDescriptor dataDescriptor);
    }

    interface IDataSourceType {
        IRegister setDataSourceType(String dataSourceType);
    }

    interface IDataDescriptor2 {
        IDataDescriptor2 setDataDescriptor(int index, MCDataDescriptor dataDescriptor);

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

        MCDataSourceQuery build();
    }

    interface IRegister {
        IRegister setDataSourceId(String dataSourceId);

        IRegister setPlatformType(String platformType);

        IRegister setPlatformId(String platformId);

        IRegister setApplicationType(String applicationType);

        IRegister setApplicationId(String applicationId);

        IRegister setDataSourceMetaData(MCDataSourceMetaData dataSourceMetaData);

        IRegister setPlatformMetaData(MCPlatformMetaData platformMetaData);

        IRegister setApplicationMetaData(MCApplicationMetaData applicationMetaData);

        MCDataSourceRegister build();
    }
}