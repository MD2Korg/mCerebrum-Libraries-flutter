import 'package:flutter/material.dart';
import 'dart:async';
import 'package:extensionapi/extensionapi.dart';
import 'package:core/core_api.dart';

import 'package:flutter/services.dart';
import 'package:core/core.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  MCExtensionAPI mcExtensionAPI;

  @override
  void initState() {
    super.initState();
    mcExtensionAPI = CoreAPI.get();
  }

  @override
  Widget build(BuildContext context) {
    Map<String, dynamic> config = new Map();
    config["serverAddress"]="https://odin.md2k.org";
    return MaterialApp(
      home: mcExtensionAPI.userInterfaces[0].widget(config),
    );
  }
}
