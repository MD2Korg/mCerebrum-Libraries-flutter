import 'dart:async';

import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:phonesensor/data/pdata.dart';
import 'package:phonesensor/phonesensor.dart';

import 'i_sensor.dart';

class MCAccelerometer extends ISensor{
  MCAccelerometer():super("accelerometer");
  StreamSubscription<PData> streamSubscription;

  void start(StreamController<PData> streamController){
    streamSubscription = PhoneSensor.accelerometerEvents.listen((onData){
      streamController.add(onData);
    });
  }
  void stop(){
    streamSubscription.cancel();
  }

  Future<MCDataSource> createDataSource() async{
      MCDataSourceMetaData metaData = MCDataSourceMetaData.create("accelerometer",
          "Accelerometer", "Measures the acceleration force in m/s^2 that is applied to a device on all three physical axes (x, y, and z), including the force of gravity.");
      List<MCDataDescriptor> dataDescriptors = [
        MCDataDescriptor.create(
            "x", "X", "Acceleration force along the x axis (including gravity) in m/s^2", MCDataType.DOUBLE,
            unit: "METER_PER_SECOND_SQUARED"),
        MCDataDescriptor.create(
            "y", "Y", "Acceleration force along the y axis (including gravity) in m/s^2", MCDataType.DOUBLE,
            unit: "METER_PER_SECOND_SQUARED"),
        MCDataDescriptor.create(
            "z", "Z", "Acceleration force along the z axis (including gravity) in m/s^2", MCDataType.DOUBLE,
            unit: "METER_PER_SECOND_SQUARED")
      ];
      return await MCDataSource.create(dataSourceType: "accelerometer",
          platformType: "phone",
          applicationId: "phonesensor",
          dataSourceMetaData: metaData,
          dataDescriptors: dataDescriptors);
    }
}
