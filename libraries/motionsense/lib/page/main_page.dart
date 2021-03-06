import 'package:flutter/material.dart';

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {

  @override
  void initState() {
    super.initState();
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
          title: const Text('MotionSense2'),
        ),
        body: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ListTile(
//                leading: Icon(Icons.settings),
                title: Text(
                  "Settings",
                  style: TextStyle(fontSize: 16),
                ),
                trailing: new OutlineButton(
                    color: Colors.green,
                    shape: new RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(10.0)),
                    textColor: Colors.green,
                    onPressed: () {
/*
                      Navigator.push(
                          context,
                          new MaterialPageRoute(
                              builder: (_context) => new SettingsPage()));
*/
                    },
                    child: Icon(
                      Icons.settings,
                      color: Colors.green,
                    ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                    ),

/*
                trailing: new FlatButton(
                  textColor: Colors.white,
                  color: Colors.green,
                  onPressed: () {
                    Navigator.push(
                        context,
                        new MaterialPageRoute(
                            builder: (_context) => new SettingsPage()));
                  },
                  child: new Text("Open", style: TextStyle(fontSize: 16)),
                ),
*/
              ),
              Container(
                color: Theme.of(context).highlightColor,
                child: Center(
                  child: Text("Data Summary",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
/*
              Expanded(
                child: new DataSourceTable(_summary.getDevices()),
              )
*/
            ]),
      ),
    );
  }
}
