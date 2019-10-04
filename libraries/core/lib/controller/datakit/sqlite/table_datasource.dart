import 'dart:convert';

import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:sqflite/sqlite_api.dart';

class TableDataSource {
  static const String _T_Name = "datasource";
  static const String _C_ID = "_id";
  static const String _C_UUID = "uuid";
  static const String _C_DataSourceType = "datasource_type";
  static const String _C_DataSourceId = "datasource_id";
  static const String _C_PlatformType = "platform_type";
  static const String _C_PlatformId = "platform_id";
  static const String _C_PlatformAppType = "platformapp_type";
  static const String _C_PlatformAppId = "platformapp_id";
  static const String _C_ApplicationType = "application_type";
  static const String _C_ApplicationId = "application_id";
  static const String _C_CreateTimestamp = "create_timestamp";
  static const String _C_DataSource = "datasource";

  Future<void> createTable(Database database) async {
    String statement = "CREATE TABLE IF NOT EXISTS " +
        _T_Name +
        " (" +
        _C_ID +
        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        _C_UUID +
        " TEXT, " +
        _C_DataSourceType +
        " TEXT, " +
        _C_DataSourceId +
        " TEXT, " +
        _C_PlatformType +
        " TEXT, " +
        _C_PlatformId +
        " TEXT, " +
        _C_PlatformAppType +
        " TEXT, " +
        _C_PlatformAppId +
        " TEXT, " +
        _C_ApplicationType +
        " TEXT, " +
        _C_ApplicationId +
        " TEXT, " +
        _C_CreateTimestamp +
        " INTEGER, " +
        _C_DataSource +
        " TEXT)";
    await database.execute(statement);
  }

  Future<MCDataSourceResult> insertDataSource(
      Database db, MCDataSource dataSource) async {
    int createTimestamp = new DateTime.now().millisecondsSinceEpoch;
    Map<String, dynamic> values = _createHashForSelection(dataSource);
    values[_C_UUID] = dataSource.getUUID();
    values[_C_CreateTimestamp] = createTimestamp;
    values[_C_DataSource] = json.encode(dataSource);
    int val = await db.insert(_T_Name, values);
    print("val = " + val.toString());
    return MCDataSourceResult.create(
        uuid: dataSource.getUUID(),
        createTimestamp: createTimestamp,
        dataSource: dataSource);
  }

  Future<MCDataSourceResult> _queryExactDataSource(
      Database db, MCDataSource dataSource) async {
    MCDataSourceResult result;
    List<Map> r = new List();
    try {
      r = await db.rawQuery("SELECT * FROM " +
          _T_Name +
          " WHERE " +
          _C_UUID +
          " = '" +
          dataSource.getUUID() +
          "' AND " +
          _C_ID +
          " IN (SELECT MAX(" +
          _C_ID +
          ") FROM " +
          _T_Name +
          " GROUP BY " +
          _C_UUID +
          ");");
    } catch (e) {
      print("e=" + e);
    }
    if (r.length > 0) {
      Map e = r[0];
      String str = e[_C_DataSource];
      MCDataSource m = MCDataSource.fromJson(json.decode(str));
      result = MCDataSourceResult.create(
          uuid: e[_C_UUID],
          createTimestamp: e[_C_CreateTimestamp],
          dataSource: m);
    }
    return result;
  }

  Future<List<MCDataSourceResult>> queryMetaData(
      Database db, MCDataSourceResult dataSourceResult) async {
    List<MCDataSourceResult> result = new List();
    List<Map> r = await db.rawQuery("SELECT * FROM " +
        _T_Name +
        " WHERE " +
        _C_UUID +
        " = " +
        dataSourceResult.uuid +
        ";");
    r.forEach((e) {
      String str = e[_C_DataSource];
      MCDataSource m = json.decode(str);
      MCDataSourceResult mcDataSourceResult = MCDataSourceResult.create(
          uuid: e[_C_UUID],
          createTimestamp: e[_C_CreateTimestamp],
          dataSource: m);
      result.add(mcDataSourceResult);
    });
    return result;
  }

  Future<List<MCDataSourceResult>> queryDataSource(
      Database db, MCDataSource dataSource,
      {matchUUID = false}) async {
    List<MCDataSourceResult> result = new List();
    if (matchUUID == true) {
      MCDataSourceResult res = await _queryExactDataSource(db, dataSource);
      if (res != null) result.add(res);
    } else {
      result = await _queryDataSource(db, dataSource);
    }
    return result;
  }

  Future<List<MCDataSourceResult>> _queryDataSource(
      Database db, MCDataSource dataSource) async {
    List<MCDataSourceResult> result = new List();
    Map<String, dynamic> h = _createHashForSelection(dataSource);
    String where = "";
    h.forEach((key, value) {
      if (where.length != 0) where += " AND ";
      where += key + " = " + value;
    });
    if (where.length != 0) where += " AND ";

    String statement = "SELECT * FROM " +
        _T_Name +
        " WHERE " +
        where +
        " " +
        _C_ID +
        " IN (SELECT MAX(" +
        _C_ID +
        ") FROM " +
        _T_Name +
        " GROUP BY " +
        _C_UUID +
        ");";

    List<Map> r = await db.rawQuery(statement);

    r.forEach((e) {
      String str = e[_C_DataSource];
      MCDataSource m = MCDataSource.fromJson(json.decode(str));
      MCDataSourceResult mcDataSourceResult = MCDataSourceResult.create(
          uuid: e[_C_UUID],
          createTimestamp: e[_C_CreateTimestamp],
          dataSource: m);
      result.add(mcDataSourceResult);
    });
    return result;
  }

  Map<String, dynamic> _createHashForSelection(MCDataSource dataSource) {
    Map<String, dynamic> h = new Map();
    h[_C_DataSourceType] = dataSource.dataSourceType;
    h[_C_DataSourceId] = dataSource.dataSourceId;
    h[_C_PlatformType] = dataSource.platformType;
    h[_C_PlatformId] = dataSource.platformId;
    h[_C_PlatformAppType] = dataSource.platformAppType;
    h[_C_PlatformAppId] = dataSource.platformAppId;
    h[_C_ApplicationType] = dataSource.applicationType;
    h[_C_ApplicationId] = dataSource.applicationId;
    h.removeWhere((key, value) => value == null);
    return h;
  }
}
