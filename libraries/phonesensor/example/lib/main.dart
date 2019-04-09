import 'package:flutter/material.dart';
import 'package:phonesensor/page/main_page.dart';

void main() => runApp(MaterialApp(
    home: MyApp()));

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
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
              "Open",
              style: TextStyle(color: Colors.white),
            ),
            color: Colors.teal,
            onPressed: () {
              Navigator.push(context,
                  new MaterialPageRoute(
                      builder: (_context) => new MainPage()
                  )
              );
            },
          ),
        ]),
      ),
    );
  }
}
