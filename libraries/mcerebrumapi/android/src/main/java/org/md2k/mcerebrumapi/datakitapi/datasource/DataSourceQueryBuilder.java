package org.md2k.mcerebrumapi.datakitapi.datasource;

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
class DataSourceQueryBuilder
        implements IDataSourceBuilder.IQuery {
    private MCDataSource dataSource;

    DataSourceQueryBuilder() {
        dataSource = new MCDataSource();
    }

    @Override
    public IDataSourceBuilder.IQuery setDataSourceType(String dataSourceType) {
        this.dataSource.dataSourceType = dataSourceType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setDataSourceId(String dataSourceId) {
        this.dataSource.dataSourceId = dataSourceId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setPlatformType(String platformType) {
        this.dataSource.platformType = platformType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setPlatformId(String platformId) {
        this.dataSource.platformId = platformId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setPlatformAppType(String platformAppType) {
        this.dataSource.platformAppType = platformAppType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setPlatformAppId(String platformAppId) {
        this.dataSource.platformAppId = platformAppId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setApplicationType(String applicationType) {
        this.dataSource.applicationType = applicationType;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery setApplicationId(String applicationId) {
        this.dataSource.applicationId = applicationId;
        return this;
    }

    @Override
    public IDataSourceBuilder.IQuery fromUUID(String uuid) {
        this.dataSource = new MCDataSource(uuid);
        return this;
    }

    @Override
    public MCDataSourceQuery build() {
        return dataSource;
    }

    public MCDataSource getDataSource() {
        return dataSource;
    }
}
