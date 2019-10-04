import 'package:flutter/material.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:core/mc_library_extension.dart';
import 'package:phonesensor/mc_library_extension.dart';
import 'package:core/controller/controller.dart';
import 'MainPage.dart';
import 'config.dart';

void main() async {
//  bool res = await AndroidAlarmManager.initialize();
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<MCLibrary> mcLibraries = List();
  CoreController coreController;
  @override
  void initState() {
    super.initState();
    coreController = CoreController();

    mcLibraries.add(CoreLibrary(coreController));
    mcLibraries.add(PhoneSensorLibrary(coreController));
//    mcLibraries.add(MotionSenseLibrary(iData));
    start();
  }

  Future<void> start() async {
    bool hasPermission = await checkPermissions();
    await init();
    await CoreController().replaceConfig(null, Config.getDefault());
    await startBackground();
//    await startBackground();
  }

  Future<void> init() async {
    for (int i = 0; i < mcLibraries.length; i++) {
      if (mcLibraries[i].iExec.init != null) {
        await mcLibraries[i].iExec.init();
      }
    }
  }

  Future<void> startBackground() async {
    for (int i = 0; i < mcLibraries.length; i++) {
      if (mcLibraries[i].iExec.backgroundProcess != null) {
        await mcLibraries[i].iExec.backgroundProcess.start();
      }
    }
  }

  Future<bool> checkPermissions() async {
    for (int i = 0; i < mcLibraries.length; i++) {
      if (mcLibraries[i].iExec.permission != null) {
        bool res = await mcLibraries[i].iExec.permission.hasPermission();
        if (res != true) {
          bool res1 = await mcLibraries[i].iExec.permission.getPermission();
          if (res1 == false) return false;
        }
      }
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MainPage(mcLibraries),
      routes: {
/*
        "main": mcExtensionAPI.mainUI,
        "login": mcExtensionAPI.userInterfaces["login"],
        "storage": mcExtensionAPI.userInterfaces["storage"],
*/
      },
    );
  }
}
