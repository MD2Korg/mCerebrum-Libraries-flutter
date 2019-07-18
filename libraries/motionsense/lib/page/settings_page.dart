import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:motionsense/data/configuration.dart';

import '../motionsense.dart';

class SettingsPage extends StatefulWidget {
  @override
  _SettingsPageState createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  Configuration configuration = new Configuration();
  StreamSubscription _streamSubscription;
  List<Device> listDevice = new List();
  bool isScanning;

  static const stream = const EventChannel('org.md2k.motionsense.scan');

  Future<void> getSettings() async {
    await configuration.getSettings();
    setState(() {});
  }

  void onDone() {
    print("on done");
  }

  void _addToAvailableDevice(dynamic deviceMap) {
    Map map = json.decode(deviceMap);
    Device d = new Device();
    if (map["deviceName"] != "MotionSenseHRV") return;
    if(configuration.getDevice(map["deviceId"])!=null) return;
    d.deviceName = map["deviceName"];
    d.deviceId = map["deviceId"];
    listDevice.add(d);
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    getSettings();
    _startScan();
  }

  @override
  void dispose() {
    _stopScan();
    super.dispose();
  }

  _startScan() {
    if (_streamSubscription == null) {
      _streamSubscription = stream.receiveBroadcastStream().listen(
          _addToAvailableDevice,
          onError: _addToAvailableDevice,
          onDone: onDone);
    }
    isScanning = true;
    listDevice.clear();

    setState(() {});
  }

  _stopScan() {
    if (_streamSubscription != null) _streamSubscription.cancel();
    _streamSubscription = null;
    isScanning = false;
    setState(() {});
  }

  _buildScanningButton() {
/*
    if (state != BluetoothState.on) {
      return null;
    }
*/
    if (isScanning) {
      return new FloatingActionButton(
        child: new Icon(Icons.stop, color: Colors.white),
        onPressed: _stopScan,
        backgroundColor: Colors.red,
      );
    } else {
      return new FloatingActionButton(
          child: new Icon(Icons.search, color: Colors.white), onPressed: _startScan, backgroundColor: Colors.blue,);
    }
  }

  _buildScanResultTiles() {
/*
    return scanResults.values
        .map((r) => ScanResultTile(
            result: r,
            myCallback: (BluetoothDevice device, String platformId) =>
                _addDevice(r.device, platformId)))
        .toList();
*/
  }

/*
   _addDevice(BluetoothDevice device, String platformId) async{
     deviceConnection?.cancel();
     deviceConnection = null;
    deviceConnection = await _flutterBlue.connect(device).listen((s) {
      if (s == BluetoothDeviceState.connected) {
        device.discoverServices().then((onValue){
          BluetoothCharacteristic ch = new BluetoothCharacteristic(uuid: Guid("da39d600-1d81-48e2-9c68-d0ae4bbd351f"), serviceUuid: Guid("da395d22-1d81-48e2-9c68-d0ae4bbd351f"), descriptors: null, properties: null);
          device.readCharacteristic(ch).then((version) {
            String versionStr = version[0].toString();
            for(int i =1;i<version.length;i++){
              versionStr+="."+version[i].toString();
            }
            deviceConnection?.cancel();
            deviceConnection = null;
            configuration.addDevice("MOTION_SENSE_HRV_PLUS_GEN2", platformId, device.id.toString(), device.name, versionStr).then((onValue){
              scanResults.remove(DeviceIdentifier(device.id.toString()));
              setState(() {});

            });
          });

        });
      } else {
//        print("abc");
      }
    }, onDone: (){
      deviceConnection?.cancel();
      deviceConnection = null;

    });
  }
*/

  _buildAlertTile() {
/*
    return new Container(
      color: Colors.redAccent,
      child: new ListTile(
        title: new Text(
          'Bluetooth adapter is ${state.toString().substring(15)}',
          style: Theme.of(context).primaryTextTheme.subhead,
        ),
        trailing: new Icon(
          Icons.error,
          color: Theme.of(context).primaryTextTheme.subhead.color,
        ),
      ),
    );
*/
  }

  _buildProgressBarTile() {
    return new LinearProgressIndicator();
  }

  Future<void> _deviceSelected(
      String deviceName, String deviceId, String platformId) async {
    await configuration.addDevice(
        "MOTION_SENSE_HRV", platformId, deviceId, deviceName, "1.0.1.0");
    for(int i=0;i<listDevice.length;i++){
      if(listDevice[i].deviceId==deviceId){
        listDevice.removeAt(i);
        break;
      }
    }
    setState(() {});
  }

