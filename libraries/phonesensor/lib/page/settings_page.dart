import 'package:flutter/material.dart';
import 'package:mcerebrumapi/mc_library.dart';
import 'package:phonesensor/data/settings.dart';
import 'package:phonesensor/sensor/sensors.dart';

class SettingsPage extends StatefulWidget {
  final IData iData;
  final Sensors sensors;

  SettingsPage(this.iData, this.sensors);

  @override
  _SettingsPageState createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  Settings settings;

  @override
  void initState() {
    super.initState();
    readConfig();
  }

  Future<void> readConfig() async {
    Map<String, dynamic> s = new Map<String, dynamic>();
    Map<String, dynamic> d = new Map<String, dynamic>();
    if(widget.iData!=null) {
      s = await widget.iData.getConfig();
      d = await widget.iData.getDefaultConfig();
    }
    settings = new Settings(s,d);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        onWillPop: () async {
          if(widget.iData!=null)
          await widget.iData.setConfig(settings.settings);
          return true;
        },
        child: new Scaffold(
          appBar: AppBar(elevation: 4.0, title: Text("PhoneSensor Settings")),
          body: settings==null?Container(): bodyData(context),
        ));
  }

  Widget bodyData(BuildContext context) {
    List<Color> colors = [
      Colors.green,
      Colors.blue,
      Colors.orange,
      Colors.deepPurple,
      Colors.pink,
      Colors.brown,
      Colors.orangeAccent,
      Colors.teal,
      Colors.deepPurpleAccent,
      Colors.amber
    ];
    int index = 0;
    return Container(
      height: double.infinity,
      child: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Column(
              children: <Widget>[
                Container(
                  color: Theme.of(context).highlightColor,
                  padding: EdgeInsets.all(10),
                  child: Center(
                    child: Text("Settings",
                        style: Theme.of(context).textTheme.title),
                  ),
                ),
                ListTile(
                  dense: true,
                  leading: Text(
                    "Set to Default",
                    style: TextStyle(fontSize: 14),
                  ),
                  trailing: new OutlineButton(
                    color: Colors.green,
                    shape: new RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(10.0)),
                    textColor: Colors.green,
                    onPressed: () {
//                      setToDefault(id);
                    },
                    child: new Text("Default", style: TextStyle(fontSize: 14)),
                  ),
                ),
                Container(
                  color: Theme.of(context).highlightColor,
                  padding: EdgeInsets.all(10),
                  child: Center(
                    child: Text("Sensor Settings",
                        style: Theme.of(context).textTheme.title),
                  ),
                ),
/*
                createTile("accelerometer", colors[(index++) % colors.length], new SensorSettingsFixedSecond("accelerometer", settings)),
                createTile("gyroscope", colors[(index++)%colors.length], new SensorSettingsFixedSecond("gyroscope", settings)),
                createTile("magnetometer", colors[(index++)%colors.length], new SensorSettingsFixedSecond("magnetometer", settings)),
*/
                createTile("activityType", colors[(index++)%colors.length], null),
                createTile("battery", colors[(index++)%colors.length], null/*new SensorSettingsOnChange("battery", settings)*/),
                createTile("gps", colors[(index++)%colors.length], null/*new SensorSettingsSecMinHour("gps", settings)*/),
/*
                createTile("accelerometer_linear", colors[(index++)%colors.length], new SensorSettingsFixedSecond("accelerometer_linear", settings)),
                createTile("gravity", colors[(index++)%colors.length], new SensorSettingsFixedSecond("gravity", settings)),
                createTile("ambient_light", colors[(index++)%colors.length], new SensorSettingsSecMinHour("ambient_light", settings)),
                createTile("air_pressure", colors[(index++)%colors.length], new SensorSettingsSecMinHour("air_pressure", settings)),
                createTile("ambient_temperature", colors[(index++)%colors.length], new SensorSettingsSecMinHour("ambient_temperature", settings)),
                createTile("proximity", colors[(index++)%colors.length], new SensorSettingsSecMinHour("proximity", settings)),
                createTile("relative_humidity", colors[(index++)%colors.length], new SensorSettingsSecMinHour("relative_humidity", settings)),
 //               createTile("bluetooth_nearby", colors[(index++)%colors.length], new SensorSettingsPage("bluetooth_nearby", sensorInfo, configuration)),
                createTile("bluetooth_status", colors[(index++)%colors.length], new SensorSettingsOnChange("bluetooth_status", settings)),
                createTile("charging_status", colors[(index++)%colors.length], new SensorSettingsOnChange("charging_status", settings)),
                createTile("gps_status", colors[(index++)%colors.length], new SensorSettingsOnChange("gps_status", settings)),
                createTile("significant_motion", colors[(index++)%colors.length], new SensorSettingsOnChange("significant_motion", settings)),
*/
//                createTile("step_count", colors[(index++)%colors.length], new SensorSettingsPage("step_count", sensorInfo, configuration)),
//                createTile("wifi_nearby", colors[(index++)%colors.length], new SensorSettingsPage("wifi_nearby", sensorInfo, configuration)),
//                createTile("wifi_status", colors[(index++)%colors.length], new SensorSettingsPage("wifi_status", sensorInfo, configuration)),

              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget createTile(String id, Color color, Widget moreSettingsWidget) {
    return ListTile(
      leading: new Container(
        decoration: new BoxDecoration(
            color: Theme.of(context).backgroundColor.withOpacity(0.0)),
        margin: const EdgeInsets.only(top: 4.0, bottom: 4.0, right: 20.0),
        child: new CircleAvatar(
          backgroundImage:
              AssetImage('images/' + id + '.png', package: 'phonesensor'),
          backgroundColor: color.withOpacity(0.6),
        ),
      ),
      title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Text(widget.sensors.getSensor(id).getDataSource().dataSourceMetaData.title,
                style: TextStyle(
                  fontSize: widget.sensors.getSensor(id).getDataSource().dataSourceMetaData.title.length > 25 ? 14 : 16,
                )),
            Switch(
              value: settings.isEnabled(id),
              onChanged: (bool newValue) {
                settings.setEnabled(id, newValue);
                setState(() {
                });
              },
            ),
          ]),
      trailing: GestureDetector(
        onTap: () {
          print("onTap called.");
          Navigator.push(context,
              new MaterialPageRoute(builder: (_context) => moreSettingsWidget));
        },
        child: Icon(Icons.more_vert),
      ),
    );
  }
}
