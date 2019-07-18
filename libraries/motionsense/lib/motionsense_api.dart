import 'package:flutter/material.dart';
import 'package:mcerebrumapi/extensionapi.dart';
import 'package:motionsense/page/main_page.dart';
import 'package:motionsense/page/plot_page.dart';
import 'package:motionsense/page/settings_page.dart';

class MotionSenseAPI {
  static MCExtensionAPI get() {
    Map<String, WidgetBuilder> m = new Map();
    m["plot"] = (BuildContext context) => PlotPage();
    return new MCExtensionAPI("motionsense",
        mainUI: (BuildContext context) => MainPage(),
        settingUI: (BuildContext context) => SettingsPage(),
        userInterfaces: m);
  }
}