  List<Widget> listTiles() {
    List<String> ll = new List();
    ll.add("LEFT_WRIST");
    ll.add("RIGHT_WRIST");
    List<Widget> list = new List();
    for (int i = 0; i < listDevice.length; i++) {
      ListTile l = new ListTile(
        title: new Text(listDevice[i].deviceName),
        subtitle: new Text(listDevice[i].deviceId),
        trailing: PopupMenuButton<String>(
          elevation: 3.2,
          onCanceled: () {
            print('You have not chossed anything');
          },
          onSelected: (String value) {
            _deviceSelected(
                listDevice[i].deviceName, listDevice[i].deviceId, value);
          },
          child: new Text("Add",
              style:
                  TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),
          tooltip: 'This is tooltip',
          itemBuilder: (BuildContext context) {
            return ll.map((String choice) {
              return PopupMenuItem<String>(
                value: choice,
                child: Text(choice),
              );
            }).toList();
          },
        ),
/*
        trailing: new FlatButton(
          onPressed: (){
            new DropdownButton<String>(
              items: <String>['Left Wrist', 'Right Wrist'].map((String value) {
                return new DropdownMenuItem<String>(
                  value: value,
                  child: new Text(value),
                );
              }).toList(),
              onChanged: (_) {},
            );
          },
          child: new Text("Add", style: TextStyle(color: Colors.green),),
        ),
*/
      );
      list.add(l);
    }
    return list;
  }

  @override
  Widget build(BuildContext context) {
/*
    if (state != BluetoothState.on) {
      tiles.add(_buildAlertTile());
    }
    tiles.addAll(_buildScanResultTiles());
*/
    return new Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("MotionSense Devices", style: TextStyle(color: Colors.black))),
      floatingActionButton: _buildScanningButton(),
      body: new Column(
        children: <Widget>[
          (isScanning) ? _buildProgressBarTile() : new Container(),
          Container(
            color: Theme.of(context).highlightColor,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Center(
                child: Text("Configured Devices",
                    style: Theme.of(context).textTheme.subtitle),
              ),
            ),
          ),
          configuration.getDeviceNo() == 0
              ? Container(
                  padding: const EdgeInsets.symmetric(vertical: 20.0),
                  child: Text("Not configured yet",
                      style: TextStyle(fontSize: 14)),
                )
              : Column(children: createList()),
          Container(
            color: Theme.of(context).highlightColor,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Center(
                child: Text("Available Devices",
                    style: Theme.of(context).textTheme.subtitle),
              ),
            ),
          ),
          Expanded(
            child: new ListView(
              children: listTiles(),
            ),
          )
        ],
      ),
    );
  }

  List<Widget> createList() {
    List<Widget> list = new List();
    int no = configuration.getDeviceNo();
    for (int i = 0; i < no; i++) {
      Map m = configuration.getDeviceByIndex(i);
      list.add(GestureDetector(
          onTap: () {
            showDialog(
              context: context,
              builder: (BuildContext context) {
                // return object of type Dialog
                return AlertDialog(
                  title: new Text("Delete Device?"),
                  actions: <Widget>[
                    // usually buttons at the bottom of the dialog
                    new FlatButton(
                      child: new Text("Ok"),
                      onPressed: () async{
                        Device d = new Device();
                        d.deviceName = m["deviceName"];
                        d.deviceId = m["deviceId"];
                        await configuration.deleteDevice(m["deviceId"]);
                        listDevice.add(d);
                        Navigator.of(context).pop();
                        setState(() {});
                      },
                    ),
                    new FlatButton(
                      child: new Text("Cancel"),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                  ],
                );
              },
            );

/*
            Navigator.push(
                context,
                new MaterialPageRoute(
                    builder: (_context) =>
                        MotionSenseGen2Page(m["deviceId"], configuration)));
*/
          },
          child: ListTile(
              title: Text(
                m["deviceName"],
                style: TextStyle(fontSize: 14),
              ),
              subtitle: Text(
                m["deviceId"],
                style: TextStyle(fontSize: 12),
              ),
              trailing: Text(
                m["platformId"],
                style: TextStyle(fontSize: 14),
              ))));
    }
    return list;
  }
}

class Device {
  String deviceName;
  String deviceId;
}
