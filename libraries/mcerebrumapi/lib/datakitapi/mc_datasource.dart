import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:package_info/package_info.dart';

import 'mc_data_format.dart';
import 'mc_metadata.dart';

class MCDataSource extends Equatable {
  static const String _SEPARATOR = "-";

  final String dataSourceType;
  final String dataSourceId;
  final String platformType;
  final String platformId;
  final String platformAppType;
  final String platformAppId;
  final String applicationType;
  final String applicationId;

  final MCDataSourceMetaData dataSourceMetaData;
  final MCPlatformMetaData platformMetaData;
  final MCPlatformAppMetaData platformAppMetaData;
  final MCApplicationMetaData applicationMetaData;
  final List<MCDataDescriptor> dataDescriptors;

  final MCDataFormat dataFormat;

  MCDataSource._internal(
      {this.dataSourceType,
      this.dataSourceId,
      this.platformType,
      this.platformId,
      this.platformAppType,
      this.platformAppId,
      this.applicationType,
      this.applicationId,
        this.dataFormat,
      this.dataSourceMetaData,
      this.platformMetaData,
      this.platformAppMetaData,
      this.applicationMetaData,
      this.dataDescriptors});

  static MCDataSource query(
      {String dataSourceType,
      String dataSourceId,
      String platformType,
      String platformId,
      String platformAppType,
      String platformAppId,
      String applicationType,
      String applicationId}) {
    return new MCDataSource._internal(
        dataSourceType: dataSourceType,
        dataSourceId: dataSourceId,
        platformType: platformType,
        platformId: platformId,
        platformAppType: platformAppType,
        platformAppId: platformAppId,
        applicationType: applicationType,
        applicationId: applicationId);
  }

  static Future<MCDataSource> create({
    @required String dataSourceType,
    String dataSourceId,
    String platformType,
    String platformId,
    String platformAppType,
    String platformAppId,
    String applicationId,
    MCDataFormat dataFormat=MCDataFormat.DATA,
    @required MCDataSourceMetaData dataSourceMetaData,
    MCPlatformMetaData platformMetaData,
    MCPlatformAppMetaData platformAppMetaData,
    @required List<MCDataDescriptor> dataDescriptors,
  }) async {
    MCApplicationMetaData applicationMetaData =
        await MCApplicationMetaData.create();
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    return new MCDataSource._internal(
        dataSourceType: dataSourceType,
        dataSourceId: dataSourceId,
        platformType: platformType,
        platformId: platformId,
        platformAppType: platformAppType,
        platformAppId: platformAppId,
        applicationType: packageInfo.packageName,
        applicationId: applicationId,
        dataSourceMetaData: dataSourceMetaData,
        platformMetaData: platformMetaData,
        platformAppMetaData: platformAppMetaData,
        applicationMetaData: applicationMetaData,
        dataDescriptors: dataDescriptors);
  }

  bool isSubsetOf(MCDataSource dataSource) {
    if (dataSourceType != null &&
        (dataSource.dataSourceType == null ||
            dataSource.dataSourceType != dataSourceType)) return false;
    if (dataSourceId != null &&
        (dataSource.dataSourceId == null ||
            dataSource.dataSourceId != dataSourceId)) return false;
    if (platformType != null &&
        (dataSource.platformType == null ||
            dataSource.platformType != platformType)) return false;
    if (platformId != null &&
        (dataSource.platformId == null || dataSource.platformId != platformId))
      return false;
    if (platformAppType != null &&
        (dataSource.platformAppType == null ||
            dataSource.platformAppType != platformAppType)) return false;
    if (platformAppId != null &&
        (dataSource.platformAppId == null ||
            dataSource.platformAppId != platformAppId)) return false;
    if (applicationType != null &&
        (dataSource.applicationType == null ||
            dataSource.applicationType != applicationType)) return false;
    if (applicationId != null &&
        (dataSource.applicationId == null ||
            dataSource.applicationId != applicationId)) return false;
    return true;
  }

