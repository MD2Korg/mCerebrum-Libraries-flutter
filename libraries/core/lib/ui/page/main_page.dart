import 'package:core/core.dart';
import 'package:core/data/config.dart';
import 'package:core/data/space_info.dart';
import 'package:core/ui/page/login_page.dart';
import 'package:core/ui/page/storage_page.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  SpaceInfo spaceInfo = new SpaceInfo();
  Config config;
  Map checkUpdate;

  @override
  void initState() {
    super.initState();
    readConfig();
    readSpaceInfo();
    readCheckUpdate();
  }

  void readConfig() {
    Core.getConfig().then((Config onValue) {
      config = onValue;
      setState(() {});
    }, onError: (e) {});
  }

  void readCheckUpdate() {
    Core.checkUpdate(null).then((Map onValue) {
      checkUpdate = onValue;
      setState(() {});
    }, onError: (e) {});
  }

  void readSpaceInfo() {
    Core.getSpaceInfo().then((SpaceInfo onValue) {
      spaceInfo = onValue;
      setState(() {});
    }, onError: (e) {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Core'),
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
                  title: Text("File name"),
                  trailing: config == null || config.filename == null
                      ? Text("")
                      : Text(config.filename),
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.amber,
                  ),
                  title: Text("From"),
                  trailing: Text(config.from),
                ),
                ListTile(
                    leading: Icon(
                      Icons.update,
                      color: Colors.teal,
                    ),
                    title: Text("Version"),
                    subtitle: Text(config.version),
                    trailing: new OutlineButton(
                        color: Colors.green,
                        shape: new RoundedRectangleBorder(
                            borderRadius: new BorderRadius.circular(10.0)),
                        textColor: Colors.green,
                        onPressed: checkUpdate == null ||
                            checkUpdate["update"] == null ||
                            checkUpdate["update"] == false
                            ? () {
                          Core.checkUpdate(null).then((Map value) {
                            checkUpdate = value;
                            setState(() {

                            });
                          });
                        }
                            : () {
                          Core.updateConfig(null).then((bool onValue) {
                            readConfig();
                          }, onError: (e) {
                            print("error = > " + e.toString());
                          });
                        },
                        child: checkUpdate == null ||
                            checkUpdate["update"] == null ||
                            checkUpdate["update"] == false
                            ? Text("up-to-date")
                            : Text("Update (" +
                            checkUpdate["core_config_version"] +
                            ")"))),
/*
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileAlt,
                    color: Colors.brown,
                  ),
                  title: Text("Change Configuration"),
//                  subtitle: Text("Sign in required"),
                  trailing: Icon(Icons.arrow_right),
                  onTap: () async{
                    await Navigator.push(context, MaterialPageRoute(builder: (context) => ConfigListPage()));
                    readConfigInfo();
                  },
                ),
*/
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
                  trailing: Icon(Icons.arrow_right),
                  onTap: () async {
                    await Navigator.push(context,
                        MaterialPageRoute(builder: (context) => StoragePage()));
                    readSpaceInfo();
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
                  subtitle: config.isLoggedIn == true
                      ? Text("Logged in as " + config.userId)
                      : Text("Not logged in yet"),
                  trailing: !config.isLoggedIn
                      ? new OutlineButton(
                          color: Colors.green,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.green,
                      onPressed: () async {
                        await Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => LoginPage()));
                        readConfig();
                          },
                          child: Text("Sign in"))
                      : new OutlineButton(
                          color: Colors.red,
                          shape: new RoundedRectangleBorder(
                              borderRadius: new BorderRadius.circular(10.0)),
                          textColor: Colors.red,
                    onPressed: () async {
                      await Core.logout();
                      readConfig();
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
                  trailing: Text(config.serverAddress),
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
