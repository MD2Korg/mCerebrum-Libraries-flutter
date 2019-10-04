import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/mc_library.dart';

class RouterData {
  Map<String, List<DataCallback>> subscribers;

  Future<void> start() async {
    subscribers = new Map();
  }

  Future<void> stop() async {
    subscribers.clear();
  }

  void add(String uuid, DataCallback callback) {
    List<DataCallback> l = subscribers[uuid];
    if (l == null) {
      l = new List();
    }
    l.add(callback);
    subscribers[uuid] = l;
  }

  void remove(DataCallback callback) {
    subscribers.forEach((key, value) {
      value.removeWhere((e) {
        return e == callback;
      });
    });
  }

  void notify(List<MCData> mcData) {
    for (int i = 0; i < mcData.length; i++) {
      List<DataCallback> l = subscribers[mcData[i].uuid];
      if (l == null) return;
      l.forEach((e) {
        e([mcData[i]]);
      });
    }
  }
}
