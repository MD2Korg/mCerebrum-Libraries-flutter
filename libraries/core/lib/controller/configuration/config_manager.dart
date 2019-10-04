import 'dart:io';

import 'package:core/controller/utils/file_utils.dart';
import 'package:flutter/material.dart';

import 'configuration.dart';

class ConfigManager {
  Configuration _configuration;
  String _directory, _configFilename, _defaultConfigFilename;

  Future<void> init(
      {@required String directory,
      String configFilename = "config.json",
      String defaultConfigFilename = "default_config.json"}) async {
    _directory = directory;
    _configFilename = configFilename;
    _defaultConfigFilename = defaultConfigFilename;
    await new Directory(directory).create(recursive: true);

    Map<String, dynamic> config =
        await FileUtils.readAsMap(directory + "/" + configFilename);
    Map<String, dynamic> defaultConfig =
        await FileUtils.readAsMap(directory + "/" + defaultConfigFilename);
    _configuration = new Configuration(config, defaultConfig);
  }

  Future<Map<String, dynamic>> getById(String id) async =>
      _configuration.getById(id);

  Future<Map<String, dynamic>> getDefaultById(String id) async =>
      _configuration.getDefaultById(id);

  dynamic getByKey(key) => _configuration.getByKey(key);

  Future<void> setById(String id, Map<String, dynamic> c) async {
    _configuration.setById(id, c);
    await FileUtils.writeMapAsJson(
        _directory + "/" + _configFilename, _configuration.config);
  }

  Future<void> replaceDefaultConfig(Map<String, dynamic> defaultConfig) async {
    Map<String, dynamic> d = defaultConfig;
    if (d == null) d = _configuration.defaultConfig;
    if (d == null) d = new Map<String, dynamic>();
    _configuration = new Configuration(_configuration.config, d);
    await FileUtils.writeMapAsJson(_directory + "/" + _defaultConfigFilename,
        _configuration.defaultConfig);
  }

  Future<void> replaceConfig(Map<String, dynamic> config) async {
    Map<String, dynamic> c = config;
    Map<String, dynamic> d = _configuration.defaultConfig;
    if (c == null) c = new Map<String, dynamic>();
    if (d == null) d = new Map<String, dynamic>();

    c.removeWhere((key, value) =>
        d[key] != null && (d[key].toString() == value.toString()));
    _configuration = new Configuration(c, d);
    await FileUtils.writeMapAsJson(
        _directory + "/" + _configFilename, _configuration.config);
  }

  int getUploadTime() {
    int uploadTime = getByKey("core_upload_time");
    if (uploadTime == null) uploadTime = 15 * 60 * 1000;
    return uploadTime;
  }

  String getUserId() => getByKey("core_login_userId");

  String getServerAddress() =>
      getByKey("core_cc_address") ?? "https://odin.md2k.org";
}
