import 'package:mcerebrumapi/mc_library.dart';
import 'package:motionsense/page/settings/settings_page.dart';
import 'package:motionsense/sensor/sensors.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';


class MotionSenseLibrary extends MCLibrary {
  MotionSenseLibrary(ICore iCore) : super(iCore, "motionsense",title: "MotionSense", description: "Collect motionsense sensor data");

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
        settings: MCSettings(
          ui: (param){
            return MySettingsPage(iData);
          }, settingsState: () async{
          Map<String, dynamic> x = await iData.getConfig();
          MotionSenseSettings s =MotionSenseSettings.fromJson(x);
          if(s.motionSenseDevices.length==0) return MCSettingsState.NOT_CONFIGURED;
            else return MCSettingsState.CONFIGURED;
        },
        ),
 backgroundProcess: MCBackgroundProcess(start: ({param}) async {
      await sensors.start(iData);
    },stop: ({param}) async{
    await sensors.stop();
    }, isRunning:() async {return sensors.startTimestamp!=-1;},
    getRunningTime: () async {return sensors.startTimestamp==-1?-1: new DateTime.now().millisecondsSinceEpoch- sensors.startTimestamp;}
    )
/*
        settings: MCSettings(
          ui: (param){
            return SettingsPage(iData);
          },
        )

*/
    );
  }
}
