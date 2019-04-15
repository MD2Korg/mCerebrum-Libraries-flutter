import 'dart:async';
import 'dart:convert';

import 'package:core/data/config.dart';
import 'package:core/data/login_info.dart';
import 'package:core/data/space_info.dart';
import 'package:flutter/services.dart';
class Core{
  static const String _CHANNEL = "core";
  static const platform = const MethodChannel(_CHANNEL);
  static const String _CHANGE_CONFIG = "CHANGE_CONFIG";
  static const String _CONFIG_INFO = "CONFIG_INFO";
  static const String _CONFIG_LIST = "CONFIG_LIST";
  static const String _LOGIN ="LOGIN";
  static const String _LOGIN_INFO = "LOGIN_INFO";
  static const String _LOGOUT = "LOGOUT";
  static const String _SPACE_INFO = "SPACE_INFO";

  static Future<String> login(String server, String username, String password) async {
    try {
      final String result = await platform.invokeMethod(_LOGIN, {
        "server": server,
        "username": username,
        "password": password
      });
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<void> logout() async {
    try {
      await platform.invokeMethod(_LOGOUT);
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<LoginInfo> getLoginInfo() async {
    try {
      final String result = await platform.invokeMethod(_LOGIN_INFO);
      return LoginInfo.name(jsonDecode(result));
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<Config> getConfigInfo() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG_INFO);
      Config res = Config.name(jsonDecode(result));
      return res;
    } on PlatformException catch (e){
      throw e.code;
    }
  }

  static Future<List> getConfigListCerebralCortex() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG_LIST, {"type": "cerebral_cortex"});
      List l = jsonDecode(result);
      List<Config> configInfo = new List();
      for(int i = 0;i<l.length;i++){
        Config c = Config.name(l[i]);
        configInfo.add(c);
      }
      return configInfo;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<List> getConfigListAsset() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG_LIST, {"type": "asset"});
      List l = jsonDecode(result);
      List<Config> configInfo = new List();
      for(int i = 0;i<l.length;i++){
        Config c = Config.name(l[i]);
        configInfo.add(c);
      }
      return configInfo;
    } on PlatformException catch (e){
      throw e.code;
    }
  }

  static Future<bool> changeConfig(Config configInfo) async {
    try {
      final bool result = await platform.invokeMethod(_CHANGE_CONFIG, {"configInfo":jsonEncode(configInfo.map)});
      return result;
    } on PlatformException catch (e) {
      return false;
    }
  }
  static Future<SpaceInfo> getSpaceInfo() async {
    final String res = await platform.invokeMethod(_SPACE_INFO);
    return new SpaceInfo.name(jsonDecode(res));
  }
  static Future<bool> get setStart async {
    final res = await platform.invokeMethod('START');
    return res;
  }
  static Future<bool> get setStop async {
    final res = await platform.invokeMethod('STOP');
    return res;
  }
  static Future<bool> get isStarted async {
    final res = await platform.invokeMethod('IS_RUNNING');
    return res;
  }
  static Future<bool> get delete async {
    final res = await platform.invokeMethod('DELETE_DATA');
    return res;
  }
  static Future<String> get getDataSources async {
    final res = await platform.invokeMethod('DATASOURCE_INFO');
    return res;
  }

}