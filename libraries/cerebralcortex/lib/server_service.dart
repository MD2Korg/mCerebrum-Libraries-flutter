import 'dart:async';

import 'package:flutter/services.dart';

class ServerService{
  static const String CHANNEL = "cerebralcortex";
  static const platform = const MethodChannel(CHANNEL);
  static const String IS_LOGGED_IN = "IS_LOGGED_IN";
  static const String LOGIN ="LOGIN";
  static const String LOGOUT = "LOGOUT";
  static const String USER_ID = "USER_ID";
  static const String SERVER_ADDRESS = "SERVER_ADDRESS";

  static const String CONFIG_LIST = "CONFIG_LIST";
  static const String CONFIG_CURRENT = "CONFIG_CURRENT";
  static const String CONFIG_DOWNLOAD = "CONFIG_DOWNLOAD";
  static const String CONFIG_SERVER = "CONFIG_SERVER";
  static const String CONFIG_UPDATE = "CONFIG_UPDATE";


  static Future<bool> isLoggedIn() async {
    try {
      final bool result = await platform.invokeMethod(IS_LOGGED_IN);
      return result;

    } on PlatformException catch (e) {
      return false;
    }
  }
  static Future<String> login(String server, String username, String password) async {
    try {
      final String result = await platform.invokeMethod(LOGIN, {
        "server": server,
        "username": username,
        "password": password
      });
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<String> getUserId() async {
    try {
      final String result = await platform.invokeMethod(USER_ID);
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<String> getServerAddress() async {
    try {
      final String result = await platform.invokeMethod(SERVER_ADDRESS);
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  static Future<String> getCurrentConfigFilename() async {
    try {
      final String result = await platform.invokeMethod(CONFIG_CURRENT);
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
/*
  static Future<String> getConfigurationFileList() async {
    try {
      final String result = await platform.invokeMethod(CURRENT_CONFIG);
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
*/

/*
  Future<String> downloadConfig() async {
    try {
//      var userInfo = ['https://odin.md2k.org',userName, password];
      final String result = await platform.invokeMethod(DOWNLOAD_CONFIG);
      return result;
    } on PlatformException catch (e) {
      return e.message;
    }
  }
*/


/*
  Future<bool> getLoginShow() async {
    // Simulate a future for response after 2 second.
    SharedPreferences prefs = await SharedPreferences.getInstance();
    if(!prefs.getKeys().contains("init_login"))
      return false;
    bool counter = prefs.getBool('init_login');
    return counter;
  }
  Future<bool> setLoginShow() async {
    // Simulate a future for response after 2 second.
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setBool('init_login', true);
    return true;
  }
*/

  // Logout
  Future<void> logout() async {
    // Simulate a future for response after 1 second.
    return await new Future<void>.delayed(
        new Duration(
            seconds: 1
        )
    );
  }

}