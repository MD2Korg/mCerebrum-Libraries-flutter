import 'package:core/core.dart';
import 'package:core/data/config_info.dart';
import 'package:core/data/login_info.dart';
import 'package:core/data/space_info.dart';
import 'package:core/ui/page/configlist_page.dart';
import 'package:core/ui/page/storage_page.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import 'login_page.dart';

class MainPage extends StatefulWidget {
  _MainPageState _mainPageState;

  MainPage(Map<String, dynamic> config) {
    _mainPageState = _MainPageState(config);
  }

  @override
  _MainPageState createState() => _mainPageState;
}

class _MainPageState extends State<MainPage> {
  LoginInfo loginInfo = new LoginInfo();
  ConfigInfo configInfo = new ConfigInfo();
  SpaceInfo spaceInfo = new SpaceInfo();
  Map<String, dynamic> config;

  _MainPageState(config) {
    this.config = config;
  }

  @override
  void initState() {
    super.initState();
    getConfigInfo();
    getLoginInfo();
    getSpaceInfo();
  }

  Future<void> getLoginInfo() async {
    loginInfo = await Core.getLoginInfo();
    if (!mounted) return;
    setState(() {});
  }
  Future<void> getConfigInfo() async {
    configInfo = await Core.getConfigInfo();
    if (!mounted) return;
    setState(() {});
  }
  Future<void> getSpaceInfo() async {
    spaceInfo = await Core.getSpaceInfo();
    if (!mounted) return;
    setState(() {});
  }

  _navigateConfigList(BuildContext context) async {
    final result = await Navigator.push(
      context,
      // We'll create the SelectionScreen in the next step!
      MaterialPageRoute(
          builder: (context) => ConfigListPage(config)),
    );
    await getConfigInfo();
    setState(() {});
  }
  _navigateStoragePage(BuildContext context) async {
    await Navigator.push(
      context,
      // We'll create the SelectionScreen in the next step!
      MaterialPageRoute(
          builder: (context) => StoragePage()),
    );
    await getSpaceInfo();
    setState(() {});
  }

  _navigateLogin(BuildContext context) async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => LoginPage(config)),
    );
    await getLoginInfo();
  }

  _navigateLogout(BuildContext context) async {
    await Core.logout();
    await getLoginInfo();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Settings'),
        ),
        body: SingleChildScrollView(
            padding: EdgeInsets.all(0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Container(
                  color: Theme.of(context).highlightColor,
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Center(
                      child: Text("Configuration",
                          style: Theme.of(context).textTheme.title),
                    ),
                  ),
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.amber,
                  ),
                  title: Text("Name"),
                  trailing: Text(configInfo.fileName),
                ),
                ListTile(
                    leading: Icon(
                      Icons.update,
                      color: Colors.teal,
                    ),
                    title: Text("Update"),
                    trailing: new OutlineButton(
                        color: Colors.green,
                        shape: new RoundedRectangleBorder(
                            borderRadius: new BorderRadius.circular(10.0)),
                        textColor: Colors.green,
                        onPressed: () {
                          _navigateLogin(context);
                        },
                        child: Text("Check"))),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.brown,
                  ),
                  title: Text("Change Configuration"),
//                  subtitle: Text("Sign in required"),
                  trailing: Icon(Icons.arrow_right),
                  onTap: () {
                    _navigateConfigList(context);
                  },
                ),
                Container(
                  color: Theme.of(context).highlightColor,
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Center(
                      child: Text("Storage",
                          style: Theme.of(context).textTheme.title),
                    ),
                  ),
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.brown,
                  ),
                  title: Text("Size"),
//                  subtitle: Text("Sign in required"),
                  trailing: Text(SpaceInfo.toSizeString(spaceInfo.size)),
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.brown,
                  ),
                  title: Text("Storage Settings"),
//                  subtitle: Text("Sign in required"),
                  trailing: Icon(Icons.arrow_right),
                  onTap: () {
                    _navigateStoragePage(context);
                  },
                ),
                Container(
                  color: Theme.of(context).highlightColor,
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Center(
                      child: Text("Login Info",
                          style: Theme.of(context).textTheme.title),
                    ),
                  ),
                ),
                ListTile(
                  leading: Icon(
                    Icons.lock,
                    color: Colors.lightBlueAccent,
                  ),
                  title: Text("Login Status"),
                  subtitle: loginInfo.isLoggedIn == true
                      ? Text("Logged in as " + loginInfo.userId)
                      : Text("Not logged in yet"),
                  trailing: !loginInfo.isLoggedIn
                      ? new OutlineButton(
                          color: Colors.green,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.green,
                          onPressed: () {
                            _navigateLogin(context);
                          },
                          child: Text("Sign in"))
                      : new OutlineButton(
                          color: Colors.red,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.red,
                          onPressed: () {
                            _navigateLogout(context);
                            setState(() {});
                          },
                          child: Text("Sign out"),
                        ),
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.globe,
                    color: Colors.green,
                  ),
                  title: Text("Server Address"),
                  trailing: Text(loginInfo.serverAddress),
                ),
                Container(
                  color: Theme.of(context).highlightColor,
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Center(
                      child: Text("Upload",
                          style: Theme.of(context).textTheme.title),
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
                  ],
                ),
              ],
            )));
  }
}
