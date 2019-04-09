class LoginInfo{
  String userId;
  String serverAddress;
  bool isLoggedIn;
  String password;
  String accessToken;
  int createTimestamp;

  LoginInfo({this.userId="", this.serverAddress="", this.isLoggedIn=false, this.password="",
      this.accessToken="", this.createTimestamp=0});

  LoginInfo.name(Map<String, dynamic> map){
    userId = map["userId"];
    serverAddress = map["serverAddress"];
    createTimestamp = map["createTimestamp"];
    isLoggedIn = map["isLoggedIn"];
    password = map["password"];
    accessToken = map["accessToken"];
  }
}