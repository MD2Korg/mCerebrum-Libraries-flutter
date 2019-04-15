import 'package:core/data/ConfigId.dart';

class Config {
  Map<String, dynamic> map;

  Config.name(Map<String, dynamic> map) {
    this.map = map;
  }

  Config() {
    map = new Map<String, dynamic>();
  }

  String get filename {
    if(map.containsKey(ConfigId.core_config_filename))
    return map[ConfigId.core_config_filename];
    else return "";
  }
  String get from {
    if(map.containsKey(ConfigId.core_config_from))
      return map[ConfigId.core_config_from];
    else return "";
  }
  int get serverPublishedTime {
    if(map.containsKey(ConfigId.core_config_publishTime))
      return map[ConfigId.core_config_publishTime];
    else return -1;
  }
  bool get isFromCerebralCortex{
    if(map.containsKey(ConfigId.core_config_from)){
      String value = map[ConfigId.core_config_from];
      if(value=="cerebral_cortex") return true;
      else return false;
    }
  }
}
