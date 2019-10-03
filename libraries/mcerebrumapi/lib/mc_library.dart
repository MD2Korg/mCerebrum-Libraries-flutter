import 'package:flutter/material.dart';
import 'package:mcerebrumapi/datakitapi/mc_summary_level.dart';

import 'datakitapi/mc_data.dart';
import 'datakitapi/mc_datasource.dart';
import 'datakitapi/mc_datasource_result.dart';
import 'datakitapi/mc_loglevel.dart';

abstract class MCLibrary{
  IExec iExec;
  String id();
  String name();
  String description();
  MCLibrary(ICore iCore){
    IData iData = new IData(iCore, id());
    iExec = createExec(iData);
  }
  IExec createExec(IData iData);
}
class IExec{
  final Future<void> Function() init;
  final MCBackgroundProcess backgroundProcess;
  final MCSettings settings;
  final Widget Function({dynamic param}) mainUi;
  final Map<String, Widget Function({dynamic param})> widgets;
  final Map<String, Future<dynamic> Function({dynamic param})> actions;

  final MCPermission permission;
  IExec({this.init, this.backgroundProcess, this.settings, this.widgets, this.permission, this.mainUi, this.actions});
  Future<dynamic> Function({dynamic param}) getAction(String id){
    if(actions!=null)
      return actions[id];
    else return null;
  }
  Widget getUI(String id,{dynamic param}){
    if(widgets == null) return null;
    return widgets[id](param:param);
  }
}
class MCPermission{
  final Future<bool> Function() hasPermission;
  final Future<bool> Function() getPermission;
  MCPermission({@required this.hasPermission, @required this.getPermission});
}


class MCBackgroundProcess{
  final Future<void> Function({dynamic param}) start;
  final Future<void> Function({dynamic param}) stop;
  final Future<bool> Function() isRunning;
  final Future<int> Function() getRunningTime;
  MCBackgroundProcess({@required this.start, @required this.stop, @required this.isRunning, @required this.getRunningTime});
}


class MCSettings{
  final Widget Function(dynamic param) ui;
  final Future<MCSettingsState> Function({Map<String, dynamic> settings, Map<String, dynamic> defaultSettings}) settingsState;// =  async{return SettingsState.NOT_APPLICABLE;}
  MCSettings({this.ui, this.settingsState});
}

class IData{
  final ICore _iCore;
  final String _id;
  IData(this._iCore, this._id);
  Future<MCDataSourceResult> registerDataSource(MCDataSource dataSource)=> _iCore.registerDataSource(dataSource);
  Future<List<MCDataSourceResult>> queryDataSource(MCDataSource dataSource)=>_iCore.queryDataSource(dataSource);
  void subscribeDataSource(MCDataSource dataSource, DataSourceCallback callback)=>_iCore.subscribeDataSource(dataSource, callback);
  void unsubscribeDataSource(DataSourceCallback callback)=>_iCore.unsubscribeDataSource(callback);

  Future<void> insertData(MCDataSourceResult dataSourceResult, List<MCData> data)=>_iCore.insertData(dataSourceResult, data);
  Future<List<MCData>> queryData(MCDataSourceResult dataSourceResult, {int startTimestamp, int endTimestamp, int limit, bool recent=true})=> _iCore.queryData(dataSourceResult,startTimestamp: startTimestamp, endTimestamp:endTimestamp, limit:limit, recent: recent);
  Future<int> countData(MCDataSourceResult dataSourceResult,{int startTimestamp, int endTimestamp})=>_iCore.countData(dataSourceResult, startTimestamp: startTimestamp, endTimestamp: endTimestamp);
  void subscribeData(MCDataSourceResult dataSourceResult, DataCallback callback)=>_iCore.subscribeData(dataSourceResult, callback);
  void unsubscribeData(DataCallback callback)=>_iCore.unsubscribeData(callback);
  //summary
  Future<void> updateSummary(MCDataSourceResult dataSourceResult, MCSummaryLevel summaryLevel, Map<String, double> addValue)=> _iCore.updateSummary(dataSourceResult, summaryLevel, addValue);
  //config
  Future<Map<String, dynamic>> getConfig()=>_iCore.getConfig(_id);
  Future<Map<String, dynamic>> getDefaultConfig()=>_iCore.getDefaultConfig(_id);
  Future<void> setConfig(Map<String, dynamic> c)=>_iCore.setConfig(_id, c);
  //log
  Future<void> insertLog(MCLogLevel logLevel, String className, String methodName, String message, {int timeStamp})=>_iCore.insertLog(_id, logLevel, className, methodName, message);
  //userActivity
  Future<void> insertUserActivity(String className, String event, String message, {int timeStamp})=>_iCore.insertUserActivity(_id, className, event, message);
}

abstract class ICore{
  Future<MCDataSourceResult> registerDataSource(MCDataSource dataSource);
  Future<List<MCDataSourceResult>> queryDataSource(MCDataSource dataSource);
  Future<List<MCDataSourceResult>> queryMetaData(MCDataSourceResult dataSourceResult);
  void subscribeDataSource(MCDataSource dataSource, DataSourceCallback callback);
  void unsubscribeDataSource(DataSourceCallback callback);

  Future<void> insertData(MCDataSourceResult dataSourceResult, List<MCData> data);
  Future<List<MCData>> queryData(MCDataSourceResult dataSourceResult, {int startTimestamp, int endTimestamp, int limit, bool recent=true});
  Future<int> countData(MCDataSourceResult dataSourceResult,{int startTimestamp, int endTimestamp});
  void subscribeData(MCDataSourceResult dataSourceResult, DataCallback callback);
  void unsubscribeData(DataCallback callback);
  Future<Map<String, dynamic>> getConfig(String id);
  Future<Map<String, dynamic>> getDefaultConfig(String id);
  Future<void> setConfig(String id, Map<String, dynamic> c);
  Future<void> updateSummary(MCDataSourceResult dataSourceResult, MCSummaryLevel summaryLevel, Map<String, double> value);

  Future<void> insertLog(String id, MCLogLevel logLevel, String className, String methodName, String message);
  Future<void> insertUserActivity(String id, String className, String event, String message);
}
typedef DataCallback = void Function(List<MCData> data);
typedef DataSourceCallback = void Function(MCDataSourceResult dataSourceResult);

enum MCSettingsState{
  NOT_CONFIGURED,
  PARTIALLY_CONFIGURED,
  CONFIGURED,
  NOT_APPLICABLE
}
