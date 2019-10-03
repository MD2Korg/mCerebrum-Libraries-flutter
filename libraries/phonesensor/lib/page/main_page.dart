import 'package:flutter/material.dart';

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
//  Timer _timer;
//  Summary _summary;

  @override
  void initState() {
    super.initState();
/*
    _timer = new Timer.periodic(const Duration(seconds: 1), getSummary);
    _summary = Summary();
*/
  }

/*
  void getSummary(Timer timer) async {
    await _summary.getSummary();
    setState(() {});
  }
*/

  @override
  void dispose() {
//    _timer.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('PhoneSensor'),
        ),
        body: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
/*
              ListTile(
//                  leading: Icon(Icons.play_circle_filled),
                  title: Text(
                    "Data Collection",
                    style: TextStyle(fontSize: 16),
                  ),
                  subtitle: _summary.isRunning()
                      ? Text(_summary.getRunningTime())
                      : null, //Text('stopped'),
                  trailing: !_summary.isRunning()
                      ? new OutlineButton(
                          color: Colors.green,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.green,
                          onPressed: () {
                            Phonesensor.setBackgroundService(true);
                          },
                          child: Icon(
                            Icons.play_circle_outline,
                            color: Colors.green,
                          ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                          )
                      : new OutlineButton(
                          color: Colors.red,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.red,
                          onPressed: () {
                            Phonesensor.setBackgroundService(false);
                          },
                          child: Icon(
                            Icons.pause_circle_outline,
                            color: Colors.red,
                          ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                          )),
*/
/*
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
                      Navigator.push(
                          context,
                          new MaterialPageRoute(
                              builder: (_context) => new SettingsPage(null)));
                    },
                    child: Icon(
                      Icons.settings,
                      color: Colors.green,
                    ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                    ),

*/
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
*//*

              ),
*/
              Container(
                color: Theme.of(context).highlightColor,
                padding: EdgeInsets.all(10),
                child: Center(
                  child: Text("Data Summary",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
/*
              Expanded(
                //TODO: activate
//                child: new DataSourceTable(_summary),
              )
*/
            ]),
      ),
    );
  }
}
