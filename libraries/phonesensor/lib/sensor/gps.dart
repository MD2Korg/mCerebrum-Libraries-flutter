import 'dart:async';

import 'package:location/location.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:phonesensor/data/pdata.dart';

import 'i_sensor.dart';

class MCGps extends ISensor{
  MCGps():super("gps");
  var location = new Location();
  StreamSubscription<LocationData> streamSubscription;

  void start(StreamController<PData> streamController){
    location.changeSettings(distanceFilter: 1);
    streamSubscription = location.onLocationChanged().listen((LocationData currentLocation) {
      streamController.add(new PData(id, new DateTime.now().millisecondsSinceEpoch, [currentLocation.latitude, currentLocation.longitude]));
    });
  }
  void stop(){
    streamSubscription.cancel();
  }

  Future<MCDataSource> createDataSource() async{
      MCDataSourceMetaData metaData = MCDataSourceMetaData.create("gps",
          "gps", "Measures latitude and longitude");
      List<MCDataDescriptor> dataDescriptors = [
        MCDataDescriptor.create(
            "latitude", "Latitude", "measures the latitude", MCDataType.DOUBLE,
            unit: "degree"),
        MCDataDescriptor.create(
            "longitude", "Longitude", "measures the latitude", MCDataType.DOUBLE,
            unit: "degree")
      ];
      return await MCDataSource.create(dataSourceType: "GPS",
          platformType: "PHONE",
          applicationId: "phonesensor",
          dataSourceMetaData: metaData,
          dataDescriptors: dataDescriptors);
    }
}