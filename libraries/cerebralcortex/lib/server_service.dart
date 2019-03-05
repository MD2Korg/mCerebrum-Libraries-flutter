import 'dart:async';

import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ServerService{
  static const platform = const MethodChannel('mcerebrum/server');
  static const String IS_LOGGED_IN = "IS_LOGGED_IN";
  static const String LOGIN ="LOGIN";
  static const String LOGOUT = "LOGOUT";
  static const String DOWNLOAD_CONFIG = "DOWNLOAD_CONFIG";
  static const String CHECK_UPDATE_CONFIG = "CHECK_UPDATE_CONFIG";
  static const String UPDATE_CONFIG = "UPDATE_CONFIG";


  Future<bool> isLoggedIn() async {
    try {
      final bool result = await platform.invokeMethod(IS_LOGGED_IN);
      return result;

    } on PlatformException catch (e) {
      return false;
    }
  }
  Future<String> login(String username, String password) async {
    try {
      final String result = await platform.invokeMethod(LOGIN, {
        "server": "https://odin.md2k.org",
        "username": username,
        "password": password
      });
      return result;
    } on PlatformException catch (e){
      throw e.code;
    }
  }
  Future<String> downloadConfig() async {
    try {
//      var userInfo = ['https://odin.md2k.org',userName, password];
      final String result = await platform.invokeMethod(DOWNLOAD_CONFIG);
      return result;
    } on PlatformException catch (e) {
      return e.message;
    }
  }

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