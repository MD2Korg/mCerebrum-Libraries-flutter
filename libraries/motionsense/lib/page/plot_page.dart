import 'dart:async';

import 'package:flutter/material.dart';
import 'package:motionsense/data/summary.dart';
import 'package:motionsense/motionsense.dart';
import 'package:motionsense/page/settings_page.dart';
import 'package:motionsense/page/summary_table.dart';

class PlotPage extends StatefulWidget {
  @override
  _PlotPageState createState() => _PlotPageState();
}

class _PlotPageState extends State<PlotPage> {
  String platformType;
  String platformId;
  @override
  void initState() {
    read();
    super.initState();
  }
  Future<void> read() async{
    Map m = await Motionsense.getSettings;
    List l = m["motionsense_devices"];
    if(l!=null && l.length!=0){
      platformType = l[0]["platformType"];
      platformId = l[0]["platformId"];
    }
    setState(() {

    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('MotionSense Plot'),
        ),
        body: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ListTile(
                  title: Text(
                    "Accelerometer",
                    style: TextStyle(fontSize: 16),
                  ),
                  trailing: new OutlineButton(
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.green,
                          onPressed: () async{
                            await Motionsense.plot(platformType,platformId,"ACCELEROMETER");
                          },
                          child: Text("Plot"))
              ),
              ListTile(
                  title: Text(
                    "Gyroscope",
                    style: TextStyle(fontSize: 16),
                  ),
                  trailing: new OutlineButton(
                      shape: new RoundedRectangleBorder(
                          borderRadius: new BorderRadius.circular(10.0)),
                      textColor: Colors.green,
                      onPressed: () async{
                        await Motionsense.plot(platformType,platformId,"GYROSCOPE");
                      },
                      child: Text("Plot"))
              ),
              ListTile(
                  title: Text(
                    "PPG",
                    style: TextStyle(fontSize: 16),
                  ),
                  trailing: new OutlineButton(
                      shape: new RoundedRectangleBorder(
                          borderRadius: new BorderRadius.circular(10.0)),
                      textColor: Colors.green,
                      onPressed: () async{
                        await Motionsense.plot(platformType,platformId,"PPG");
                      },
                      child: Text("Plot"))
              ),
            ]),
      ),
    );
  }
}
