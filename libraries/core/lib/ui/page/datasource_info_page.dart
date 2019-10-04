// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:convert';

import 'package:core/controller/data/datasource_info.dart';
import 'package:core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class DataSourceInfos extends DataTableSource {
  List<DataSourceInfo> _dataSourceInfos=new List();
  DataSourceInfos(String  jsonString){
  List jsonList = jsonDecode(jsonString);
  for(int i =0;i<jsonList.length;i++){
    String uuid = jsonList[i]["uuid"];
    int dataCount = jsonList[i]["dataCount"];
    int dataCountLastHour=jsonList[i]["dataCountLastHour"];
    String dataSource = jsonList[i]["dataSource"];
    int lastDataTime = jsonList[i]["lastDataTime"];
    DataSourceInfo a = new DataSourceInfo(uuid, dataSource, dataCount, dataCountLastHour, lastDataTime);
    _dataSourceInfos.add(a);
  }
  }
/*
  final List<DataSourceInfo> _dataSourceInfos = <DataSourceInfo>[
    DataSourceInfo('PRIVACY-------',     "",                  159, 14,  1),
    DataSourceInfo('Ice cream',          "",         237,  8,  1),
    DataSourceInfo('Eclair',                "",               262,  6,  7),
    DataSourceInfo('Cupcake',                  "",            305,  3,  8),
  ];
*/

  void _sort<T>(Comparable<T> getField(DataSourceInfo d), bool ascending) {
    _dataSourceInfos.sort((DataSourceInfo a, DataSourceInfo b) {
      if (!ascending) {
        final DataSourceInfo c = a;
        a = b;
        b = c;
      }
      final Comparable<T> aValue = getField(a);
      final Comparable<T> bValue = getField(b);
      return Comparable.compare(aValue, bValue);
    });
    notifyListeners();
  }

  int _selectedCount = 0;

  @override
  DataRow getRow(int index) {
    assert(index >= 0);
    if (index >= _dataSourceInfos.length)
      return null;
    final DataSourceInfo dessert = _dataSourceInfos[index];
    return DataRow.byIndex(
        index: index,
        selected: dessert.selected,

        onSelectChanged: (bool value) {
          if (dessert.selected != value) {
            _selectedCount += value ? 1 : -1;
            assert(_selectedCount >= 0);
            dessert.selected = value;
            notifyListeners();
          }
        },

        cells: <DataCell>[
          DataCell(Text('${dessert.uuid}')),
          DataCell(Text('${dessert.dataCount}')),
          DataCell(Text('${dessert.dataCountLastHour}')),
          DataCell(Text('${dessert.lastDataTime}')),
        ]
    );
  }

  @override
  int get rowCount => _dataSourceInfos.length;

  @override
  bool get isRowCountApproximate => false;

  @override
  int get selectedRowCount => _selectedCount;

  void _selectAll(bool checked) {
    for (DataSourceInfo dessert in _dataSourceInfos)
      dessert.selected = checked;
    _selectedCount = checked ? _dataSourceInfos.length : 0;
    notifyListeners();
  }
}

class DataSourceTable extends StatefulWidget {
  static const String routeName = '/material/data-table';

  @override
  _DataSourceTableState createState() => _DataSourceTableState();
}

class _DataSourceTableState extends State<DataSourceTable> {
  int _rowsPerPage = PaginatedDataTable.defaultRowsPerPage;
  int _sortColumnIndex;
  bool _sortAscending = true;
  DataSourceInfos _dataSourceInfos;
  @override
  void initState() {
    super.initState();
    getDataSources();
  }

  Future<void> getDataSources() async {
    String dataSources;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      dataSources = await Core.getDataSources;
    } on PlatformException {
      dataSources = null;
    }
    if (!mounted) return;

    setState(() {
      _dataSourceInfos = new DataSourceInfos(dataSources);
    });
  }

  void _sort<T>(Comparable<T> getField(DataSourceInfo d), int columnIndex, bool ascending) {
    _dataSourceInfos._sort<T>(getField, ascending);
    setState(() {
      _sortColumnIndex = columnIndex;
      _sortAscending = ascending;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Data tables'),
        ),
        body: _dataSourceInfos==null?SizedBox():ListView(
            children: <Widget>[
              PaginatedDataTable(
                  header: const Text('Datasources'),
                  rowsPerPage: _rowsPerPage,
                  onRowsPerPageChanged: (int value) { setState(() { _rowsPerPage = value; }); },
                  sortColumnIndex: _sortColumnIndex,
                  sortAscending: _sortAscending,
//                  onSelectAll: _dessertsDataSource._selectAll,
                  columns: <DataColumn>[
                    DataColumn(
                        label: const Text('DataSource'),
                        onSort: (int columnIndex, bool ascending) => _sort<String>((DataSourceInfo d) => d.uuid, columnIndex, ascending)
                    ),
                    DataColumn(
                        label: const Text('# Sample'),
                        numeric: true,
                        onSort: (int columnIndex, bool ascending) => _sort<num>((DataSourceInfo d) => d.dataCount, columnIndex, ascending)
                    ),
                    DataColumn(
                        label: const Text('# Sample(1 hr)'),
                        numeric: true,
                        onSort: (int columnIndex, bool ascending) => _sort<num>((DataSourceInfo d) => d.dataCountLastHour, columnIndex, ascending)
                    ),
                    DataColumn(
                        label: const Text('Last'),
                        numeric: true,
                        onSort: (int columnIndex, bool ascending) => _sort<num>((DataSourceInfo d) => d.lastDataTime, columnIndex, ascending)
                    ),
                  ],
                  source: _dataSourceInfos
              )
            ]
        )
    );
  }
}
