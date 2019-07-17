import 'package:flutter/material.dart';
import 'package:mcerebrumapi/extensionapi.dart';
import 'package:motionsense/motionsense_api.dart';
import 'package:motionsense/motionsense.dart';

class MainPage extends StatelessWidget {
  final MCExtensionAPI api = MotionSenseAPI.get();

  Widget bodyData(BuildContext context) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Column(
              // crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                RaisedButton(
                  child: Text("Main"),
                  onPressed: () async {
                    Navigator.pushNamed(context, "main");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("Settings"),
                  onPressed: () async {
                    Navigator.pushNamed(context, "settings");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("isConfigured"),
                  onPressed: () async {
                    Map m = await Motionsense.getSettings;
                    print("abc");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
              ],
            )
          ],
        ),
      );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "MotionSense Example",
          style: TextStyle(color: Colors.black),
        ),
      ),
      body: SingleChildScrollView(child: bodyData(context)),
    );
  }
}
