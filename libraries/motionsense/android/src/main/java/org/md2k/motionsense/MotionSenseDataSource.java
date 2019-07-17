package org.md2k.motionsense;

import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCPlatformMetaData;
import org.md2k.motionsenselibrary.device.SensorType;

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
public class MotionSenseDataSource {

    public static MCDataSource accelerometer(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("x", MCDataDescriptor.builder().build())
                .setField("y", MCDataDescriptor.builder().build())
                .setField("z", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.ACCELEROMETER.name())
                .setDataSourceMetaData(MCDataSourceMetaData.builder().build())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Accelerometer")
                        .build())
                .build();
    }
    public static MCDataSource accelerometerDataQuality(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("data_quality", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.ACCELEROMETER_DATA_QUALITY.name())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Accelerometer (Data Quality)")
                        .build())
                .build();
    }
    public static MCDataSource gyroscope(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("x", MCDataDescriptor.builder().build())
                .setField("y", MCDataDescriptor.builder().build())
                .setField("z", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.GYROSCOPE.name())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Gyroscope")
                        .build())
                .build();
    }
    public static MCDataSource motionSequenceNumber(String platformType, String platformId, String deviceId, String version, double frequency) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("seq_number", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.MOTION_SEQUENCE_NUMBER.name())
                .setDataSourceMetaData(MCDataSourceMetaData.builder().setMetaData("FREQUENCY",String.valueOf(frequency)).build())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Motion Sequence No")
                        .build())
                .build();
    }
    public static MCDataSource motionRaw(String platformType, String platformId, String deviceId, String version, double frequency) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("seq_number", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.MOTION_RAW.name())
                .setDataSourceMetaData(MCDataSourceMetaData.builder().setMetaData("FREQUENCY",String.valueOf(frequency)).build())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Motion Raw")
                        .build())
                .build();
    }
    public static MCDataSource battery(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("battery", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.BATTERY.name())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Battery")
                        .build())
                .build();
    }
    public static MCDataSource ppg(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("red", MCDataDescriptor.builder().build())
                .setField("green", MCDataDescriptor.builder().build())
                .setField("infrared", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.PPG.name())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("PPG")
                        .build())
                .build();
    }
    public static MCDataSource ppgDataQuality(String platformType, String platformId, String deviceId, String version) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("data_quality", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.PPG_DATA_QUALITY.name())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("PPG (Data Quality)")
                        .build())
                .build();
    }
    public static MCDataSource ppgSequenceNumber(String platformType, String platformId, String deviceId, String version, double frequency) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("seq_number", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.PPG_SEQUENCE_NUMBER.name())
                .setDataSourceMetaData(MCDataSourceMetaData.builder().setMetaData("FREQUENCY",String.valueOf(frequency)).build())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("PPG Sequence")
                        .build())
                .build();
    }
    public static MCDataSource ppgRaw(String platformType, String platformId,String deviceId, String version, double frequency) {
        return MCDataSource.registerBuilder()
                .setApplicationInfo("motionsense", BuildConfig.VERSION_NAME)
                .doubleArray()
                .setField("seq_number", MCDataDescriptor.builder().build())
                .setDataSourceType(SensorType.PPG_RAW.name())
                .setDataSourceMetaData(MCDataSourceMetaData.builder().setMetaData("FREQUENCY",String.valueOf(frequency)).build())
                .setPlatformType(platformType)
                .setPlatformId(platformId)
                .setPlatformMetaData(MCPlatformMetaData.builder().setDeviceId(deviceId).setVersionFirmware(version).build())
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("PPG Raw")
                        .build())
                .build();
    }

}
