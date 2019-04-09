import 'dart:async';

import 'package:charts_flutter/flutter.dart' as charts;
import 'package:core/core.dart';
import 'package:core/data/space_info.dart';
import 'package:core/ui/page/datasource_info_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class StoragePage extends StatefulWidget {
  @override
  _StoragePageState createState() => _StoragePageState();
}

class _StoragePageState extends State<StoragePage> {
  SpaceInfo _spaceInfo;
  bool isStarted = false;
  List<charts.Series> seriesList;
  bool animate = true;

  @override
  void initState() {
    super.initState();
    _spaceInfo = new SpaceInfo();
    getSpaceInfo();
    isRunning();
  }

  Future<void> getSpaceInfo() async {
    _spaceInfo = await Core.getSpaceInfo();
    seriesList = _createSampleData();
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {});
  }
  List<charts.Series<LinearSales, int>> _createSampleData() {
    final data = [
      new LinearSales("Free", 0, _spaceInfo == null ? 0 : _spaceInfo.available,
          charts.ColorUtil.fromDartColor(Colors.grey)),
      new LinearSales("mCerebrum", 1, _spaceInfo == null ? 0 : _spaceInfo.size,
          charts.ColorUtil.fromDartColor(Colors.green)),
      new LinearSales("Other", 2, _spaceInfo == null ? 0 : _spaceInfo.other,
          charts.ColorUtil.fromDartColor(Colors.blue)),
    ];

    return [
      new charts.Series<LinearSales, int>(
        id: 'storage',
        domainFn: (LinearSales sales, _) => sales.year,
        measureFn: (LinearSales sales, _) => sales.sales,
        colorFn: (LinearSales sales, _) => sales.color,

        data: data,
        overlaySeries: true,
        // Set a label accessor to control the text of the arc label.
        labelAccessorFn: (LinearSales row, _) =>
        '${row.name}: ${SpaceInfo.toSizeString(row.sales)}',
        insideLabelStyleAccessorFn: (LinearSales sales, _) =>
            charts.TextStyleSpec(color: charts.Color.white),
        outsideLabelStyleAccessorFn: (LinearSales sales, _) =>
            charts.TextStyleSpec(color: sales.color),
      )
    ];
  }
  Future<void> setStart() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      await Core.setStart;
      await isRunning();
      await getSpaceInfo();
    } on PlatformException {}
  }

  Future<void> setStop() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      await Core.setStop;
    } on PlatformException {}
    await isRunning();
  }

  Future<void> delete() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      await Core.delete;
      await isRunning();
      await getSpaceInfo();
    } on PlatformException {}
  }

  Future<void> isRunning() async {
    bool res = false;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      res = await Core.isStarted;
    } on PlatformException {}
    if (!mounted) return;
    setState(() {
      isStarted = res;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("Storage")),
      body: bodyData(context),
    );
  }

  Widget _createSizeChart(BuildContext context) => Container(
        width: double.infinity,
        child: Padding(
          padding: const EdgeInsets.all(10.0),
//            color: Colors.white.withOpacity(0),
          child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Container(width: double.infinity, height: 120, child: _chart()),
                SizedBox(
                  height: 20,
                ),
                Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: <Widget>[
                      Text("Total: " + SpaceInfo.toSizeString(_spaceInfo.total),
                          style: Theme.of(context).textTheme.body1),
                      Text("mCerebrum: " + SpaceInfo.toSizeString(_spaceInfo.size),
                          style: TextStyle(color: Colors.green)),
                      Text("Free: " + SpaceInfo.toSizeString(_spaceInfo.available),
                          style: TextStyle(color: Colors.grey)),
                    ]),
              ]),
        ),
      );

  Widget bodyData(BuildContext context) {
    return Container(
      height: double.infinity,
      child: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                    child: Text(
                  "Statistics",
                  style: Theme.of(context).textTheme.title,
                )),
              ),
            ),
            _spaceInfo != null && _spaceInfo.total!=0
                ? _createSizeChart(context)
                : Container(
                    child: Text("error"),
                  ),
            //1
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Settings",
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
            Column(
              children: <Widget>[
                ListTile(
                  leading: Icon(
                    Icons.play_circle_outline,
                    color: Colors.green,
                  ),
                  title: Text("Enable"),
                  trailing: Switch(
                    value: isStarted,
                    onChanged: (bool newValue) {
                      setState(() {
                        if (newValue) {
                          setStart();
                        } else
                          setStop();
//                          isStarted = newValue;
                      });
                    },
                  ),
                ),
                ListTile(
                    leading: Icon(
                      FontAwesomeIcons.eye,
                      color: Colors.lightBlueAccent,
                    ),
                    title: Text("Explore Data"),
                    subtitle:
                        _spaceInfo != null && _spaceInfo.size > 0 && isStarted
                            ? null
                            : !isStarted
                                ? Text("Storage is disabled")
                                : Text("Database is empty"),
                    enabled:
                        _spaceInfo != null && _spaceInfo.size > 0 && isStarted
                            ? true
                            : false,
                    trailing: Icon(Icons.arrow_right),
                    onTap: () {
                      Navigator.push(
                          context,
                          new MaterialPageRoute(
                              builder: (_context) => new DataSourceTable()));
                    }),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.compressArrowsAlt,
                    color: Colors.purple,
                  ),
                  title: Text("Prune Rules"),
                  enabled: false,
                  trailing: Icon(Icons.arrow_right),
                ),
                ListTile(
                  leading: Icon(
                    Icons.delete,
                    color: Colors.amber,
                  ),
                  title: Text("Delete Data"),
                  enabled:
                      (_spaceInfo.size > 0) ? true : false,
                  subtitle: (_spaceInfo != null && _spaceInfo.size > 0)
                      ? Text(SpaceInfo.toSizeString(_spaceInfo.size))
                      : Text("Empty"),
//                  trailing: Icon(Icons.arrow_right),
                  onTap: (_spaceInfo.size > 0)
                      ? () {
                          _showDialog(context);
                          setState(() {});
                        }
                      : null,
                ),
                ListTile(
                  leading: Icon(
                    FontAwesomeIcons.fileExport,
                    color: Colors.blueGrey,
                  ),
                  title: Text("Export Data"),
                  enabled: false,
//                  trailing: Icon(Icons.arrow_right),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _chart() {
    return new charts.PieChart(seriesList,
        animate: animate,
        // Configure the width of the pie slices to 60px. The remaining space in
        // the chart will be left as a hole in the center.
        //
        // [ArcLabelDecorator] will automatically position the label inside the
        // arc if the label will fit. If the label will not fit, it will draw
        // outside of the arc with a leader line. Labels can always display
        // inside or outside using [LabelPosition].
        //
        // Text style for inside / outside can be controlled independently by
        // setting [insideLabelStyleSpec] and [outsideLabelStyleSpec].
        //
        // Example configuring different styles for inside/outside:
        //       new charts.ArcLabelDecorator(
        //          insideLabelStyleSpec: new charts.TextStyleSpec(...),
        //          outsideLabelStyleSpec: new charts.TextStyleSpec(...)),
        defaultRenderer: new charts.ArcRendererConfig(
            arcWidth: 10,
            arcRendererDecorators: [
              new charts.ArcLabelDecorator(
                  labelPosition: charts.ArcLabelPosition.outside)
            ]));
  }

  void _showDialog(BuildContext context) {
    // flutter defined function
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          title: new Text("Delete Data?"),
          content: new Text("Are you sure you want to delete all data?"),
          actions: <Widget>[
            // usually buttons at the bottom of the dialog
            new FlatButton(
              child: new Text("CANCEL"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            new FlatButton(
              child: new Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
                delete();
              },
            ),
          ],
        );
      },
    );
  }

