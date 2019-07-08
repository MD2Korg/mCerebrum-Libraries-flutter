library extensionapi;
import 'package:flutter/material.dart';

class MCExtensionAPI {
  String id;
  Map<String, WidgetBuilder> userInterfaces;
  Future<int> settingsStatus;
  WidgetBuilder settingUI;
  WidgetBuilder mainUI;
  Map<String, Future<dynamic> Function(dynamic param)> actions;

  MCExtensionAPI(this.id, {this.mainUI, this.userInterfaces, this.settingsStatus, this.settingUI, this.actions});
}
