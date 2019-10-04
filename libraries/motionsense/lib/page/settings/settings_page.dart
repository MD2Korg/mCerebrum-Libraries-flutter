import 'package:flutter/material.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:motionsenselib/page/settings/settings_page.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';

class MySettingsPage extends StatefulWidget {
  final IData iData;
  MySettingsPage(this.iData);
  @override
  _MySettingsPageState createState() => _MySettingsPageState();
}

class _MySettingsPageState extends State<MySettingsPage> {
  void onDone() {
    print("on done");
  }

  @override
  void initState(){
    super.initState();
    start();
  }
  Future<void> start() async{
    Map<String, dynamic> json = await widget.iData.getConfig();
    MotionSenseSettings settings=new MotionSenseSettings.fromJson(json);
    Map<String, dynamic> x = await Navigator.push(
        context,
        new MaterialPageRoute(
            builder: (_context) => new SettingsPage(settings)));
    MotionSenseSettings newSettings = x["settings"];
    bool isEdit = x["edit"];
    if(isEdit)
      await widget.iData.setConfig(newSettings.toJson());
    print("abc");
    Navigator.pop(context);

  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: new Container(),
    );
  }
}

