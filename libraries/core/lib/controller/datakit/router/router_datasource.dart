import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/mc_library.dart';

class RouterDataSource {
  Map<DataSourceCallback, MCDataSource> subscribers;

  Future<void> start() async {
    subscribers = new Map();
  }

  Future<void> stop() async {
    subscribers.clear();
  }

  void add(MCDataSource dataSource, DataSourceCallback callback) {
    subscribers[callback] = dataSource;
  }

  void remove(DataSourceCallback callback) {
    subscribers.remove(callback);
  }

  void notify(MCDataSourceResult dataSourceResult) {
    subscribers.forEach((key, value) {
      if (value.isSubsetOf(dataSourceResult.dataSource)) key(dataSourceResult);
    });
  }
}
