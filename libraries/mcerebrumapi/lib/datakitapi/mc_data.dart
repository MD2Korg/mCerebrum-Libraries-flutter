import 'package:equatable/equatable.dart';

import 'mc_datasource_result.dart';

class MCData extends Equatable{
  final String uuid;
  final int timestamp;
  final Map<String, dynamic> data;

  static MCData create(MCDataSourceResult dataSourceResult, List<dynamic> data, {int timestamp}){
    Map<String, dynamic> d = new Map();
    if(timestamp==null || timestamp==0) timestamp = DateTime.now().millisecondsSinceEpoch;
    for(int i=0;i<dataSourceResult.dataSource.dataDescriptors.length;i++)
      d[dataSourceResult.dataSource.dataDescriptors[i].id]=data[i];
    MCData mcData = MCData._internal(dataSourceResult.uuid, timestamp, d);
    return mcData;
  }
  MCData._internal(this.uuid, this.timestamp, this.data);

  @override
  List<Object> get props => [uuid, timestamp, data];

}