/*
  @override
  Widget build(BuildContext context) {
    return new charts.PieChart(seriesList,
        animate: animate,
        // Configure the width of the pie slices to 60px. The remaining space in
        // the chart will be left as a hole in the center.
        //
        // [ArcLabelDecorator] will automatically position the label inside the
        // arc if the label will fit. If the label will not fit, it will draw
        // outside of the arc with a leader line. Labels can always display
        // inside or outside using [LabelPosition].
        //
        // Text style for inside / outside can be controlled independently by
        // setting [insideLabelStyleSpec] and [outsideLabelStyleSpec].
        //
        // Example configuring different styles for inside/outside:
        //       new charts.ArcLabelDecorator(
        //          insideLabelStyleSpec: new charts.TextStyleSpec(...),
        //          outsideLabelStyleSpec: new charts.TextStyleSpec(...)),
        defaultRenderer: new charts.ArcRendererConfig(
            arcWidth: 60,
            arcRendererDecorators: [new charts.ArcLabelDecorator()]));
  }
*/

  /// Create one series with sample hard coded data.
/*
  static List<charts.Series<LinearSales, int>> _createSampleData(
      List<int> list) {
    final data = [
      new LinearSales("Free", 0, list == null ? 0 : list[3],
          charts.ColorUtil.fromDartColor(Colors.grey)),
      new LinearSales("mCerebrum", 1, list == null ? 0 : list[1],
          charts.ColorUtil.fromDartColor(Colors.green)),
      new LinearSales("Other", 2, list == null ? 0 : list[2],
          charts.ColorUtil.fromDartColor(Colors.blue)),
    ];

    return [
      new charts.Series<LinearSales, int>(
        id: 'storage',
        domainFn: (LinearSales sales, _) => sales.year,
        measureFn: (LinearSales sales, _) => sales.sales,
        colorFn: (LinearSales sales, _) => sales.color,

        data: data,
        overlaySeries: true,
        // Set a label accessor to control the text of the arc label.
        labelAccessorFn: (LinearSales row, _) =>
            '${row.name}: ${getSizeStr(row.sales)}',
        insideLabelStyleAccessorFn: (LinearSales sales, _) =>
            charts.TextStyleSpec(color: charts.Color.white),
        outsideLabelStyleAccessorFn: (LinearSales sales, _) =>
            charts.TextStyleSpec(color: sales.color),
      )
    ];
  }
*/
}

/// Sample linear data type.
class LinearSales {
  final int year;
  final int sales;
  final charts.Color color;
  final String name;

  LinearSales(this.name, this.year, this.sales, this.color);
}

