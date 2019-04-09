import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class Phonesensor {
  static const _RUNNING_TIME = 'RUNNING_TIME';
  static const _SUMMARY = "SUMMARY";
  static const _GET_SETTINGS = 'GET_SETTINGS';
  static const _SET_SETTINGS = 'SET_SETTINGS';
  static const _GET_SENSOR_INFO = "GET_SENSOR_INFO";
  static const _BACKGROUND_SERVICE = 'BACKGROUND_SERVICE';
  static const _PLOT = 'PLOT';

  static const MethodChannel _channel = const MethodChannel('phonesensor');

  static Future<int> get getRunningTime async {
    final res = await _channel.invokeMethod(_RUNNING_TIME);
    return res;
  }

  static Future<Map<String, dynamic>> get getSettings async {
    String str = await _channel.invokeMethod(_GET_SETTINGS);
    final Map<String, dynamic> res = jsonDecode(str);
    return res;
  }

  static Future<bool> setSettings(Map _configuration) async {
    String x = jsonEncode(_configuration);
    final res = await _channel.invokeMethod(_SET_SETTINGS, {"config":x});
    return res;
  }

  static Future<Map<String, dynamic>> get getSensorInfo async {
    final res = await _channel.invokeMethod(_GET_SENSOR_INFO);
    Map<String, dynamic> x = jsonDecode(res);
    return x;
  }
  static Future<Map<String, dynamic>> getSummary() async {
    final res = await _channel.invokeMethod(_SUMMARY);
    Map<String, dynamic> x = jsonDecode(res);
    return x;
  }
  static Future<bool> setBackgroundService(bool run) async {
    final res = await _channel.invokeMethod(_BACKGROUND_SERVICE, {"run":run});
    return res;
  }
  static Future<bool> plot(String sensorName) async {
    final res = await _channel.invokeMethod(_PLOT, {"dataSourceType": sensorName});
    return res;
  }

}
