import 'dart:async';

import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:motionsenselib/motionsenselib.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';

import 'device.dart';

class Sensors{
  int startTimestamp;
//  StreamController<Map<String, dynamic>> streamController=new StreamController();
  StreamSubscription<String> streamSubscription;
  Sensors(){
    startTimestamp = -1;
  }
  Future<void> start(IData iData) async{
    if(startTimestamp !=-1) return;
    Map<String, MCDataSourceResult> mcDataSourceResults;
    MotionSenseSettings settings = MotionSenseSettings.fromJson(await iData.getConfig());
    await MotionSenseLib.setSettings(settings);
    mcDataSourceResults = await _registerDataSources(iData, settings);

    startTimestamp = new DateTime.now().millisecondsSinceEpoch;
/*
    streamController.stream.listen((data) async{
      print("data received");
      String id = data["deviceId"]+"_"+data["sensorType"];
      MCDataSourceResult dataSourceResult = mcDataSourceResults[id];
      MCData d = MCData.create(dataSourceResult, data["timestamp"], data["data"]);
      print(d.uuid+" "+d.data.toString());
      await iData.insertData([d]);
    });
*/
    streamSubscription = MotionSenseLib.listenSensorData.listen((data) {
      print("data received");

/*
      String id = data["deviceId"]+"_"+data["sensorType"];
      MCDataSourceResult dataSourceResult = mcDataSourceResults[id];
      MCData d = MCData.create(dataSourceResult, data["timestamp"], data["data"]);
      print(d.uuid+" "+d.data.toString());
      await iData.insertData([d]);
*/

//      streamController.add(onData);
    }, onError: (){
      print("onError");
    }, onDone: (){print("onDone");});
  }
  Future<void> stop() async{
    startTimestamp = -1;
    MotionSenseLib.setBackgroundService(false);
    streamSubscription.cancel();
/*
    streamController.close();
*/
  }
  Future<Map<String, MCDataSourceResult>> _registerDataSources(IData iData, MotionSenseSettings settings) async{
    Map<String, MCDataSourceResult> mcDataSourceResults = new Map();
    for(int i=0;i<settings.motionSenseDevices.length;i++){
      Map<String, MCDataSource> r = await Device.createDataSources(settings.motionSenseDevices[i]);
      for(String str in r.keys){
        mcDataSourceResults[str] = await iData.registerDataSource(r[str]);
      }
    }
    return mcDataSourceResults;
  }
}