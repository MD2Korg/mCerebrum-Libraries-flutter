import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:sqflite/sqflite.dart';
import 'package:sqflite/sqlite_api.dart';

class TableData {
  static const String _C_ID = "id";
  static const String _C_TIMESTAMP = "timestamp";
  static const String _C_SYNC = "cc_sync";

  Future<void> createTable(
      Database database, MCDataSourceResult dataSourceResult,
      {MCDataSourceResult old}) async {
    if (old != null &&
        !_isMetadataEqual(dataSourceResult.dataSource.dataDescriptors,
            old.dataSource.dataDescriptors)) {
      await _deleteTable(database, old);
    }
    await _createTable(database, dataSourceResult);
  }

  Future<void> _deleteTable(
      Database database, MCDataSourceResult dataSourceResult) async {
    String tableName = "'" + dataSourceResult.uuid + "'";
    await database.execute("DROP TABLE IF EXISTS " + tableName);
  }

  bool _isMetadataEqual(List<MCDataDescriptor> a, List<MCDataDescriptor> b) {
    if (a.length != b.length) return false;
    for (int i = 0; i < a.length; i++) {
      if (a[i].id != b[i].id) return false;
      if (a[i].dataType != b[i].dataType) return false;
    }
    return true;
  }

  Future<void> _createTable(
      Database database, MCDataSourceResult dataSourceResult) async {
    String tableName = "'" + dataSourceResult.uuid + "'";
    String statement = "CREATE TABLE IF NOT EXISTS " +
        tableName +
        " (" +
        _C_ID +
        " INTEGER PRIMARY KEY autoincrement, " +
        _C_TIMESTAMP +
        " INTEGER, " +
        _C_SYNC +
        " INTEGER DEFAULT 0 ";

    for (int i = 0;
        i < dataSourceResult.dataSource.dataDescriptors.length;
        i++) {
      MCDataDescriptor d = dataSourceResult.dataSource.dataDescriptors[i];
      statement += ", " + d.id + " " + _getType(d.dataType);
    }
    statement += ")";
    await database.execute(statement);
  }

  String _getType(MCDataType dataType) {
    switch (dataType) {
      case MCDataType.INTEGER:
        return "INTEGER";
      case MCDataType.DOUBLE:
        return "DOUBLE";
      case MCDataType.OBJECT:
        return "TEXT";
      case MCDataType.STRING:
        return "TEXT";
      default:
        return "TEXT";
    }
  }

  Future<void> insertData(Database db, List<MCData> data) async {
    Batch batch = db.batch();
    data.forEach((d) {
      String tableName = "'" + d.uuid + "'";
      Map<String, dynamic> ins = Map.from(d.data);
      ins[_C_TIMESTAMP] = d.timestamp;
      batch.insert(tableName, ins);
    });
    List v = await batch.commit();
  }

  Future<List<MCData>> queryData(
      Database db, MCDataSourceResult dataSourceResult,
      {int startTimestamp,
      int endTimestamp,
      int limit,
      int syncBit,
      bool latest}) async {
    String whereStatement = "";
    String tableName = "'" + dataSourceResult.uuid + "'";
    if (startTimestamp != null)
      whereStatement += _C_TIMESTAMP + " >= " + startTimestamp.toString();
    if (endTimestamp != null) {
      if (whereStatement.length != 0) whereStatement += " AND ";
      whereStatement += _C_TIMESTAMP + " <= " + endTimestamp.toString();
    }
    if (syncBit != null) {
      if (whereStatement.length != 0) whereStatement += " AND ";
      whereStatement += _C_SYNC + " = " + syncBit.toString();
    }
    if (whereStatement.length != 0) whereStatement = " WHERE " + whereStatement;
    String limitStatement = "";
    if (limit != null) {
      limitStatement = " LIMIT " + limit.toString();
    }
    String orderStatement =
        " ORDER BY " + _C_TIMESTAMP + (latest ? " DESC" : " ASC");
    List<Map> queryRes = await db.rawQuery("SELECT * FROM " +
        tableName +
        whereStatement +
        orderStatement +
        limitStatement);
    int length = queryRes.length;
    List<MCData> result = new List(length);
    int index = 0;

    queryRes.forEach((e) {
      List data = new List();
      int timestamp = e[_C_TIMESTAMP];
      for (int i = 0;
          i < dataSourceResult.dataSource.dataDescriptors.length;
          i++) {
        String name = dataSourceResult.dataSource.dataDescriptors[i].id;
        data.add(e[name]);
      }
      result[latest ? length - index - 1 : index] =
          MCData.create(dataSourceResult, timestamp, data);
      index++;
    });
    return result;
  }

  Future<void> setSyncBit(Database db, MCDataSourceResult dataSourceResult,
      int startTimestamp, int endTimestamp) async {
    String tableName = "'" + dataSourceResult.uuid + "'";
    Map<String, dynamic> row = new Map<String, dynamic>();
    row[_C_SYNC] = 1;
    await db.update(tableName, row,
        where: _C_TIMESTAMP +
            " >= ? AND " +
            _C_TIMESTAMP +
            " <=? AND " +
            _C_SYNC +
            " = ? ",
        whereArgs: [startTimestamp, endTimestamp, 0]);
  }

  Future<int> countData(Database db, MCDataSourceResult dataSourceResult,
      {int startTimestamp, int endTimestamp, int syncBit}) async {
    String tableName = "'" + dataSourceResult.uuid + "'";
    String whereStatement = "";
    if (startTimestamp != null)
      whereStatement += _C_TIMESTAMP + " >= " + startTimestamp.toString();
    if (endTimestamp != null) {
      if (whereStatement.length != 0) whereStatement += " AND ";
      whereStatement += _C_TIMESTAMP + " <= " + endTimestamp.toString();
    }
    if (syncBit != null) {
      if (whereStatement.length != 0) whereStatement += " AND ";
      whereStatement += _C_SYNC + " = " + syncBit.toString();
    }
    if (whereStatement.length != 0) whereStatement = " WHERE " + whereStatement;

    return Sqflite.firstIntValue(await db
        .rawQuery('SELECT COUNT(*) FROM ' + tableName + " " + whereStatement));
  }

  Future<void> pruneData(
      Database db, MCDataSourceResult dataSourceResult, int leaveLimit) async {
    String tableName = "'" + dataSourceResult.uuid + "'";
    int countAll = await countData(db, dataSourceResult);
    if (countAll <= leaveLimit) return;
    int deleteCount = countAll - leaveLimit;
    String deleteStatement;
    deleteStatement = "delete from " +
        tableName +
        " where " +
        _C_TIMESTAMP +
        " in (select " +
        _C_TIMESTAMP +
        " from " +
        tableName +
        " order by " +
        _C_TIMESTAMP +
        " LIMIT " +
        deleteCount.toString() +
        ")";
    await db.rawDelete(deleteStatement);
  }
}
