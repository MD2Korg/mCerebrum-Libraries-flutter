import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'login_page.dart';
import 'server_service.dart';

class ServerPage extends StatefulWidget {
  @override
  _ServerPageState createState() => _ServerPageState();
}

class _ServerPageState extends State<ServerPage> {
  bool loggedIn = false;
  String userId="";
  String server="";
  String config="default";
  @override
  void initState() {
    super.initState();
  }

  Future<void> isLoggedIn() async {
    bool res = false;
    // Platform messages may fail, so we use a try/catch PlatformException.
    res = await ServerService.isLoggedIn();
    if (!mounted) return;
    setState(() {
      loggedIn = res;
    });
  }
  Future<void> getUserId() async {
    String res = "";
    // Platform messages may fail, so we use a try/catch PlatformException.
    res = await ServerService.getUserId();
    if (!mounted) return;
    setState(() {
      userId = res;
    });
  }
  Future<void> getServerAddress() async {
    String res = "";
    // Platform messages may fail, so we use a try/catch PlatformException.
    res = await ServerService.getServerAddress();
    if (!mounted) return;
    setState(() {
      server = res;
    });
  }

  Widget profileHeader() => Container(
        height: 120,
        width: double.infinity,
        child: Padding(
          padding: const EdgeInsets.all(10.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Icon(
                Icons.cloud,
                size: 50,
                color: Colors.blue,
              ),
              SizedBox(
                height: 10,
              ),
              Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: <Widget>[
                    Text("User:    " + userId,
                        style: TextStyle(color: Colors.orange)),
                    Text("Config: " + config,
                        style: TextStyle(color: Colors.green)),
                  ]),
              Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: <Widget>[
                    Text("Server: " + server,
                        style: TextStyle(color: Colors.blue)),
                  ]),

              /*
                  Container(
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(50.0),
                        border: Border.all(width: 2.0, color: Colors.white)),
                    child: Icon(Icons.cloud,size: 100,),
                  ),
*/
            ],
          ),
        ),
      );

  Widget widgetNotLoggedIn(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          profileHeader(),
          //1
          Container(
            color: Theme.of(context).highlightColor,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Center(
                child:
                    Text("Settings", style: Theme.of(context).textTheme.title),
              ),
            ),
          ),
          Column(
            children: <Widget>[
              ListTile(
                leading: Icon(
                  FontAwesomeIcons.signInAlt,
                  color: Colors.green,
                ),
                title: Text("Sign in"),
                  onTap: () {
                  _navigateLogin(context);
                  }),
            ],
          ),
        ],
      );
  _navigateLogin(BuildContext context) async {
    // Navigator.push returns a Future that will complete after we call
    // Navigator.pop on the Selection Screen!
    final result = await Navigator.push(
      context,
      // We'll create the SelectionScreen in the next step!
      MaterialPageRoute(builder: (context) => LoginPage()),
    );
    setState(() {
      loggedIn = result;
    });

  }
  Widget widgetLoggedIn(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          profileHeader(),
          //1
          Container(
            color: Theme.of(context).highlightColor,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Center(
                child:
                    Text("Settings", style: Theme.of(context).textTheme.title),
              ),
            ),
          ),
          Column(
            children: <Widget>[
              ListTile(
                leading: Icon(
                  FontAwesomeIcons.sync,
                  color: Colors.lightBlueAccent,
                ),
                title: Text("Upload Data"),
              ),
              ListTile(
                leading: Icon(
                  FontAwesomeIcons.upload,
                  color: Colors.purple,
                ),
                title: Text("Uploader Settings"),
                trailing: Icon(Icons.arrow_right),
              ),
              ListTile(
                leading: Icon(
                  FontAwesomeIcons.signOutAlt,
                  color: Colors.grey,
                ),
                title: Text("Sign out"),
              ),
            ],
          ),
        ],
      );

  @override
  Widget build(BuildContext context) {
    if (loggedIn)
      return widgetLoggedIn(context);
    else
      return widgetNotLoggedIn(context);
  }
}
