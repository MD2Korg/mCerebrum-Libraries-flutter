import 'package:flutter/material.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:motionsenselib/motionsenselib.dart';
import 'package:motionsenselib/settings/device_settings.dart';

class Device {
  static Future<Map<String, MCDataSource>> createDataSources(DeviceSettings deviceSettings) async {
    Map<String, MCDataSource> dataSources=new Map();
    List list =
        await MotionSenseLib.getSensorInfo(deviceSettings.deviceId);
    for (int i = 0; i < list.length; i++) {
      dataSources[deviceSettings.deviceId+"_"+list[i]["sensorType"]] = await _createDataSource(deviceSettings, list[i]);
    }
    return dataSources;
  }

  static Future<MCDataSource> _createDataSource(
      DeviceSettings deviceSettings, Map sensorInfo) async {
    return MCDataSource.create(
        dataSourceType: sensorInfo["sensorType"],
        platformType: deviceSettings.platformType,
        platformId: deviceSettings.platformId,
        applicationId: "motionsense",
        dataSourceMetaData: MCDataSourceMetaData.create(
            sensorInfo["sensorType"].toLowerCase(),
            sensorInfo["title"],
            sensorInfo["description"]),dataDescriptors: _createDataDescriptor(sensorInfo["fields"]));
  }
  static List<MCDataDescriptor> _createDataDescriptor(List fields){
    List<MCDataDescriptor> list = new List();
    for(int i =0;i<fields.length;i++){
      MCDataDescriptor.create(fields[i]["id"], fields[i]["title"], fields[i]["description"], MCDataType.DOUBLE);
    }
    return list;
  }
}
