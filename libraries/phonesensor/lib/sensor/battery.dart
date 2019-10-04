import 'dart:async';

import 'package:battery/battery.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:phonesensor/data/pdata.dart';

import 'i_sensor.dart';

class MCBattery extends ISensor{
  Battery _battery;
  Timer timer;
  MCBattery():super("battery"){
    _battery = Battery();
  }
  void start(StreamController<PData> streamController){
    timer = Timer.periodic(Duration(seconds: 10), (Timer timer) async{
      double value = (await _battery.batteryLevel).toDouble();
      streamController.add(new PData(id, new DateTime.now().millisecondsSinceEpoch, [value]));
    });
  }
  void stop(){
    if(timer!=null)
      timer.cancel();
  }

  @override
  Future<MCDataSource> createDataSource() async{
      MCDataSourceMetaData metaData = MCDataSourceMetaData.create("battery",
          "Battery", "Measures battery level of the phone in percentage");
      List<MCDataDescriptor> dataDescriptors = [
        MCDataDescriptor.create(
            "battery", "Battery Level", "battry level of the phone in percentage",
            MCDataType.DOUBLE, unit: "percentage",
            minValue: 0.0,
            maxValue: 100.0)
      ];
      return await MCDataSource.create(dataSourceType: "BATTERY",
          platformType: "PHONE",
          applicationId: "phonesensor",
          dataSourceMetaData: metaData,
          dataDescriptors: dataDescriptors);
  }
}