  factory MCDataSource.fromJson(Map<String, dynamic> json) {
    return MCDataSource._internal(
        dataSourceType: json['dataSourceType'],
        dataSourceId: json['dataSourceId'],
        platformType: json['platformType'],
        platformId: json['platformId'],
        platformAppType: json['platformAppType'],
        platformAppId: json['platformAppId'],
        applicationType: json['applicationType'],
        applicationId: json['applicationId'],
        dataFormat: MCDataFormat.fromString(json['dataFormat']?? "DATA"),
        dataSourceMetaData: json['dataSourceMetaData'] == null
            ? null
            : MCDataSourceMetaData.fromJson(json['dataSourceMetaData']),
        platformMetaData: json['platformMetaData'] == null
            ? null
            : MCPlatformMetaData.fromJson(json['platformMetaData']),
        platformAppMetaData: json['platformAppMetaData'] == null
            ? null
            : MCPlatformAppMetaData.fromJson(json['platformAppMetaData']),
        applicationMetaData: json['applicationMetaData'] == null
            ? null
            : MCApplicationMetaData.fromJson(json['applicationMetaData']),
        dataDescriptors: (json['dataDescriptors'] as List)
            .map((i) => MCDataDescriptor.fromJson(i))
            .toList());
  }

  Map<String, dynamic> toJson() => {
        'dataSourceType': dataSourceType,
        'dataSourceId': dataSourceId,
        'platformType': platformType,
        'platformId': platformId,
        'platformAppType': platformAppType,
        'platformAppId': platformAppId,
        'applicationType': applicationType,
        'applicationId': applicationId,
    'dataFormat': dataFormat==null?"DATA":dataFormat.toString(),
        'dataSourceMetaData': dataSourceMetaData,
        'platformMetaData': platformMetaData,
        'platformAppMetaData': platformAppMetaData,
        'applicationMetaData': applicationMetaData,
        'dataDescriptors': dataDescriptors,
      };


  static MCDataSource fromUUID(String uuid){
    String dataSourceType, dataSourceId, platformType, platformId, platformAppType, platformAppId, applicationType, applicationId;
    List<String> splits = uuid.split(_SEPARATOR);
    if (splits.length > 0 && splits[0] != null && splits[0].length != 0)
      dataSourceType = splits[0];
    if (splits.length > 1 && splits[1] != null && splits[1].length != 0)
      dataSourceId = splits[1];
    if (splits.length > 2 && splits[2] != null && splits[2].length != 0)
      platformType = splits[2];
    if (splits.length > 3 && splits[3] != null && splits[3].length != 0)
      platformId = splits[3];
    if (splits.length > 4 && splits[4] != null && splits[4].length != 0)
      platformAppType = splits[4];
    if (splits.length > 5 && splits[5] != null && splits[5].length != 0)
      platformAppId = splits[5];
    if (splits.length > 6 && splits[6] != null && splits[6].length != 0)
      applicationType = splits[6];
    if (splits.length > 7 && splits[7] != null && splits[7].length != 0)
      applicationId = splits[7];
    return MCDataSource._internal(dataSourceType: dataSourceType, dataSourceId: dataSourceId, platformType: platformType, platformId: platformId, platformAppType: platformAppType, platformAppId: platformAppId, applicationType: applicationType, applicationId: applicationId);
  }


  String getUUID() {
    return _prepString(dataSourceType) +
        _SEPARATOR +
        _prepString(dataSourceId) +
        _SEPARATOR +
        _prepString(platformType) +
        _SEPARATOR +
        _prepString(platformId) +
        _SEPARATOR +
        _prepString(platformAppType) +
        _SEPARATOR +
        _prepString(platformAppId) +
        _SEPARATOR +
        _prepString(applicationType) +
        _SEPARATOR +
        _prepString(applicationId);
  }

  String _prepString(String s) {
    if (s == null)
      return "";
    else
      return s;
  }

  @override
  List<Object> get props => [
    dataSourceType,
    dataSourceId,
    platformType,
    platformId,
    platformAppType,
    platformAppId,
    applicationType,
    applicationId,
    dataFormat.toString(),
    dataSourceMetaData,
    platformMetaData,
    platformAppMetaData,
    applicationMetaData,
    dataDescriptors
  ];
}
