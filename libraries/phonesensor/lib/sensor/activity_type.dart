import 'dart:async';

import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:phonesensor/data/pdata.dart';
import 'package:phonesensor/phonesensor.dart';

import 'i_sensor.dart';

class MCActivityType extends ISensor{
  MCActivityType():super("activityType");
  StreamSubscription<PData> streamSubscription;
  void start(StreamController<PData> streamController){
    streamSubscription = PhoneSensor.activityTypeEvents.listen((onData){
      streamController.add(onData);
    });  }
  void stop(){

    streamSubscription.cancel();

  }
  double getActivityType(String type){
    switch(type){
      case "STILL":return 0.0;
      case "ON_FOOT":return 1.0;
      case "WALKING":return 2.0;
      case "RUNNING":return 3.0;
      case "ON_BICYCLE":return 4.0;
      case "IN_VEHICLE":return 5.0;
      case "TILTING":return 6.0;
      default:return 7.0;
    }
  }

  @override
  Future<MCDataSource> createDataSource() async{
      MCDataSourceMetaData metaData = MCDataSourceMetaData.create("activityType",
          "ActivityType", "The detected activity of the device (Detected activity: STILL(0), ON_FOOT(1), WALKING(2), RUNNING(3), ON_BICYCLE(4), IN_VEHICLE(5), TILTING(6), UNKNOWN(7)");
      List<MCDataDescriptor> dataDescriptors = [
        MCDataDescriptor.create(
            "activitytype", "ActivityType", "The detected activity of the device (Detected activity: STILL(0), ON_FOOT(1), WALKING(2), RUNNING(3), ON_BICYCLE(4), IN_VEHICLE(5), TILTING(6), UNKNOWN(7)", MCDataType.DOUBLE,),
        MCDataDescriptor.create(
          "confidence", "confidence", "Confidence of the detected activity", MCDataType.DOUBLE,),
      ];
      return await MCDataSource.create(dataSourceType: "ACTIVITY_TYPE",
          platformType: "PHONE",
          applicationId: "phonesensor",
          dataSourceMetaData: metaData,
          dataDescriptors: dataDescriptors);
  }
}