import 'package:cerebralcortex/cerebralcortex_api.dart';
import 'package:extensionapi/extensionapi.dart';
import 'package:flutter/material.dart';
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
    mcExtensionAPI = CerebralCortexAPI.get();
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: mcExtensionAPI.userInterfaces[0].widget,
//          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
