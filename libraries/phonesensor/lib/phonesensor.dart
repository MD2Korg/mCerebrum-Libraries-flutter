import 'dart:async';
import 'package:flutter/services.dart';

import 'data/pdata.dart';

class Phonesensor {
  static const _PLOT = 'PLOT';

  static const MethodChannel _channel = const MethodChannel('phonesensor');

  static Future<bool> plot(String sensorName) async {
    final res = await _channel.invokeMethod(_PLOT, {"dataSourceType": sensorName});
    return res;
  }

}

const EventChannel _activityTypeEventChannel = EventChannel('org.md2k.phonesensor.channel.sensors.activity_type');
const EventChannel _accelerometerEventChannel = EventChannel('org.md2k.phonesensor.channel.sensors.accelerometer');

Stream<PData> _events;

Stream<PData> get activityTypeEvents {
  if (_events == null) {
    _events = _activityTypeEventChannel
        .receiveBroadcastStream()
        .map(
            (dynamic event) {
              return PData("activityType", event["timestamp"], event["sample"]);
            });
  }
  return _events;
}

Stream<PData> get accelerometerEvents {
  if (_events == null) {
    _events = _accelerometerEventChannel
        .receiveBroadcastStream()
        .map(
            (dynamic event) {
//          Map<String, dynamic> map = event.cast<Map<String, dynamic>();
          return PData("accelerometer",event["timestamp"], event["sample"]);
        });
  }
  return _events;
}
