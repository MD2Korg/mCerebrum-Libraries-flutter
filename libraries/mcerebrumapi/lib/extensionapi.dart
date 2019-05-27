library extensionapi;
import 'package:flutter/material.dart';
import 'package:mcerebrumapi/param.dart';
import 'package:permission_handler/permission_handler.dart';

class MCExtensionAPI {
  String id;
  Map<String, WidgetBuilder> userInterfaces;
  List<PermissionGroup> permissions;
  Future<int> settingsStatus;
  WidgetBuilder settingUI;
  WidgetBuilder mainUI;
  Map<String, Future<dynamic> Function(Param param)> actions;

  MCExtensionAPI(this.id, {this.mainUI, this.userInterfaces, this.permissions, this.settingsStatus, this.settingUI, this.actions});
}
