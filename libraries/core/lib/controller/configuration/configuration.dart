class Configuration {
  final Map<String, dynamic> config;
  final Map<String, dynamic> defaultConfig;

  Configuration(this.config, this.defaultConfig);

  Map<String, dynamic> getById(String id) {
    Map<String, dynamic> mC = _getById(id + "_", config);
    Map<String, dynamic> mD = _getById(id + "_", defaultConfig);
    mD.addAll(mC);
    return mD;
  }

  Map<String, dynamic> getDefaultById(String id) {
    return _getById(id + "_", defaultConfig);
  }

  void setById(String id, Map<String, dynamic> c) {
    _removeById(id + "_", config);
    c.removeWhere((key, value) =>
        defaultConfig[key] != null &&
        (defaultConfig[key].toString() == value.toString()));
    config.addAll(c);
  }

  Map<String, dynamic> _getById(String id, Map<String, dynamic> c) {
    final filteredMap = new Map<String, dynamic>.fromIterable(
        c.keys.where((k) => k.startsWith(id)),
        key: (k) => k,
        value: (k) => c[k]);
    return filteredMap;
  }

  void _removeById(String id, Map<String, dynamic> c) {
    c.removeWhere((key, value) => key.startsWith(id));
  }

  dynamic getByKey(String key) {
    var value = config[key];
    if (value == null) value = defaultConfig[key];
    return value;
  }
}
