// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:core/controller/configuration/configuration.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  Configuration configuration;
  setUpAll(() async {
    Map defaultConfig = {
      "core_a": "a",
      "core_b": 10,
      "core_c": [
        {
          "ca": "ca",
          "cb": 20,
          "cc": [
            {"cca": "cca", "ccb": 30}
          ]
        }
      ]
    };
    Map config = {"core_x": "x", "core_y": 20, "core_z": 23.4};
    configuration = new Configuration(config, defaultConfig);
  });
  test('getById', () async {
    Map res = configuration.getById("core");
    expect(res.length, 6);
  });
  test('getDefaultById', () async {
    Map res = configuration.getDefaultById("core");
    expect(res.length, 3);
  });
  test('getByKey', () async {
    var res = configuration.getByKey("core_x");
    expect(res, "x");
    res = configuration.getByKey("core_y");
    expect(res, 20);
    res = configuration.getByKey("core_a");
    expect(res, "a");
    res = configuration.getByKey("core_b");
    expect(res, 10);
    res = configuration.getByKey("core_z");
    expect(res, 23.4);
    double value = configuration.getByKey("core_z") as double;
    expect(value, 23.4);
    res = configuration.getByKey("core_c");
    expect(res[0]["ca"], "ca");
  });
  test('setById new value', () async {
    Map a = configuration.getById("core");
    a["core_m"] = "m";
    configuration.setById("core", a);
    a = configuration.getDefaultById("core");
    expect(a.length, 3);
    a = configuration.getById("core");
    expect(a.length, 7);
  });
  test('setById  value same', () async {
    Map a = configuration.getById("core");
    a["core_a"] = "a";
    configuration.setById("core", a);
    a = configuration.getDefaultById("core");
    expect(a.length, 3);
    a = configuration.getById("core");
    expect(a.length, 6);
    expect(a["core_a"], "a");
  });
  test('setById value different', () async {
    Map a = configuration.getById("core");
    a["core_a"] = "b";
    configuration.setById("core", a);
    a = configuration.getDefaultById("core");
    expect(a.length, 3);
    a = configuration.getById("core");
    expect(a.length, 6);
    expect(a["core_a"], "b");
  });
  test('setById value same (complex)', () async {
    Map a = configuration.getById("core");
    a["core_c"] = [
      {
        "cb": 20,
        "ca": "ca",
        "cc": [
          {"cca": "cca", "ccb": 30}
        ]
      }
    ];
    configuration.setById("core", a);
    a = configuration.getDefaultById("core");
    expect(a.length, 3);
    a = configuration.getById("core");
    expect(a.length, 6);
  });
}
