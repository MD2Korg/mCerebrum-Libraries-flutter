import 'dart:convert';
import 'dart:io';

import 'package:path_provider/path_provider.dart';

class FileUtils {
  static Future<String> get externalDirectoryPath async {
    Directory directory = await getExternalStorageDirectory();
    if (directory == null) directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  static Future<String> get tempDirectoryPath async {
    Directory directory = await getTemporaryDirectory();
    return directory.path;
  }

  static Future<String> get internalDirectoryPath async {
    Directory directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  static Future<Map<String, dynamic>> readAsMap(String configFilepath) async {
    try {
      final file = File(configFilepath);

      String contents = await file.readAsString();
      return json.decode(contents);
    } catch (e) {
      return new Map<String, dynamic>();
    }
  }

  static Future<void> writeMapAsJson(
      String configFilepath, Map<String, dynamic> config) async {
    final file = File(configFilepath);
    String contents = json.encode(config);
    await file.writeAsString(contents);
  }
}
