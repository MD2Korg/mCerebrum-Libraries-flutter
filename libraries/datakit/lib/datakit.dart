import 'dart:async';

import 'package:flutter/services.dart';

class Datakit {
  static const MethodChannel _channel =
      const MethodChannel('datakit');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<List<int>> get getSpaceInfo async {
/*
    List<int> res = new List();
    res.add(132322342300);
    res.add(523422223230);
    res.add(123487823230);
    res.add(42322329880);
*/
   final List<int> res = await _channel.invokeMethod('spaceInfo');
    return res;
  }
  static Future<bool> get setStart async {
    final res = await _channel.invokeMethod('start');
    return res;
  }
  static Future<bool> get setStop async {
    final res = await _channel.invokeMethod('stop');
    return res;
  }
  static Future<bool> get isStarted async {
    final res = await _channel.invokeMethod('isStarted');
    return res;
  }
  static Future<bool> get delete async {
    final res = await _channel.invokeMethod('delete');
    return res;
  }
  static Future<String> get getDataSources async {
    final res = await _channel.invokeMethod('datasources');
    return res;
  }

}
