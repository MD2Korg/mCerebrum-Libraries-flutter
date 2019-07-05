import 'package:core/core.dart';
import 'package:core/ui/widgets/common_flushbar.dart';
import 'package:flushbar/flushbar.dart';
import 'package:flutter/material.dart';

class ConfigListPage extends StatefulWidget {

  @override
  _ConfigListPageState createState() => _ConfigListPageState();
}

class _ConfigListPageState extends State<ConfigListPage> {
  Flushbar flush;
  List configListServer = new List();
  List configListAsset = new List();

/*
  ConfigInfo configInfo = new ConfigInfo();
*/

  @override
  void initState() {
    super.initState();
    _getConfigListServer(context);
    _getConfigListAsset(context);
    getConfigInfo();
  }

  Future<void> getConfigInfo() async {
/*
    configInfo = await Core.getConfigInfo();
    if (!mounted) return;
*/
    setState(() {});
  }

  List<Widget> createListTiles(List configList) {
    List<ListTile> listTiles = new List();
/*
    for (int i = 0; i < configList.length; i++) {
      ConfigInfo c = configList[i];
      ListTile l = new ListTile(
        title: Text(c.filename),
        subtitle: c.serverPublishedTime > 0
            ? Text(_timeToString(c.serverPublishedTime))
            : null,
        trailing: new OutlineButton(
            color: (c.filename == configInfo.filename &&
                    c.isFromCerebralCortex == configInfo.isFromCerebralCortex)
                ? Colors.red
                : Colors.green,
            shape: new RoundedRectangleBorder(
                borderRadius: new BorderRadius.circular(10.0)),
            textColor: (c.filename == configInfo.filename &&
                    c.isFromCerebralCortex == configInfo.isFromCerebralCortex)
                ? Colors.red
                : Colors.green,
            disabledTextColor: Colors.red,
            onPressed: (c.filename == configInfo.filename &&
                c.isFromCerebralCortex == configInfo.isFromCerebralCortex)
                ?null:() {
              if (flush != null && !flush.isDismissed()) flush.dismiss(true);
              flush = CommonFlushBar().showProgress(context, "downloading...");
              _changeConfig(c.filename);
            },
            child: (c.filename == configInfo.filename &&
                    c.isFromCerebralCortex == configInfo.isFromCerebralCortex)
                ? Text("Selected")
                : Text("Select")),
      );
      listTiles.add(l);
    }
*/
    return listTiles;
  }

  Future<void> _changeConfig(String filename) async {
    bool res = await Core.changeConfig(filename);
    if (flush != null && !flush.isDismissed()) flush.dismiss(true);
    flush = CommonFlushBar().showSuccess(context, "Configuration changed");
    await getConfigInfo();
  }

  static String _twoDigits(int n) {
    if (n >= 10) return "${n}";
    return "0${n}";
  }

  String _timeToString(int time) {
    var date = new DateTime.fromMillisecondsSinceEpoch(time).toLocal();
    String y = "${date.year}";
    String m = _twoDigits(date.month);
    String d = _twoDigits(date.day);
    String h = _twoDigits(date.hour);
    String min = _twoDigits(date.minute);
    String sec = _twoDigits(date.second);
    return "$y-$m-${d} $h:$min:$sec";
  }

  _getConfigListServer(BuildContext context) async {
/*
    configListServer = await Core.getConfigListCerebralCortex();
*/
    setState(() {});
  }

  _getConfigListAsset(BuildContext context) async {
/*
    configListAsset = await Core.getConfigListAsset();
*/
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Configuration Files"),
        ),
        body: SingleChildScrollView(
            child: new Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Current Config File",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
/*
            ListTile(
              leading: Text("Filename"),
              title: Text(configInfo.filename),
              trailing: Text("From: "+configInfo.from),
            ),
*/
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Config files (Local)",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
            Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: createListTiles(configListAsset)),
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Config files (Server)",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
            Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: createListTiles(configListServer)),
          ],
        )));
  }
}
