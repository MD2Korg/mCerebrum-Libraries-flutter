import 'dart:async';
import 'dart:io';
import 'dart:isolate';
import 'dart:math';
import 'dart:ui';

import 'package:background_locator/background_locator.dart';
import 'package:background_locator/location_dto.dart';
import 'package:background_locator/location_settings.dart';
import 'package:flutter/material.dart';
import 'package:location_permissions/location_permissions.dart';
import 'package:path_provider/path_provider.dart';

import 'file_manager.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ReceivePort port = ReceivePort();

  String logStr = '';
  bool isRunning;
  LocationDto lastLocation;
  DateTime lastTimeLocation;
  Future<void> printDir() async{
    Directory dir = await getExternalStorageDirectory();
    print("external dir="+dir.path);
//    final directory = await getTemporaryDirectory();
    dir = await getTemporaryDirectory();
    print("temp dir="+dir.path);

  }
  Timer _t;
  Future<String> getDirectory() async{
    if(Platform.isAndroid){
      return (await getExternalStorageDirectory()).path;
    }else{
      return (await getApplicationDocumentsDirectory()).path;
    }
  }
  Future<void> _readFile() async{
    String dir = await getDirectory();
    String path = dir+"/gps.txt";
    File f = File(path);
    if(f.existsSync()) {
      String size = f.lengthSync().toString();
      String time = f.lastModifiedSync().toIso8601String();
      logStr = "\n\n\nFileSize="+size+"\n\n\nlast modified="+time;
    }
    else logStr = "File not found";
    setState(() {

    });
  }
  @override
  void initState() {
    super.initState();
    _t = Timer.periodic(Duration(seconds: 1), (v){

      _readFile();

    });

    printDir();

    IsolateNameServer.registerPortWithName(port.sendPort, 'LocatorIsolate');
    port.listen(
      (dynamic data) async {
        print("listening...");
//        await setLog(data);
        await updateUI(data);
      },
      onError: (err) {
        print("Error log in dart: $err");
      },
      cancelOnError: false,
    );
    initPlatformState();
  }

  @override
  void dispose() {
    IsolateNameServer.removePortNameMapping('LocatorIsolate');
    _t.cancel();
    super.dispose();
  }

  static double dp(double val, int places) {
    double mod = pow(10.0, places);
    return ((val * mod).round().toDouble() / mod);
  }

  static String formatDateLog(DateTime date) {
    return date.hour.toString() +
        ":" +
        date.minute.toString() +
        ":" +
        date.second.toString();
  }

  static String formatLog(LocationDto locationDto) {
    return dp(locationDto.latitude, 4).toString() +
        " " +
        dp(locationDto.longitude, 4).toString();
  }

  Future<void> setLog(LocationDto data) async {
    final date = DateTime.now();
    await FileManager.writeToLogFile(
        '${formatDateLog(date)} --> ${formatLog(data)}\n');
  }

  Future<void> updateUI(LocationDto data) async {
    final log = await FileManager.readLogFile();
    setState(() {
      lastLocation = data;
      lastTimeLocation = DateTime.now();
      logStr = log;
    });
  }

  Future<void> initPlatformState() async {
    print('Initializing...');
    await BackgroundLocator.initialize();
    logStr = await FileManager.readLogFile();
    print('Initialization done');
    final _isRunning = await BackgroundLocator.isRegisterLocationUpdate();
    setState(() {
      isRunning = _isRunning;
    });
    print('Running ${isRunning.toString()}');
  }

  static void callback(LocationDto locationDto, String directory) async {
    print("dir = "+directory);
    print('location in dart: ${locationDto.toString()}');
    final SendPort send = IsolateNameServer.lookupPortByName('LocatorIsolate');
    final date = DateTime.now();
    await FileManager.writeToLogFile(
        '${formatDateLog(date)} --> ${formatLog(locationDto)}\n');
    send?.send(locationDto);
  }

  static void notificationCallback() {
    print('notificationCallback');
  }

  @override
  Widget build(BuildContext context) {
    final start = SizedBox(
      width: double.maxFinite,
      child: RaisedButton(
        child: Text('Start'),
        onPressed: () {
          _checkLocationPermission();
        },
      ),
    );
    final stop = SizedBox(
      width: double.maxFinite,
      child: RaisedButton(
        child: Text('Stop'),
        onPressed: () {
          BackgroundLocator.unRegisterLocationUpdate();
          setState(() {
            isRunning = false;
          });
        },
      ),
    );
    final clear = SizedBox(
      width: double.maxFinite,
      child: RaisedButton(
        child: Text('Clear Log'),
        onPressed: () {
          FileManager.clearLogFile();
          setState(() {
            logStr = '';
          });
        },
      ),
    );
    String msgStatus = "-";
    if (isRunning != null) {
      if (isRunning) {
        msgStatus = 'Is running';
      } else {
        msgStatus = 'Is not running';
      }
    }
    final status = Text("Status: $msgStatus");

    final log = Text(
      logStr,
    );

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter background Locator'),
        ),
        body: Container(
          width: double.maxFinite,
          padding: const EdgeInsets.all(22),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[start, stop, clear, status, log],
            ),
          ),
        ),
      ),
    );
  }

  void _checkLocationPermission() async {
    final access = await LocationPermissions().checkPermissionStatus();
    switch (access) {
      case PermissionStatus.unknown:
      case PermissionStatus.denied:
      case PermissionStatus.restricted:
        final permission = await LocationPermissions().requestPermissions(
          permissionLevel: LocationPermissionLevel.locationAlways,
        );
        if (permission == PermissionStatus.granted) {
          _startLocator((await getDirectory()));
        } else {
          // show error
        }
        break;
      case PermissionStatus.granted:

        _startLocator((await getDirectory()));
        break;
    }
  }

  void _startLocator(String directory) {
    BackgroundLocator.registerLocationUpdate(
      callback,
      directory,
      androidNotificationCallback: notificationCallback,
      settings: LocationSettings(
        notificationTitle: "Start Location Tracking example",
        notificationMsg: "Track location in background exapmle",
        wakeLockTime: 20,
        autoStop: false,
      ),
    );
    setState(() {
      isRunning = true;
    });
  }
}
