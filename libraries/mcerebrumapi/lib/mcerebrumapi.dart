import 'dart:async';

import 'package:flutter/services.dart';

class Mcerebrumapi {
  static const MethodChannel _channel =
      const MethodChannel('org.md2k.mcerebrumapi.channel');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
