import 'dart:async';
import 'dart:io';

import 'package:connectivity/connectivity.dart';
import 'package:core/controller/utils/file_utils.dart';
import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/datakitapi/mc_loglevel.dart';
import 'package:mcerebrumapi/datakitapi/mc_summary_level.dart';
import 'package:mcerebrumapi/mc_library.dart';

import 'configuration/config_manager.dart';
import 'datakit/datakit_manager.dart';
import 'uploader/upload_manager.dart';

class CoreController implements ICore {
  ConfigManager _configManager;
  DataKitManager _dataKitManager;
  UploadManager _uploadManager;
  Timer _archiveTimer;
  int startTimestamp = -1;
  static final CoreController _instance = new CoreController._internal();

  factory CoreController() {
    return _instance;
  }

  CoreController._internal() {
    _configManager = new ConfigManager();
    _dataKitManager = new DataKitManager();
    _uploadManager = new UploadManager();
  }

  Future<void> init() async {
    print("corecontroller init...");
    String parentDirectory = await FileUtils.externalDirectoryPath;
    await _configManager.init(
        directory: parentDirectory + "/config",
        configFilename: "config.json",
        defaultConfigFilename: "default_config.json");
    await _dataKitManager.init(
        databaseDirectory: parentDirectory + "/database",
        archiveDirectory: parentDirectory + "/archive");
    await _uploadManager.init(url: "https://odin.md2k.org");
  }

  String userId;

  Future<void> start() async {
    print("corecontroller start...");
    startTimestamp = DateTime.now().millisecondsSinceEpoch;
    await _dataKitManager.start();
    int uploadTime = _configManager.getUploadTime();
    userId = _configManager.getUserId();

//      bool res = await AndroidAlarmManager.cancel(_ARCHIVE_ID);
//      bool res = await AndroidAlarmManager.periodic(const Duration(seconds: 10), _UPLOAD_ID,uploadData, exact: true, wakeup: true);
//      print(" alarmmanager periodic= "+res.toString());
    _archiveTimer =
        Timer.periodic(Duration(milliseconds: uploadTime), (var time) async {
      await _dataKitManager.archiveData();
      var connectivityResult = await (Connectivity().checkConnectivity());
      if (connectivityResult == ConnectivityResult.wifi) {
        await uploadData();
      }
    });
  }

  Future<void> stop() async {
    print("corecontroller stop...");
    startTimestamp = -1;
    _archiveTimer.cancel();
    //  await AndroidAlarmManager.cancel(_UPLOAD_ID);
    await _dataKitManager.stop();
  }

  //Configuration
  Future<void> replaceConfig(
          Map<String, dynamic> config, Map<String, dynamic> defaultConfig) =>
      _configManager.replaceDefaultConfig(defaultConfig);

  @override
  Future<Map<String, dynamic>> getConfig(String id) =>
      _configManager.getById(id);

  @override
  Future<Map<String, dynamic>> getDefaultConfig(String id) =>
      _configManager.getDefaultById(id);

  @override
  Future<void> setConfig(String id, Map<String, dynamic> c) =>
      _configManager.setById(id, c);

  // DataSource
  @override
  Future<MCDataSourceResult> registerDataSource(MCDataSource dataSource) =>
      _dataKitManager.insertDataSource(dataSource);

  @override
  Future<List<MCDataSourceResult>> queryDataSource(MCDataSource dataSource,
          {bool matchUUID = false}) =>
      _dataKitManager.queryDataSource(dataSource, matchUUID: matchUUID);

  @override
  Future<List<MCDataSourceResult>> queryMetaData(
          MCDataSourceResult dataSourceResult) =>
      _dataKitManager.queryMetaData(dataSourceResult);

  @override
  void subscribeDataSource(MCDataSource dataSource, callback) =>
      _dataKitManager.subscribeDataSource(dataSource, callback);

  @override
  void unsubscribeDataSource(callback) =>
      _dataKitManager.unsubscribeDataSource(callback);

  //Data
  @override
  Future<int> countData(MCDataSourceResult dataSourceResult,
          {int startTimestamp, int endTimestamp}) =>
      _dataKitManager.countData(dataSourceResult,
          startTimestamp: startTimestamp, endTimestamp: endTimestamp);

  @override
  Future<void> insertData(List<MCData> data) =>
      _dataKitManager.insertData(data);

  @override
  Future<List<MCData>> queryData(MCDataSourceResult dataSourceResult,
          {int startTimestamp,
          int endTimestamp,
          int limit,
          bool recent = true}) =>
      _dataKitManager.queryData(dataSourceResult,
          startTimestamp: startTimestamp,
          endTimestamp: endTimestamp,
          limit: limit,
          recent: recent);

  @override
  void subscribeData(MCDataSourceResult dataSourceResult, callback) =>
      _dataKitManager.subscribeData(dataSourceResult, callback);

  @override
  void unsubscribeData(callback) => _dataKitManager.unsubscribeData(callback);

  //Uploader
  Future<void> uploadData() async {
    print("upload_data");

    String parentDirectory = await FileUtils.externalDirectoryPath;
    Directory directory = Directory(parentDirectory + "/archive");
    List<FileSystemEntity> files = directory.listSync();
    for (int i = 0; i < files.length; i++) {
      if (files[i].path.endsWith(".json")) {
        String prefix = files[i].path.substring(0, files[i].path.length - 5);
        bool res = await _uploadManager.upload(userId, userId, prefix);
        if (res == true) {
          File(prefix + ".json").deleteSync();
          File(prefix + ".gzip").deleteSync();
        }
      }
    }
  }

  @override
  Future<void> insertLog(String id, MCLogLevel logLevel, String className,
      String methodName, String message) {
    // TODO: implement insertLog
    return null;
  }

  @override
  Future<void> insertUserActivity(
      String id, String className, String event, String message) {
    // TODO: implement insertUserActivity
    return null;
  }

  @override
  Future<void> insertDataAsSummary(MCSummaryLevel summaryLevel, MCData data) {
    // TODO: implement updateDataAsSummary
    return null;
  }
}
