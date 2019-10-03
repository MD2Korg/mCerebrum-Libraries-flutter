import 'package:mcerebrumapi/mc_library.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:phonesensor/sensor/sensors.dart';

class PhoneSensorLibrary extends MCLibrary {
  PhoneSensorLibrary(ICore iCore)
      : super(iCore, "phonesensor",
            title: "PhoneSensor", description: "Collect phone sensor data");

  @override
  IExec createExec(IData iData) {
    Sensors sensors = new Sensors();
    return IExec(
        permission: MCPermission(hasPermission: () async {
          PermissionStatus permission = await PermissionHandler()
              .checkPermissionStatus(PermissionGroup.locationAlways);
          return permission == PermissionStatus.granted;
        }, getPermission: () async {
          Map<PermissionGroup, PermissionStatus> permissions =
          await PermissionHandler()
              .requestPermissions([PermissionGroup.locationAlways]);
          return permissions[PermissionGroup.locationAlways] ==
              PermissionStatus.granted;
        }),
        init: () async {
          await sensors.init();
        },
/*
        settings: MCSettings(
          ui: (param) {
            return SettingsPage(iData, sensors);
          },
        ),
*/
        backgroundProcess: MCBackgroundProcess(start: ({param}) async {
          await sensors.start(iData);
        }, stop: ({param}) async {
          await sensors.stop();
        }, isRunning: () async {
          return sensors.startTimestamp != -1;
        }, getRunningTime: () async {
          return sensors.startTimestamp == -1
              ? -1
              : new DateTime.now().millisecondsSinceEpoch -
                  sensors.startTimestamp;
        }));
  }
}
