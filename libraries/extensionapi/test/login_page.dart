import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget{
  @override
  _LoginPageState createState() =>_LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final username = TextEditingController();
  final password = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 4.0,
        backgroundColor: Colors.black,
        actions: <Widget>[
          SizedBox(
            width: 5.0,
          ),
          IconButton(
            onPressed: () {

//              this.widget._callback("CANCEL");
//              Navigator.pushReplacementNamed(context,UIData.routeMainPage);
            },
            icon: Icon(Icons.close),
          ),
        ],
      ),
      body: Container(
        // Add box decoration
        decoration: BoxDecoration(
          // Box decoration takes a gradient
          gradient: LinearGradient(
            // Where the linear gradient begins and ends
            begin: Alignment.topLeft,
            end: Alignment.bottomLeft,
            // Add one stop for each color. Stops should increase from 0 to 1
//        stops: [0.1, 0.5, 0.7, 0.9],
            colors: [
              // Colors are easy thanks to Flutter's Colors class.
              Colors.black,
              Colors.teal.shade900,
            ],
          ),
        ),
        child: Center(child: loginBody(context)),
      ),
    );
  }

  loginBody(BuildContext context) => SingleChildScrollView(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[loginHeader(), loginFields(context)],
    ),
  );

  loginHeader() => Column(
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: <Widget>[
      Text(
        "Welcome to Decisions",
        style: TextStyle(fontSize: 16, color: Colors.amber),
      ),
      SizedBox(
        height: 10,
      ),
    ],
  );

  loginFields(BuildContext context) => Container(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        Container(
          padding: EdgeInsets.symmetric(vertical: 16.0, horizontal: 30.0),
          child: TextField(
            controller: username,
            maxLines: 1,
            decoration: InputDecoration(
                labelText: "Username",
                icon: new Icon(Icons.person_outline)),
          ),
        ),
        Container(
          padding: EdgeInsets.symmetric(vertical: 0.0, horizontal: 30.0),
          child: TextField(
            controller: password,
            maxLines: 1,
            obscureText: true,
            decoration: InputDecoration(
                labelText: "Password", icon: new Icon(Icons.lock_outline)),
          ),
        ),
        SizedBox(
          height: 30.0,
        ),
        Container(
          padding: EdgeInsets.symmetric(vertical: 0.0, horizontal: 30.0),
          width: double.infinity,
          child: RaisedButton(
            padding: EdgeInsets.all(20.0),
            shape: StadiumBorder(),
            child: Text(
              "SIGN IN",
              style: TextStyle(color: Colors.white),
            ),
            color: Colors.teal,
            onPressed: () {
              tryLogin(context, username.text, password.text);
              //                  Navigator.pop(context);
//                  Navigator.pushNamed(context, UIData.routeStepSettings);
            },
          ),
        ),
        SizedBox(
          height: 10.0,
        ),
        SizedBox(
          height: 80.0,
        ),
      ],
    ),
  );

  void tryLogin(BuildContext context, String username, String password) async {
  }

  @override
  void dispose() {
    // Clean up the controller when the Widget is disposed
    username.dispose();
    password.dispose();
    super.dispose();
  }
}
