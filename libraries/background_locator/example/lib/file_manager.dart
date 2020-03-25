import 'dart:io';

import 'package:path_provider/path_provider.dart';

class FileManager {
  static Future<void> writeToLogFile(String log) async {
    print("writing...");
    final file = await _getTempLogFile();
    await file.writeAsString(log, mode: FileMode.append);
  }

  static Future<String> readLogFile() async {
    final file = await _getTempLogFile();
    return file.readAsString();
  }

  static Future<File> _getTempLogFile() async {
//    final directory = await getExternalStorageDirectory();
//    final directory = await getTemporaryDirectory();
    String path = "/storage/emulated/0/Android/data/rekab.app.background_locator_example/files/log.txt";
//    final file = File('${directory.path}/log.txt');
    final file = File(path);
    if (!await file.exists()) {
      await file.writeAsString('');
    }
    return file;
  }

  static Future<void> clearLogFile() async {
    final file = await _getTempLogFile();
    await file.writeAsString('');
  }
}
