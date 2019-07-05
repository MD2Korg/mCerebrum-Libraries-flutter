import 'dart:async';
import 'dart:convert';

import 'package:core/data/space_info.dart';
import 'package:flutter/services.dart';

import 'data/config.dart';
class Core{
  static const String _CHANNEL = "core";
  static const platform = const MethodChannel(_CHANNEL);
  static const String _CHANGE_CONFIG = "CHANGE_CONFIG";
  static const String _CHECK_UPDATE_CONFIG = "CHECK_UPDATE_CONFIG";
  static const String _UPDATE_CONFIG = "UPDATE_CONFIG";
  static const String _LOGIN ="LOGIN";
  static const String _CONFIG = "CONFIG";
  static const String _LOGOUT = "LOGOUT";
  static const String _SPACE_INFO = "SPACE_INFO";
  static const String _IS_RUNNING = "IS_RUNNING";
  static const String _DELETE_DATA = "DELETE_DATA";
  static const String _DATASOURCE_INFO = "DATASOURCE_INFO";
  static const String _START = "START";
  static const String _STOP = "STOP";

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

  static Future<Config> getConfig() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG);
      return Config.name(jsonDecode(result));
    } on PlatformException catch (e){
      throw e.code;
    }
  }

/*
  static Future<List> getConfigListCerebralCortex() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG_LIST, {"type": "cerebral_cortex"});
      List l = jsonDecode(result);
      List<ConfigInfo> configInfo = new List();
      for(int i = 0;i<l.length;i++){
        ConfigInfo c = ConfigInfo.name(l[i]);
        configInfo.add(c);
      }
      return configInfo;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
*/
/*
  static Future<List> getConfigListAsset() async {
    try {
      final String result = await platform.invokeMethod(_CONFIG_LIST, {"type": "asset"});
      List l = jsonDecode(result);
      List<ConfigInfo> configInfo = new List();
      for(int i = 0;i<l.length;i++){
        ConfigInfo c = ConfigInfo.name(l[i]);
        configInfo.add(c);
      }
      return configInfo;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
*/
  static Future<Map> checkUpdate(var a) async {
    try {
      final Map result = await platform.invokeMethod(
          _CHECK_UPDATE_CONFIG);
      return result;
    } on PlatformException catch (e) {
      return Map();
    }
  }

  static Future<bool> updateConfig(var a) async {
    try {
      final bool result = await platform.invokeMethod(
          _UPDATE_CONFIG);
      return result;
    } on PlatformException catch (e) {
      return false;
    }
  }

  static Future<bool> changeConfig(filename) async {
    try {
      final bool result = await platform.invokeMethod(
          _CHANGE_CONFIG, {"filename": filename});
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
    final res = await platform.invokeMethod(_START);
    return res;
  }
  static Future<bool> get setStop async {
    final res = await platform.invokeMethod(_STOP);
    return res;
  }
  static Future<bool> get isStarted async {
    final res = await platform.invokeMethod(_IS_RUNNING);
    return res;
  }
  static Future<bool> get delete async {
    final res = await platform.invokeMethod(_DELETE_DATA);
    return res;
  }
  static Future<String> get getDataSources async {
    final res = await platform.invokeMethod(_DATASOURCE_INFO);
    return res;
  }

}