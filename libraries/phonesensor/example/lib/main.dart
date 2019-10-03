import 'package:flutter/material.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:phonesensor/mc_library_extension.dart';

void main() => runApp(MaterialApp(
    home: MyApp()));

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  MCLibrary lib;

  @override
  void initState() {
    super.initState();
    lib = PhoneSensorLibrary(null);
    getPermission();
  }
  void getPermission() async{
    bool hasPermission = await lib.iExec.permission.hasPermission();
    print("hasPermission = "+hasPermission.toString());
    if(!hasPermission){
      bool newPermission = await lib.iExec.permission.getPermission();
      print("get permission="+newPermission.toString());
    }
    await lib.iExec.init();
    lib.iExec.backgroundProcess.start();
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('PhoneSensor example'),
        ),
        body: Column(children: [
          RaisedButton(
            padding: EdgeInsets.all(20.0),
            shape: StadiumBorder(),
            child: Text(
              "Settings",
              style: TextStyle(color: Colors.white),
            ),
            color: Colors.teal,
            onPressed: () async{
              var res = await Navigator.push(context,
                  new MaterialPageRoute(
                      builder: (_context){
                        return lib.iExec.settings.ui(null);//(/*null,null,null*/)/*new MainPage()*/
                      }
                  )
              );
              print("res = "+res.toString());
            },
          ),
        ]),
      ),
    );
  }
}
