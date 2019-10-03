import 'package:equatable/equatable.dart';
import 'package:package_info/package_info.dart';

import 'mc_datatype.dart';

class MCDataSourceMetaData extends Equatable{
  final String id;
  final String title;
  final String description;
  final Map<String, dynamic> metaData;

  MCDataSourceMetaData._internal(this.id, this.title, this.description, this.metaData);

  static MCDataSourceMetaData create(String id, String title, String description, {Map<String, dynamic> other}) {
    Map<String, dynamic> m = new Map();
    if(other!=null) m.addAll(other);
    return MCDataSourceMetaData._internal(id, title, description, m);
  }
  factory MCDataSourceMetaData.fromJson(Map<String, dynamic> json){
    return MCDataSourceMetaData._internal(
        json['id'], json['title'], json['description'], json['metaData']);
  }
  Map<String, dynamic> toJson()=>{
    'id': id,
    'title': title,
    'description': description,
    'metaData': metaData,
  };

  @override
  List<Object> get props => [id, title, description, metaData];
}
class MCPlatformMetaData extends Equatable{
  final String id;
  final String title;
  final String description;
  final Map<String, dynamic> metaData;

  MCPlatformMetaData._internal(this.id, this.title, this.description, this.metaData);

  static Future<MCPlatformMetaData> create(String id, String title, String description, {Map<String, dynamic> other}) async{
    Map m = new Map();
    if(other!=null) m.addAll(other);

    return MCPlatformMetaData._internal(id, title, description, m);
  }
  factory MCPlatformMetaData.fromJson(Map<String, dynamic> json){
    return MCPlatformMetaData._internal(
        json['id'], json['title'], json['description'], json['metaData']);
  }

  Map<String, dynamic> toJson()=>{
    'id': id,
    'title': title,
    'description': description,
    'metaData': metaData,
  };

  @override
  List<Object> get props =>[id, title, description, metaData];

}
class MCPlatformAppMetaData extends Equatable{
  final String id;
  final String title;
  final String description;
  final Map<String, dynamic> metaData;

  MCPlatformAppMetaData._internal(this.id, this.title, this.description, this.metaData);

  static Future<MCPlatformAppMetaData> create(String id, String title, String description, {Map<String, dynamic> other}) async{
    Map m = new Map();
    if(other!=null) m.addAll(other);
    return MCPlatformAppMetaData._internal(id, title, description, m);
  }
  factory MCPlatformAppMetaData.fromJson(Map<String, dynamic> json){
    return MCPlatformAppMetaData._internal(
        json['id'], json['title'], json['description'], json['metaData']);
  }

  Map<String, dynamic> toJson()=>{
    'id': id,
    'title': title,
    'description': description,
    'metaData': metaData,
  };

  @override
  List<Object> get props => [id, title, description, metaData];
}
class MCApplicationMetaData extends Equatable{
  final String id;
  final String title;
  final String description;
  final String version;
  final Map<String, dynamic> metaData;

  MCApplicationMetaData._internal(this.id, this.title, this.description, this.version, this.metaData);

  static Future<MCApplicationMetaData> create({Map<String, dynamic> other}) async{
    Map<String, dynamic> m= new Map();
    if(other!=null) m.addAll(other);
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    String id=packageInfo.packageName;
    String title=packageInfo.appName;
    String description = packageInfo.appName+" (packageName = "+packageInfo.packageName+", version="+packageInfo.version+")";
    return MCApplicationMetaData._internal(id, title, description, packageInfo.version, m);
  }
  factory MCApplicationMetaData.fromJson(Map<String, dynamic> json){
    return MCApplicationMetaData._internal(
        json['id'], json['title'], json['description'], json['version'],json['metaData']);
  }

  Map<String, dynamic> toJson()=>{
    'id': id,
    'title': title,
    'description': description,
    'version': version,
    'metaData': metaData,
  };

  @override
  List<Object> get props => [id, title, description, version, metaData];
}
class MCDataDescriptor extends Equatable{
  final String id;
  final String title;
  final String description;
  final MCDataType dataType;
  final Map<String, dynamic> metaData;

  static const String _MIN_VALUE = "min_value";
  static const String _MAX_VALUE = "max_value";
  static const String _UNIT = "unit";
  factory MCDataDescriptor.fromJson(Map<String, dynamic> json){
    return MCDataDescriptor._internal(
        json['id'], json['title'], json['description'], MCDataType.fromString(json['dataType']),json['metaData']);
  }

  Map<String, dynamic> toJson()=>{
    'id': id,
    'title': title,
    'description': description,
    'dataType': dataType.toString(),
    'metaData': metaData,
  };

  String getUnit(){
    return metaData[_UNIT];
  }
  double getMinValue(){
    return metaData[_MIN_VALUE];
  }
  double getMaxValue(){
    return metaData[_MAX_VALUE];
  }
  MCDataDescriptor._internal(this.id, this.title, this.description, this.dataType, this.metaData);

  static MCDataDescriptor create(String id, String title, String description, MCDataType dataType, {double minValue, double maxValue, String unit, Map<String, dynamic> other}){
    Map<String, dynamic> m = new Map();
    if(other!=null) m.addAll(other);
    if(minValue!=null)
      m[_MIN_VALUE]=minValue;
    if(maxValue!=null)
      m[_MAX_VALUE]=maxValue;
    if(unit!=null) m[_UNIT]=unit;
    return MCDataDescriptor._internal(id, title, description,dataType, m);
  }

  @override
  List<Object> get props => [id, title, description, dataType.toString(), metaData];
}

