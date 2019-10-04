import 'dart:async';

import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:phonesensor/data/pdata.dart';

abstract class ISensor{
  final String id;
  MCDataSource _dataSource;
  ISensor(this.id);
  Future<void> init() async{
    _dataSource = await createDataSource();
  }
  void start(StreamController<PData> streamController);
  void stop();
  Future<MCDataSource> createDataSource();
  MCDataSource getDataSource(){
    return _dataSource;
  }

/*
  bool isRunning();
  int getSampleNo();
  int getStartTimestamp();
  int getLastSampleTimestamp();
  List<double> getLastSample();
*/

}