import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';

import 'mc_datasource.dart';

class MCDataSourceResult extends Equatable{
  final String uuid;
  final int createTimestamp;
  final MCDataSource dataSource;
  MCDataSourceResult._internal(this.uuid, this.createTimestamp, this.dataSource);

  static MCDataSourceResult create({@required String uuid, @required int createTimestamp, @required MCDataSource dataSource}){
    return MCDataSourceResult._internal(uuid, createTimestamp, dataSource);
  }

  @override
  List<Object> get props => [uuid, createTimestamp, dataSource];
}