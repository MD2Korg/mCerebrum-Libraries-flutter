import 'package:phonesensor/phonesensor.dart';

class SensorInfo{

  Map _sensorInfo;
  Future<void> getSensorInfo() async {
    _sensorInfo = await Phonesensor.getSensorInfo;
    print("abc");
  }
  String getDescription(String id){
    return _sensorInfo[id.toLowerCase()]["dataSourceMetaData"]["DESCRIPTION"];
  }
  String getTitle(String id){
    return _sensorInfo[id.toLowerCase()]["dataSourceMetaData"]["TITLE"];
  }
}