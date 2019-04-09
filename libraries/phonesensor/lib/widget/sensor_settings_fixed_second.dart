import 'package:flutter/material.dart';
import 'package:phonesensor/data/configuration.dart';
import 'package:phonesensor/data/sensor_info.dart';

class SensorSettingsFixedSecond extends StatefulWidget {

  _SensorSettingsFixedSecondState _sensorSettingsPageState;
  SensorSettingsFixedSecond(id, sensorInfo,configuration){
    _sensorSettingsPageState = new _SensorSettingsFixedSecondState(id, sensorInfo, configuration);
  }

  @override
  _SensorSettingsFixedSecondState createState() =>
      _sensorSettingsPageState;
}

class _SensorSettingsFixedSecondState extends State<SensorSettingsFixedSecond> {
  Configuration configuration;
  String id;
  SensorInfo sensorInfo;

  _SensorSettingsFixedSecondState(this.id, this.sensorInfo, this.configuration);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("Phone Sensor")),
      body: bodyData(context),
    );
  }

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
                padding: const EdgeInsets.all(4.0),
                child: Center(
                  child: Text(sensorInfo.getTitle(id),
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
            new Container(
              width: 70,
              height: 70,
//              color: Theme.of(context).highlightColor,
              decoration: new BoxDecoration(
                  color: Theme.of(context).highlightColor,
//                shape: BoxShape.circle,
                  image: new DecorationImage(
                      fit: BoxFit.fitHeight,
                      image: new AssetImage('images/' + id + '.png',
                          package: 'phonesensor'))),
            ),
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(4.0),
                child: Center(
                  child: Text(sensorInfo.getDescription(id),
                      style: Theme.of(context).textTheme.body1),
                ),
              ),
            ),
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(4.0),
                child: Center(
                  child: Text('Settings',
                      style: Theme.of(context).textTheme.title),
                ),
              ),
            ),
            Column(
              children: <Widget>[
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
                      setToDefault(id);
                    },
                    child: new Text("Default", style: TextStyle(fontSize: 14)),
                  ),
                ),
//                Divider(height: 1, color: Colors.grey,),
                ListTile(
                  leading: Text(
                    "Enable",
                    style: TextStyle(fontSize: 16),
                  ),
                  trailing: Switch(
                    value: this.configuration.getEnabled(id),
                    onChanged: (bool newValue) {
                      setState(() {
                        this.configuration.setEnabled(id, newValue);
                      });
                    },
                  ),
                ),
//                Divider(height: 1, color: Colors.grey,),
                ListTile(
                  leading: Text(
                    "Sampling Rate",
                    style: TextStyle(fontSize: 16),
                  ),
                  title: TextField(
                    keyboardType: TextInputType.number,
                    textAlign: TextAlign.center,style: TextStyle(fontSize: 16),
                    controller: TextEditingController(text:this.configuration.getSampleRate(id).toString()),
                    onChanged: (String newValue){
                      setState(() {
                        this.configuration.setSampleRate(id, double.parse(newValue));
                      });
                    },
                  ),
                  trailing: Text("/SECONDS")
                ),
//                Divider(height: 1, color: Colors.grey,),
                ListTile(
                  leading: Text(
                    "Write Type",
                    style: TextStyle(fontSize: 16),
                  ),
                  trailing: new DropdownButton<String>(
                    items: <String>['FIXED', 'AS_RECEIVED', 'ON_CHANGE']
                        .map((String value) {
                      return new DropdownMenuItem<String>(
                        value: value,
                        child: new Text(value),
                      );
                    }).toList(),
                    value: this.configuration.getWriteType(id),
                    onChanged: (String value) {
                      setState(() =>
                          this.configuration.setWriteType(id, value));
                    },
                  ),
                ),
//                Divider(height: 1, color: Colors.grey,),
//                this.configuration.getWriteType(id)=='FIXED'?
//                Divider(height: 1, color: Colors.grey,): SizedBox(),
                this.configuration.getWriteType(id)=='ON_CHANGE'?
                ListTile(
                  leading: Text("WriteOnChange Type",
                      style: TextStyle(fontSize: 16)),
                  trailing: new DropdownButton<String>(
                    items: <String>['NOT_EQUAL', 'SAMPLE_DIFFERENCE']
                        .map((String value) {
                      return new DropdownMenuItem<String>(
                        value: value,
                        child: new Text(value),
                      );
                    }).toList(),
                    value: this.configuration.getWriteOnChangeType(id),
                    onChanged: (String value) {
                      setState(() =>
                          this.configuration.setWriteOnChangeType(id, value));
                    },
                  ),
                ):SizedBox(),
//                this.configuration.getWriteType(id)=='ON_CHANGE'?
//                Divider(height: 1, color: Colors.grey,): SizedBox(),
                this.configuration.getWriteType(id)=='ON_CHANGE' && this.configuration.getWriteOnChangeType(id)=="SAMPLE_DIFFERENCE"?
                ListTile(
                  leading: Text(
                    "WriteOnChange Value: ",
                    style: TextStyle(fontSize: 16),
                  ),
                  title: TextField(
                    keyboardType: TextInputType.number,
                    textAlign: TextAlign.center,style: TextStyle(fontSize: 16),
                    controller: TextEditingController(text:this.configuration.getWriteOnChangeValue(id).toString()),
                    onChanged: (String newValue){
                      this.configuration.setWriteOnChangeValue(id, double.parse(newValue));
                      setState(() {
                      });
                    },
                  ),
                ):SizedBox(),
//                this.configuration.getWriteType(id)=='ON_CHANGE' && this.configuration.getWriteOnChangeType(id)=="SAMPLE_DIFFERENCE"?
//                Divider(height: 1, color: Colors.grey,): SizedBox(),
              ],
            ),
          ],
        ),
      ),
    );
  }
  void setToDefault(String id) async {
    await configuration.setToDefault(id);
    await configuration.getSettings();
    setState(() {});
  }

  Widget createTile(String id, String name, String icon, Color color) {
    return ListTile(
      leading: new Container(
        decoration: new BoxDecoration(
            color: Theme.of(context).backgroundColor.withOpacity(0.0)),
        margin: const EdgeInsets.only(top: 4.0, bottom: 4.0, right: 20.0),
        child: new CircleAvatar(
          backgroundImage: AssetImage(icon, package: 'phonesensor'),
          backgroundColor: color.withOpacity(0.6),
        ),
      ),
      title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Text(name),
            Switch(
              value: true,
              onChanged: (bool newValue) {
                setState(() {
                  print("abc");
                });
              },
            ),
          ]),
      trailing: Icon(Icons.more_horiz),
    );
  }
}
