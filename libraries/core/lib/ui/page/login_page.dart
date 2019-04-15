import 'package:core/core.dart';
import 'package:core/ui/widgets/common_flushbar.dart';
import 'package:flutter/material.dart';
import 'package:flushbar/flushbar.dart';

class LoginPage extends StatefulWidget{
  _LoginPageState _loginPageState;
  LoginPage(Map<String, dynamic> config){
    _loginPageState = _LoginPageState(config);
  }
  @override
  _LoginPageState createState() =>_loginPageState;
}

class _LoginPageState extends State<LoginPage> {
  final username = TextEditingController();
  final password = TextEditingController();
  final Map<String, dynamic> config;
  _LoginPageState(this.config);
  Flushbar flush;
  BuildContext _context;

  @override
  Widget build(BuildContext context) {
    _context = context;
    return Scaffold(
    body: Center(child: loginBody(context)),
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
/*
          Icon(
            FontAwesomeIcons.heartbeat,
            color: Colors.amber,
            size: 80,
          ),
*/
          SizedBox(
            height: 20,
          ),
          Text(
            "Login to Cerebral Cortex",
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
                  tryLogin(context, username.text, password.text, config["core_login_serverAddress"]);
                },
              ),
            ),
            SizedBox(
              height: 10.0,
            ),
            Container(
              padding: EdgeInsets.symmetric(vertical: 0.0, horizontal: 30.0),
              child: FlatButton(
                padding: EdgeInsets.all(20.0),
                child: Text(
                  "Skip",
                  style: TextStyle(color: Colors.blueAccent),
                ),
                onPressed: () {
                  Navigator.pop(context, false);
                },
              ),
            ),

 /*           Text(
              "or",
              style: TextStyle(color: Colors.grey, fontSize: 16),
            ),
            SizedBox(
              height: 10.0,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                Container(
                  child: RaisedButton(
                    padding:
                        EdgeInsets.only(left: 50, top: 8, right: 50, bottom: 8),
                    shape: StadiumBorder(),
                    child: Column(
                      // Replace with a Row for horizontal icon + text
                      children: <Widget>[
                        Icon(FontAwesomeIcons.google),
                        Text("Google")
                      ],
                    ),
                    color: Colors.red,
                    onPressed: () {},
                  ),
                ),
                Container(
                  child: RaisedButton(
                    padding:
                        EdgeInsets.only(left: 50, top: 8, right: 50, bottom: 8),
                    shape: StadiumBorder(),
                    child: Column(
                      // Replace with a Row for horizontal icon + text
                      children: <Widget>[
                        Icon(FontAwesomeIcons.facebook),
                        Text("Facebook")
                      ],
                    ),
                    color: Color.fromRGBO(139, 157, 195, 100),
                    onPressed: () {},
                  ),
                ),
              ],
            ),*/
            SizedBox(
              height: 80.0,
            ),
          ],
        ),
      );

/*
  void prepareConfig(BuildContext context) {
*/
/*
    showDialog(
      context: context,
      barrierDismissible: false,
      child: new Dialog(
        child: new Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            new CircularProgressIndicator(),
            new Text("Preparing..."),
          ],
        ),
      ),
    );
*//*

    ServerService().downloadConfig().then((onValue) {
//      Navigator.pop(context); //pop dialog
      Navigator.pop(context);
      Navigator.pushNamed(context, UIData.routeMainPage);
    }).catchError((e) {
      CommonFlushBar().showError(context, e);
    });
  }
*/

  void tryLogin(BuildContext context, String username, String password, String serverAddress) async {
    if(flush!=null && !flush.isDismissed())
      flush.dismiss(true);
    if (username.length == 0) {
      flush = CommonFlushBar().showError(context, "Invalid Username");
    } else if (password.length == 0) {
      flush = CommonFlushBar().showError(context, "Invalid Password");
    } else {
      flush = CommonFlushBar().showProgress(context, "Signing in...");
      //"https://odin.md2k.org"
      Core.login(serverAddress, username, password).then((onValue) {
        if(flush!=null && !flush.isDismissed())
          flush.dismiss(true);
//          flush = CommonFlushBar().showSuccess(context, "Login successful");
          Navigator.pop(_context, true);
//        Navigator.pushReplacementNamed(context,UIData.routeMainPage);
//          prepareConfig(context);
      }).catchError((onError) {
        if(flush!=null && !flush.isDismissed())
          flush.dismiss(true);
        flush = CommonFlushBar().showError(context, onError.toString());
      });
    }
  }

  @override
  void dispose() {
    // Clean up the controller when the Widget is disposed
    if(flush!=null && !flush.isDismissed()){
      flush.dismiss(true);
    }
    username.dispose();
    password.dispose();
    super.dispose();
  }
}
