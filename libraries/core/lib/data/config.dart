
class Config {
  static String core_config_id = "core_config_id";
  static String core_config_title = "core_config_title";
  static String core_config_description = "core_config_description";
  static String core_config_version = "core_config_version";
  static String core_config_icon = "core_config_icon";
  static String core_config_icon_name = "name";
  static String core_config_icon_type = "type";
  static String core_config_icon_content = "content";
  static String core_config_filename = "core_config_filename";
  static String core_config_from = "core_config_from";
  static String core_config_createTime = "core_config_createTime";
  static String core_config_publishTime = "core_config_publishTime";
  static String core_config_webAddress = "core_config_webAddress";
  static String core_config_fileSize = "core_config_fileSize";
  static String core_login_serverAddress = "core_login_serverAddress";
  static String core_login_userId = "core_login_userId";
  static String core_login_password = "core_login_password";
  static String core_login_accessToken = "core_login_accessToken";
  static String core_login_isLoggedIn = "core_login_isLoggedIn";

  static String core_pluginLibrary = "core_pluginLibrary";
  static String core_pluginLibrary_id = "id";
  static String core_pluginLibrary_title = "core_pluginLibrary_title";
  static String core_pluginLibrary_description = "core_pluginLibrary_description";
  static String core_pluginLibrary_enable = "enable";

  Map<String, dynamic> map;

  Config.name(Map<String, dynamic> map) {
    this.map = map;
  }

  Config() {
    map = new Map<String, dynamic>();
  }

  String get version {
    if (map.containsKey(core_config_version))
      return map[core_config_version];
    else
      return "";
  }

  String get filename {
    if (map.containsKey(core_config_filename))
      return map[core_config_filename];
    else return "";
  }

  String get from {
    if (map.containsKey(core_config_from))
      return map[core_config_from];
    else return "";
  }

  int get serverPublishedTime {
    if (map.containsKey(core_config_publishTime))
      return map[core_config_publishTime];
    else return -1;
  }

  bool get isFromCerebralCortex{
    if (map.containsKey(core_config_from)) {
      String value = map[core_config_from];
      if(value=="cerebral_cortex") return true;
      else return false;
    }
  }

  bool get isLoggedIn {
    if (map.containsKey(core_login_isLoggedIn)) {
      return map[core_login_isLoggedIn];
    } else
      return false;
  }

  String get userId {
    if (map.containsKey(core_login_userId))
      return map[core_login_userId];
    else
      return "";
  }

  String get serverAddress {
    if (map.containsKey(core_login_serverAddress))
      return map[core_login_serverAddress];
    else
      return "";
  }

  String get password {
    if (map.containsKey(core_login_password))
      return map[core_login_password];
    else
      return "";
  }

  String get accessToken {
    if (map.containsKey(core_login_accessToken))
      return map[core_login_accessToken];
    else
      return "";
  }

}
