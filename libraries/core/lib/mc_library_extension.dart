import 'package:mcerebrumapi/mc_library.dart';
import 'package:permission_handler/permission_handler.dart';

import 'controller/controller.dart';

class CoreLibrary extends MCLibrary {
  CoreLibrary(ICore iCore)
      : super(iCore, "core",
            title: "Core",
            description: "Manages configuration and data for plugins");

  @override
  IExec createExec(IData iData) {
    return IExec(
      init: () async {
        await CoreController().init();
      },
      backgroundProcess: MCBackgroundProcess(start: ({param}) async {
        await CoreController().start();
      }, stop: ({param}) async {
        CoreController().stop();
      }, isRunning: () async {
        return CoreController().startTimestamp != -1;
      }, getRunningTime: () async {
        return (CoreController().startTimestamp != -1
            ? DateTime.now().millisecondsSinceEpoch -
                CoreController().startTimestamp
            : -1);
      }),
      permission: MCPermission(hasPermission: () async {
        PermissionStatus permission = await PermissionHandler()
            .checkPermissionStatus(PermissionGroup.storage);
        return permission == PermissionStatus.granted;
      }, getPermission: () async {
        Map<PermissionGroup, PermissionStatus> permissions =
            await PermissionHandler()
                .requestPermissions([PermissionGroup.storage]);
        return permissions[PermissionGroup.storage] == PermissionStatus.granted;
      }),
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
