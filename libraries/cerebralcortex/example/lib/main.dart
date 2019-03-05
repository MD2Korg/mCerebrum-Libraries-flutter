import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:cerebralcortex/cerebralcortex.dart';
import 'package:cerebralcortex/cerebralcortex_api.dart';
import 'package:extensionapi/extensionapi.dart';
void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  MCExtensionAPI mcExtensionAPILibrary;

  @override
  void initState() {
    super.initState();
    initPlatformState();
    mcExtensionAPILibrary = CerebralCortexAPI.get();

    print("abc");
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Cerebralcortex.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: mcExtensionAPILibrary.userInterfaces[0].widget,
//          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
