import 'dart:async';
import 'dart:io';

import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:sqflite/sqflite.dart';

import 'table_data.dart';
import 'table_datasource.dart';

class SqliteDatabase {
  Database _db;
  TableDataSource _tableDataSource;
  TableData _tableData;
  List<MCData> bufferData;
  final StreamController ctrl = StreamController();
  String _directory, _filename;

  Future<void> init(String directory, String filename) async {
    _directory = directory;
    _filename = filename;
    await Directory(directory).create(recursive: true);
  }

  Future<void> start() async {
    bufferData = new List();
    _db = await openDatabase(_directory + "/" + _filename);
    _tableDataSource = new TableDataSource();
    _tableData = new TableData();
    _tableDataSource.createTable(_db);
    Timer.periodic(Duration(seconds: 5), (time) async {
      sync();
    });
  }

  Future<void> stop() async {
    ctrl.close();
    if (_db != null) await _db.close();
    _db = null;
  }

  Future<void> delete() async {
    stop();
    File f = new File(_directory + "/" + _filename);
    bool isExist = await f.exists();
    if (isExist) await f.delete();
  }

  Future<int> size() async {
    String filePath = _directory + "/" + _filename;
    File f = new File(filePath);
    bool isExist = await f.exists();
    if (isExist)
      return await f.length();
    else
      return -1;
  }

  //DataSource
  Future<MCDataSourceResult> insertDataSource(MCDataSource dataSource) async {
    List<MCDataSourceResult> dataSourceResults = await _tableDataSource
        .queryDataSource(_db, dataSource, matchUUID: true);
    if (dataSourceResults.length > 0 &&
        dataSourceResults[0].dataSource == dataSource) {
      return dataSourceResults[0];
    } else {
      MCDataSourceResult result =
          await _tableDataSource.insertDataSource(_db, dataSource);
      if (dataSourceResults.length > 0)
        await _tableData.createTable(_db, result, old: dataSourceResults[0]);
      else
        await _tableData.createTable(_db, result);
      return result;
    }
  }

  Future<List<MCDataSourceResult>> queryDataSource(MCDataSource dataSource,
          {matchUUID = false}) =>
      _tableDataSource.queryDataSource(_db, dataSource, matchUUID: matchUUID);

  Future<List<MCDataSourceResult>> queryMetaData(
          MCDataSourceResult dataSourceResult) =>
      _tableDataSource.queryMetaData(_db, dataSourceResult);

  //Data
  Future<void> insertData(List<MCData> data) async {
    bufferData.addAll(data);
//    return _tableData.insertData(_db, data);
  }

  Future<List<MCData>> queryData(MCDataSourceResult dataSourceResult,
          {int startTimestamp,
          int endTimestamp,
          int limit,
          int syncBit,
          bool latest = true}) =>
      _tableData.queryData(_db, dataSourceResult,
          startTimestamp: startTimestamp,
          endTimestamp: endTimestamp,
          limit: limit,
          syncBit: syncBit,
          latest: latest);

  Future<int> countData(MCDataSourceResult dataSourceResult,
          {int startTimestamp, int endTimestamp, int syncBit}) =>
      _tableData.countData(_db, dataSourceResult,
          startTimestamp: startTimestamp,
          endTimestamp: endTimestamp,
          syncBit: syncBit);

  Future<void> setSyncBit(MCDataSourceResult dataSourceResult,
          int startTimestamp, int endTimestamp) =>
      _tableData.setSyncBit(
          _db, dataSourceResult, startTimestamp, endTimestamp);

  Future<void> pruneData(MCDataSourceResult dataSourceResult, int leaveLimit) =>
      _tableData.pruneData(_db, dataSourceResult, leaveLimit);

  Future<void> sync() async {
    print("sync called size = " + bufferData.length.toString());
    if (bufferData.length == 0) return;
    await _tableData.insertData(_db, bufferData);
    bufferData.clear();
  }
}
