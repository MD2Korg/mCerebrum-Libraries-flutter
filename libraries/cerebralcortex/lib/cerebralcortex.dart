import 'dart:async';

import 'package:flutter/services.dart';

class Cerebralcortex {
  static const MethodChannel _channel =
      const MethodChannel('cerebralcortex');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
