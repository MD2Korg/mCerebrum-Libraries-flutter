import 'package:flutter/material.dart';
import 'package:phonesensor/data/configuration.dart';
import 'package:phonesensor/widget/sensor_settings_fixed_second.dart';
import 'package:phonesensor/widget/sensor_settings_on_change.dart';
import 'package:phonesensor/widget/sensor_settings_sec_min_hour.dart';
import '../data/sensor_info.dart';
class SettingsPage extends StatefulWidget {
  @override
  _SettingsPageState createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  Configuration configuration = new Configuration();
  SensorInfo sensorInfo = new SensorInfo();
  bool isLoaded = false;


  @override
  void initState() {
    super.initState();
    getSettings();
  }

  Future<void> getSettings() async {
    await configuration.getSettings();
    await sensorInfo.getSensorInfo();
    isLoaded = true;
    setState(() {
      print("abc");
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("PhoneSensor Settings")),
      body: isLoaded?bodyData(context): SizedBox(),
    );
  }

  Widget bodyData(BuildContext context) {
    List<Color> colors=[Colors.green,Colors.blue,Colors.orange,Colors.deepPurple,Colors.pink, Colors.brown, Colors.orangeAccent, Colors.teal, Colors.deepPurpleAccent, Colors.amber];
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
                    shape: new RoundedRectangleBorder(borderRadius: new BorderRadius.circular(10.0)),
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

                createTile("accelerometer", colors[(index++)%colors.length], new SensorSettingsFixedSecond("accelerometer", sensorInfo, configuration)),
                createTile("gyroscope", colors[(index++)%colors.length], new SensorSettingsFixedSecond("gyroscope", sensorInfo, configuration)),
                createTile("magnetometer", colors[(index++)%colors.length], new SensorSettingsFixedSecond("magnetometer", sensorInfo, configuration)),
//                createTile("activity_type", colors[(index++)%colors.length], new SensorSettingsFixedSecond("activity_type", sensorInfo, configuration)),
                createTile("battery", colors[(index++)%colors.length], new SensorSettingsOnChange("battery", sensorInfo, configuration)),
                createTile("gps", colors[(index++)%colors.length], new SensorSettingsSecMinHour("gps", sensorInfo, configuration)),
                createTile("accelerometer_linear", colors[(index++)%colors.length], new SensorSettingsFixedSecond("accelerometer_linear", sensorInfo, configuration)),
                createTile("gravity", colors[(index++)%colors.length], new SensorSettingsFixedSecond("gravity", sensorInfo, configuration)),
                createTile("ambient_light", colors[(index++)%colors.length], new SensorSettingsSecMinHour("ambient_light", sensorInfo, configuration)),
                createTile("air_pressure", colors[(index++)%colors.length], new SensorSettingsSecMinHour("air_pressure", sensorInfo, configuration)),
                createTile("ambient_temperature", colors[(index++)%colors.length], new SensorSettingsSecMinHour("ambient_temperature", sensorInfo, configuration)),
                createTile("proximity", colors[(index++)%colors.length], new SensorSettingsSecMinHour("proximity", sensorInfo, configuration)),
                createTile("relative_humidity", colors[(index++)%colors.length], new SensorSettingsSecMinHour("relative_humidity", sensorInfo, configuration)),
 //               createTile("bluetooth_nearby", colors[(index++)%colors.length], new SensorSettingsPage("bluetooth_nearby", sensorInfo, configuration)),
                createTile("bluetooth_status", colors[(index++)%colors.length], new SensorSettingsOnChange("bluetooth_status", sensorInfo, configuration)),
                createTile("charging_status", colors[(index++)%colors.length], new SensorSettingsOnChange("charging_status", sensorInfo, configuration)),
                createTile("gps_status", colors[(index++)%colors.length], new SensorSettingsOnChange("gps_status", sensorInfo, configuration)),
                createTile("significant_motion", colors[(index++)%colors.length], new SensorSettingsOnChange("significant_motion", sensorInfo, configuration)),
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
          backgroundImage: AssetImage('images/'+id+'.png', package: 'phonesensor'),
          backgroundColor: color.withOpacity(0.6),
        ),
      ),
      title:
      Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Text(sensorInfo.getTitle(id),style: TextStyle(fontSize: sensorInfo.getTitle(id).length>25?14:16,)),
            Switch(
              value: configuration.getEnabled(id),
              onChanged: (bool newValue) {
                setState(() {
                  configuration.setEnabled(id, newValue);
                  print("abc");
                });
              },
            ),
          ]),
      trailing:
      GestureDetector(
        onTap: () {
          print("onTap called.");
          Navigator.push(context,
              new MaterialPageRoute(
                  builder: (_context) => moreSettingsWidget
              )
          );

        },
        child: Icon(Icons.more_horiz),
      ),
    );
  }
}
