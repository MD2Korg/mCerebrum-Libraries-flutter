import 'dart:async';

import 'package:core/controller/datakit/archive/archive_manager.dart';
import 'package:flutter/material.dart';
import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/mc_library.dart';

import 'router/router_data.dart';
import 'router/router_datasource.dart';
import 'sqlite/sqlite_database.dart';

class DataKitManager {
  SqliteDatabase _db = new SqliteDatabase();
  RouterData _routerData = new RouterData();
  RouterDataSource _routerDataSource = new RouterDataSource();
  ArchiveManager _archiveManager = new ArchiveManager();

  Future<void> init(
      {@required String databaseDirectory,
      String databaseFilename = "database.db",
      @required String archiveDirectory}) async {
    await _db.init(databaseDirectory, databaseFilename);
    await _archiveManager.init(archiveDirectory);
  }

  Future<void> start() async {
    await _db.start();
    await _routerData.start();
    await _routerDataSource.start();
  }

  Future<void> stop() async {
    await _db.stop();
    await _routerDataSource.stop();
    await _routerData.stop();
  }

  Future<int> getDatabaseSize() => _db.size();

  Future<int> deleteDatabase() => _db.delete();

  Future<MCDataSourceResult> insertDataSource(MCDataSource dataSource) async {
    List<MCDataSourceResult> res =
        await queryDataSource(dataSource, matchUUID: true);
    MCDataSourceResult dataSourceResult =
        await _db.insertDataSource(dataSource);
    if (res.length == 0) _routerDataSource.notify(dataSourceResult);
    return dataSourceResult;
  }

  Future<List<MCDataSourceResult>> queryDataSource(MCDataSource dataSource,
          {bool matchUUID = false}) =>
      _db.queryDataSource(dataSource, matchUUID: matchUUID);

  Future<List<MCDataSourceResult>> queryMetaData(
          MCDataSourceResult dataSourceResult) =>
      _db.queryMetaData(dataSourceResult);

  void subscribeDataSource(
          MCDataSource dataSource, DataSourceCallback callback) =>
      _routerDataSource.add(dataSource, callback);

  void unsubscribeDataSource(DataSourceCallback callback) =>
      _routerDataSource.remove(callback);

  void subscribeData(
          MCDataSourceResult dataSourceResult, DataCallback callback) =>
      _routerData.add(dataSourceResult.uuid, callback);

  void unsubscribeData(DataCallback callback) => _routerData.remove(callback);

  Future<void> insertData(List<MCData> data) async {
    await _db.insertData(data);
    _routerData.notify(data);
  }

  Future<List<MCData>> queryData(MCDataSourceResult dataSourceResult,
          {int startTimestamp,
          int endTimestamp,
          int limit,
          int syncBit,
          bool recent = true}) =>
      _db.queryData(dataSourceResult,
          startTimestamp: startTimestamp,
          endTimestamp: endTimestamp,
          limit: limit,
          syncBit: syncBit,
          latest: recent);

  Future<int> countData(MCDataSourceResult dataSourceResult,
          {int startTimestamp, int endTimestamp, int syncBit}) =>
      _db.countData(dataSourceResult,
          startTimestamp: startTimestamp,
          endTimestamp: endTimestamp,
          syncBit: syncBit);

//  Future<void> setSyncBit(MCDataSourceResult dataSourceResult, int startTimestamp, int endTimestamp) => _db.setSyncBit(dataSourceResult, startTimestamp, endTimestamp);
//  Future<void> pruneData(MCDataSourceResult dataSourceResult, int leaveLimit) => _db.pruneData(dataSourceResult, leaveLimit);
  Future<void> archiveData(
      {bool asMsgPack = true,
      bool prune = true,
      int pruneLimit = 50000,
      int archiveLimit = 25000}) async {
    print("archive data...");
    List<MCDataSourceResult> mcDataSourceResult =
        await _db.queryDataSource(MCDataSource.query());
    for (int i = 0; i < mcDataSourceResult.length; i++) {
      while (true) {
        List<MCData> data = await _db.queryData(mcDataSourceResult[i],
            limit: archiveLimit, syncBit: 0, latest: false);
        print("dataSource = " +
            mcDataSourceResult[i].uuid +
            " datalength =" +
            data.length.toString());
        if (data.length != 0) {
          _archiveManager.archive(mcDataSourceResult[i], data);
          await _db.setSyncBit(mcDataSourceResult[i], data[0].timestamp,
              data[data.length - 1].timestamp);
        }
        if (data.length != archiveLimit) break;
      }
      await _db.pruneData(mcDataSourceResult[i], pruneLimit);
    }
  }
}
