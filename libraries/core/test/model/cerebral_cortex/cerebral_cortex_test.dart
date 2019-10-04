// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'dart:io';

import 'package:core/controller/datakit/archive/messagepack/msg_pack.dart';
import 'package:core/controller/datakit/archive/messagepack/stream_metadata.dart';
import 'package:core/controller/uploader/cerebral_cortex.dart';
import 'package:core/controller/uploader/login_response.dart';
import 'package:core/controller/uploader/register_response.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';

void main() {
  CerebralCortex cerebralCortex;
  MCDataSourceResult dataSourceResult;
  const MethodChannel channel =
      MethodChannel('plugins.flutter.io/package_info');
  setUpAll(() async {
    cerebralCortex = new CerebralCortex();
    cerebralCortex.init("https://odin.md2k.org");
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return {
        "appName": "appName",
        "packageName": "packageName",
        "version": "version",
        "buildNumber": "buildNumber"
      };
    });
    MCDataDescriptor x = MCDataDescriptor.create(
        "x", "x", "Measure X axis of accelerometer", MCDataType.STRING);
    MCDataSource dataSource = await MCDataSource.create(
        dataSourceType: "test2",
        dataDescriptors: [x],
        dataSourceMetaData:
            MCDataSourceMetaData.create("test", "test", "Accelerometer data"));
    dataSourceResult = MCDataSourceResult.create(
        uuid: dataSource.getUUID(),
        createTimestamp: new DateTime.now().millisecondsSinceEpoch,
        dataSource: dataSource);

//    final directory = await Directory.systemTemp.createTemp();
/*
    const MethodChannel('plugins.flutter.io/path_provider')
        .setMockMethodCallHandler((MethodCall methodCall) async {
        return directory.path;
    });
*/
  });
  test('valid login', () async {
    String a = "9QTE4OVGZfVoEP7Hy3XnuWofKRb2";
    LoginResponse l = await cerebralCortex.login(a, a);
    print("loginresponse " + (l != null).toString());
    expect(l != null, true);
  });

  test('invalid login', () async {
    String a = "9QTE4OVGZfVoEP7Hy3XnuWofKRb21";
    LoginResponse l;
    try {
      l = await cerebralCortex.login(a, a);
      expect(l == null, true);
    } catch (e) {
      expect(l == null, true);
    }
  });
  test('register datasource', () async {
    String a = "9QTE4OVGZfVoEP7Hy3XnuWofKRb2";
    LoginResponse l = await cerebralCortex.login(a, a);
    StreamMetadata streamMetaData =
        MessagePack.buildStreamMetadata(dataSourceResult.dataSource);
    RegisterResponse response =
        await cerebralCortex.registerDataSource(l, streamMetaData);
    expect(response != null, true);
  });

  test('upload data', () async {
    List<MCData> list = [
      MCData.create(
          dataSourceResult, DateTime.now().millisecondsSinceEpoch, ["a"]),
      MCData.create(
          dataSourceResult, DateTime.now().millisecondsSinceEpoch, ["b"]),
      MCData.create(
          dataSourceResult, DateTime.now().millisecondsSinceEpoch, ["c"])
    ];
//    Uint8List res = MessagePack.createMessagePack(dataSourceResult, list);
    List<int> data =
        MessagePack.createZippedMessagePack(dataSourceResult, list);
    final directory = await Directory.systemTemp.createTemp();

    String zipPath = directory.path + "/test.gzip";

//    List<int> data = new GZipEncoder().encode(res.buffer.asInt8List());

    File fp = File(zipPath);
    fp.writeAsBytesSync(data);

    String a = "9QTE4OVGZfVoEP7Hy3XnuWofKRb2";
    LoginResponse l = await cerebralCortex.login(a, a);
    StreamMetadata streamMetaData =
        MessagePack.buildStreamMetadata(dataSourceResult.dataSource);
//    String s = json.encode(streamMetaData);
    //StreamMetadata  stream=json.decode(s);
    RegisterResponse response =
        await cerebralCortex.registerDataSource(l, streamMetaData);
    bool resUpload = await cerebralCortex.uploadData(l, response, zipPath);

    expect(resUpload, true);
  });
}
