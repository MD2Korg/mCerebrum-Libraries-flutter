import 'dart:async';

import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:phonesensor/data/pdata.dart';
import 'package:phonesensor/sensor/activity_type.dart';
import 'package:phonesensor/sensor/gps.dart';

import 'battery.dart';
import 'i_sensor.dart';

class Sensors{
  Map<String,ISensor> sensors;
  Map<String, MCDataSourceResult> mcDataSourceResults;
  int startTimestamp;
  StreamController<PData> streamController=new StreamController();
  Sensors(){
    sensors=new Map<String, ISensor>();
//    sensors["accelerometer"]=MCAccelerometer();
    sensors["battery"]=MCBattery();
    sensors["gps"] = MCGps();
    sensors["activityType"]=MCActivityType();
    startTimestamp = -1;
    mcDataSourceResults=new Map();
  }
  Future<void> start(IData iData) async{
    if(startTimestamp!=-1) return;
    for (ISensor data in sensors.values) {
      MCDataSourceResult m = await iData.registerDataSource(data.getDataSource());
      mcDataSourceResults[data.id]=m;
    }

    startTimestamp = new DateTime.now().millisecondsSinceEpoch;
    streamController.stream.listen((data) async{
      await iData.insertData([MCData.create(mcDataSourceResults[data.id], data.timestamp, data.data)]);
      print(data.id+" "+data.timestamp.toString()+" "+data.data.toString());

    });
    sensors.forEach((k,v) async{
      v.start(streamController);
    });

  }
  Future<void> stop() async{
    startTimestamp = -1;
    sensors.forEach((k,v){
      v.stop();
    });

    streamController.close();
  }
  ISensor getSensor(String id){
    return sensors[id];
  }

  Future<void> init() async{
    for (ISensor data in sensors.values) {
      await data.init();
    }
  }
}
