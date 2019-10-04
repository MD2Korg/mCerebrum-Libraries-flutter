import 'package:flutter/material.dart';

import 'MainPage.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
/*
  MCExtensionAPI mcExtensionAPI;
*/

  @override
  void initState() {
    super.initState();
/*
    mcExtensionAPI = MotionSenseAPI.get();
*/
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MainPage(),
      routes: {
/*
        "main":mcExtensionAPI.mainUI,
        "settings":mcExtensionAPI.settingUI,
        "plot": mcExtensionAPI.userInterfaces["plot"]
*/
      },
    );
  }

}
