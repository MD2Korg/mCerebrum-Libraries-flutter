import 'package:core/core.dart';
import 'package:core/ui/page/login_page.dart';
import 'package:core/ui/page/main_page.dart';
import 'package:core/ui/page/storage_page.dart';
import 'package:flutter/material.dart';
import 'package:mcerebrumapi/extensionapi.dart';

class CoreAPI {
  static MCExtensionAPI get() {
    return new MCExtensionAPI(
        "core", mainUI: (BuildContext context) => MainPage(), userInterfaces: {
      "login": (BuildContext context) => LoginPage(),
      "storage": (BuildContext context) => StoragePage(),
    }, actions: {
      "download": Core.changeConfig,
      "check_update": Core.checkUpdate,
    });
  }
}
