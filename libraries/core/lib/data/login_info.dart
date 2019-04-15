import 'package:core/data/ConfigId.dart';

class LoginInfo{
  Map<String, dynamic> map;

  LoginInfo.name(Map<String, dynamic> map) {
    this.map = map;
  }
  LoginInfo(){
    map = new Map<String, dynamic>();
  }
  bool get isLoggedIn{
    if(map.containsKey(ConfigId.core_login_isLoggedIn)){
      return map[ConfigId.core_login_isLoggedIn];
    }else return false;
  }
  String get userId {
    if(map.containsKey(ConfigId.core_login_userId))
      return map[ConfigId.core_login_userId];
    else return "";
  }
  String get serverAddress {
    if(map.containsKey(ConfigId.core_login_serverAddress))
      return map[ConfigId.core_login_serverAddress];
    else return "";
  }
  String get password {
    if(map.containsKey(ConfigId.core_login_password))
      return map[ConfigId.core_login_password];
    else return "";
  }
  String get accessToken {
    if(map.containsKey(ConfigId.core_login_accessToken))
      return map[ConfigId.core_login_accessToken];
    else return "";
  }
  int get firstLoginTime {
    if(map.containsKey(ConfigId.core_login_firstLoginTime))
      return map[ConfigId.core_login_firstLoginTime];
    else return -1;
  }
  int get lastLoginTime {
    if(map.containsKey(ConfigId.core_login_lastLoginTime))
      return map[ConfigId.core_login_lastLoginTime];
    else return -1;
  }

}