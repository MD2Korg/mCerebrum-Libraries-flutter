import 'dart:convert';
import 'dart:io';

import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';

import 'messagepack/msg_pack.dart';
import 'messagepack/stream_metadata.dart';

class ArchiveManager {
  String _directory;

  Future<void> init(String directory) async {
    _directory = directory;
    await Directory(directory).create(recursive: true);
  }

  Future<void> archive(
      MCDataSourceResult dataSourceResult, List<MCData> data) async {
    String timeStr = DateTime.now().microsecondsSinceEpoch.toString();
    String streamPath =
        _directory + "/" + dataSourceResult.uuid + timeStr + ".json";
    String dataPath =
        _directory + "/" + dataSourceResult.uuid + timeStr + ".gzip";
    StreamMetadata streamMetadata =
        MessagePack.buildStreamMetadata(dataSourceResult.dataSource);
    File fStream = new File(streamPath);
    fStream.writeAsStringSync(jsonEncode(streamMetadata.toJson()));
    List<int> zippedData =
        MessagePack.createZippedMessagePack(dataSourceResult, data);
    File fData = new File(dataPath);
    fData.writeAsBytesSync(zippedData);
  }
}
